/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import oracle.jrockit.jfr.parser.ChunkParser;
import project1.Chunk;
import project1.FileSplitter;
import project2.args.*;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrameSender
{
	private static final Random RANDOM = new Random();

	private final static String SENDER_PROGRAM_TITLE = "Chunk Frame Sender Program";
	private final static String SENDER_PROGRAM_DESCRIPTION = "This program sends a file using our version of the Stop and Wait algorithm.";

	// Args
	private static FileArg fileArg = new FileArg("-f");
	private static SenderAddressArg senderAddressArg = new SenderAddressArg("-sa");
	private static ReceiverAddressArg receiverAddressArg = new ReceiverAddressArg("-ra");
	private static SenderPortArg senderPortArg = new SenderPortArg("-sp");
	private static ReceiverPortArg receiverPortArg = new ReceiverPortArg("-rp");
	private static TimeoutArg timeoutArg = new TimeoutArg("-t");
	private static ErrorChanceArg errorChanceArg = new ErrorChanceArg("-d");
	private static MaxSizeOfChunkArg maxSizeOfChunkArg = new MaxSizeOfChunkArg("-s");
	private static NumberOfAckNumbersArg numberOfAckNumbersArg = new NumberOfAckNumbersArg("-acknums");
	private static MaxDelayArg maxDelayArg = new MaxDelayArg("-md");

	// Toggle Args
	private static IntroduceErrorArg introduceErrorArg = new IntroduceErrorArg("-e");
	private static DebugModeArg debugModeArg = new DebugModeArg("-debug");
	private static LogPrintingArg logPrintingArg = new LogPrintingArg("-reqlog");
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameSender.SENDER_PROGRAM_TITLE, ChunkFrameSender.SENDER_PROGRAM_DESCRIPTION);

	// Printer
	private static final DebugPrinter debug = new DebugPrinter(debugModeArg, System.out);
	private static final LogPrinter log = new LogPrinter(logPrintingArg, System.out);

	// Delayed Frames
	private static DelayedFrameCollection<ChunkFrame> delayedFrames;

	public static void main(String[] args) throws Exception
	{

		int sequenceNumber = 0;
		int ackNumber = 0;
		InetAddress destinationAddress;
		int destinationPort;
		
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);


		// set destination
		destinationAddress = receiverAddressArg.getValue();
		destinationPort = receiverPortArg.getValue();

		// gets the input file
		File inFile = fileArg.getValue();

		// split up the file into chunks
		LinkedList<Chunk> chunkList = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter(inFile.getAbsolutePath(), maxSizeOfChunkArg.getValue());
		splitter.overwrite(chunkList);

		// set up the socket
		DatagramSocket socket = new DatagramSocket(senderPortArg.getValue());
		socket.setSoTimeout(timeoutArg.getValue());

		// make connection and transmit setup info 
		setupConnection(socket, destinationAddress, destinationPort);
		
		// set up the delayed frame collection
		delayedFrames = new DelayedFrameCollection<ChunkFrame>(socket, destinationAddress, destinationPort);

		// frame, package, and send all chunks using Stop and Wait
		while (!chunkList.isEmpty())
		{
			ChunkFrame chunkFrame = null;

			// get the next chunk if we are not handling a delayed frame
			Chunk chunk = chunkList.remove();

			// frame the chunk
			chunkFrame = new ChunkFrame(chunk, sequenceNumber, ackNumber);

			// generate errors randomly using the generator
			if(introduceErrorArg.getValue())
			{
				chunkFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChanceArg.getValue()));
			}
			
			// Send it with the stop and wait
			sendWithStopAndWait(chunkFrame, ackNumber, socket, destinationAddress, destinationPort);

			// progress to next ackNumber
			ackNumber++;
			ackNumber %= numberOfAckNumbersArg.getValue();

			// progress to next sequence number
			sequenceNumber++;
		}
		socket.close();
	}

	private static void sendWithStopAndWait(ChunkFrame chunkFrame, int expectedAckNumber, DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
	{
		boolean done = false;
		boolean ackMatch = false;
		boolean sumCheckPass = false;
		DatagramPacket chunkPacket = chunkFrame.toDatagramPacket(destinationAddress, destinationPort);
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.LENGTH], AckFrame.LENGTH);
		while(!done)
		{
			try
			{		
				// simulate delays
				if(chunkFrame.isDelayed())
				{
					
					/*
					 * The delayedFrames collection is essentially a funnel that we put the frames we want to delay. 
					 * The collection contains a runnable sender that will automatically send the frames when the 
					 * delay elapses. 
					 */
					delayedFrames.add(chunkFrame, delay());
				}
				
				// simulate drops
				else if(chunkFrame.isDropped())
				{
					
				}
				
				// simulate sending corrupt package
				else if(chunkFrame.failedCheckSum())
				{					
					// send the package
					socket.send(chunkPacket);
				}
				
				// normal case
				else
				{
					// send the package
					socket.send(chunkPacket);
				}
				
				// receive a package or timeout
				socket.receive(ackPacket);
	
				// extract ack frame from package
				AckFrame ackFrame = new AckFrame(ackPacket);
	
				// check if received ack number matches expected ack number
				ackMatch = ackFrame.getAckNumber() == expectedAckNumber;
	
				// check if the ackFrame passed the check sum
				sumCheckPass = ackFrame.passedCheckSum();
	
				// Determines if we are done trying to send the packet again
				done = ackMatch && sumCheckPass;
			}
			catch (SocketTimeoutException e)
			{
				// TODO write error output for Timeout
			}
			catch (Exception e)
			{
				e.printStackTrace(); // TODO adjust this if desired
			}
		}
	}

	private static int delay()
	{
		return RANDOM.nextInt(maxDelayArg.getValue());
	}

	private static void setupConnection(DatagramSocket socket, InetAddress destinationAddress, int destinationPort) throws IOException
	{
		int maxLength = 0;
		boolean ackReceived = false;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		Queue<Chunk> chunks = new LinkedList<Chunk>();
		Queue<Arg<?>> args = new LinkedList<Arg<?>>();
		args.add(fileArg);
		args.add(senderAddressArg);
		args.add(receiverAddressArg);
		args.add(senderPortArg);
		args.add(receiverPortArg);
		args.add(timeoutArg);
		args.add(errorChanceArg);
		args.add(maxSizeOfChunkArg);
		args.add(numberOfAckNumbersArg);
		args.add(introduceErrorArg);
		args.add(debugModeArg);
		args.add(logPrintingArg);
		args.add(maxDelayArg);
		DatagramPacket packet;
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.LENGTH], AckFrame.LENGTH);
		AckFrame ackFrame;

		// backup the timout setting of the socket and use default timeout for
		// this
		int timeoutBackup = socket.getSoTimeout();
		int ackNumber = 0;
		socket.setSoTimeout(Defaults.TIMEOUT);

		// convert the values of all needed args to byte arrays
		for (Arg<?> arg : args)
		{
			oos.writeObject(arg.getValue());
			oos.flush();
			byte[] bytes = baos.toByteArray();
			if (bytes.length > maxLength)
			{
				maxLength = bytes.length + Defaults.ACK_PACKET_LENGTH + 4;
			}
			chunks.add(new Chunk(bytes, bytes.length));
		}

		// Send max length of packet
		packet = new DatagramPacket(byteIntConverter.ByteIntConverter.convert(maxLength), 4, destinationAddress, destinationPort);

		ackReceived = false;
		while (!ackReceived)
		{
			socket.send(packet);
			try
			{
				socket.receive(ackPacket);
				ackFrame = new AckFrame(ackPacket);
				if (ackFrame.getAckNumber() == ackNumber)
				{
					ackReceived = true;
					ackNumber++;
				}
			}
			catch (Exception e)
			{
				// try again
			}
		}

		// send the args
		for (Chunk c : chunks)
		{
			ackReceived = false;
			while (!ackReceived)
			{
				try
				{
					ChunkFrame f = new ChunkFrame(c, ackNumber, ackNumber);
					packet = f.toDatagramPacket(destinationAddress, destinationPort);
					socket.send(packet);

					socket.receive(ackPacket);
					if (new AckFrame(ackPacket).passedCheckSum())
					{
						ackReceived = true;
						ackNumber++;
					}
				}
				catch (Exception e)
				{
					// try again
				}
			}
		}

		// restore timout value of socket
		socket.setSoTimeout(timeoutBackup);
	}
}
