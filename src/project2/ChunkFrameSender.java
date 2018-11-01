/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import byteNumberConverter.ByteIntConverter;
import byteNumberConverter.ByteLongConverter;
import log.Log;
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

	// Printers
	private static DebugPrinter debug = new DebugPrinter(false, null);
	private static LogPrinter log = new LogPrinter(false, null, null, null, null);

	// start time
	private static Long startTime;

	public static void main(String[] args) throws Exception
	{
		int sequenceNumber = 0;
		int ackNumber = 0;
		InetAddress destinationAddress;
		int destinationPort;
		
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);


		// set up debug printer 
		debug = new DebugPrinter(debugModeArg, System.out);
		
		// set destination
		debug.println("");
		debug.println("setting destination");
		destinationAddress = receiverAddressArg.getValue();
		destinationPort = receiverPortArg.getValue();

		// gets the input file
		debug.println("");
		debug.println("geting input file");
		File inFile = fileArg.getValue();
		debug.println("inFile = " + inFile.getAbsolutePath());

		// split up the file into chunks
		debug.println("");
		debug.println("splitting up the file into chunks");
		LinkedList<Chunk> chunkList = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter(inFile.getAbsolutePath(), maxSizeOfChunkArg.getValue());
		splitter.overwrite(chunkList);

		// set up the socket
		debug.println("");
		debug.println("setting up the socket");
		DatagramSocket socket = new DatagramSocket(senderPortArg.getValue());
		socket.setSoTimeout(timeoutArg.getValue());

		// make connection and transmit setup info 
		debug.println("");
		debug.println("making connection and transmitting setup info");
		setupConnection(socket, destinationAddress, destinationPort);

		// set up log printer (has to happen after the start time is established)
		debug.println("");
		debug.println("Setting up the log printer");
		log = new LogPrinter(logPrintingArg.getValue(), System.out, (int) maxSizeOfChunkArg.getValue(), fileArg.getValue().length(), startTime);
		
		// frame, package, and send all chunks using Stop and Wait
		debug.println("");
		debug.println("sending all chunks with stop and wait");
		while (!chunkList.isEmpty())
		{
			ChunkFrame chunkFrame = null;

			// get the next chunk if we are not handling a delayed frame
			Chunk chunk = chunkList.remove();

			// frame the chunk
			chunkFrame = new ChunkFrame(chunk, sequenceNumber);

			
			// Send it with the stop and wait
			sendWithStopAndWait(chunkFrame, socket, destinationAddress, destinationPort);


			// progress to next sequence number
			sequenceNumber++;
		}
		
		// signal end of transmission
		sendWithStopAndWait(new ChunkFrame(new Chunk(new byte[0], 0), sequenceNumber++), socket, destinationAddress, destinationPort);
		
		// close socket
		socket.close();
		
		debug.println("");
		debug.println("END");
	}


	private static void sendWithStopAndWait(ChunkFrame chunkFrame, DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
	{
		boolean done = false;
		boolean ackMatch = false;
		boolean sumCheckPass = false;
		DatagramPacket chunkPacket = chunkFrame.toDatagramPacket(destinationAddress, destinationPort);
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.ACK_SIZE], AckFrame.ACK_SIZE);
		int expectedAckNumber = chunkFrame.getSequenceNumber() % numberOfAckNumbersArg.getValue();
		while(!done)
		{
			try
			{		
				// generate errors randomly using the generator
				if(introduceErrorArg.getValue())
				{
					chunkFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChanceArg.getValue()));
				}
				
				// simulate drops
				if(chunkFrame.isDropped())
				{
					// do not actually send
					
					// log the drop
					log.sent(chunkFrame);
				}
				
				// simulate sending corrupt package
				else if(chunkFrame.failedCheckSum())
				{			
					
					// send the package
					socket.send(chunkPacket);
					
					// log the sending
					log.sent(chunkFrame);
				}
				
				// normal case
				else
				{
					// send the package
					socket.send(chunkPacket);
					
					// log the sending
					log.sent(chunkFrame);
				}
				
				// receive a package or timeout
				socket.receive(ackPacket);
	
				// extract ack frame from package
				AckFrame ackFrame = new AckFrame(ackPacket);
	
				// log the received ack
				log.ackReceived(ackFrame, expectedAckNumber, chunkFrame.getSequenceNumber());
				
				// check if received ack number matches expected ack number
				ackMatch = ackFrame.getAckNumber() == expectedAckNumber;
	
				// check if the ackFrame passed the check sum
				sumCheckPass = ackFrame.passedCheckSum();
	
				// Determines if we are done trying to send the packet again
				done = ackMatch && sumCheckPass;
			}
			catch (SocketTimeoutException e)
			{
				log.timeout(chunkFrame);
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
		ByteArrayOutputStream baos;
		ObjectOutputStream oos;
		Queue<Chunk> chunks = new LinkedList<Chunk>();
		Queue<Arg<?>> args = new LinkedList<Arg<?>>();
		args.add(fileArg);
		args.add(senderAddressArg);
		args.add(receiverAddressArg);
		args.add(senderPortArg);
		args.add(receiverPortArg);
		args.add(errorChanceArg);
		args.add(maxSizeOfChunkArg);
		args.add(numberOfAckNumbersArg);
		args.add(introduceErrorArg);
		args.add(debugModeArg);
		args.add(logPrintingArg);
		args.add(maxDelayArg);
		DatagramPacket packet;
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.ACK_SIZE], AckFrame.ACK_SIZE);
		AckFrame ackFrame;

		// backup the timout setting of the socket and use default timeout for
		// this
		int timeoutBackup = socket.getSoTimeout();
		int seqNum = 0;
		socket.setSoTimeout(Defaults.TIMEOUT);

		// convert the values of all needed args to byte arrays
		for (Arg<?> arg : args)
		{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(arg.getValue());
			oos.flush();
			byte[] bytes = baos.toByteArray();
			if (bytes.length > maxLength)
			{
				maxLength = bytes.length + Defaults.ACK_PACKET_LENGTH + 4;
			}
			chunks.add(new Chunk(bytes, bytes.length));
			oos.close();
			baos.close();
		}

		// Send max length of packet
		packet = new ChunkFrame(new Chunk(ByteIntConverter.convert(maxLength), 4), seqNum).toDatagramPacket(destinationAddress, destinationPort);

		ackReceived = false;
		while (!ackReceived)
		{
			socket.send(packet);
			try
			{
				socket.receive(ackPacket);
				ackFrame = new AckFrame(ackPacket);
				if (ackFrame.getAckNumber() == seqNum % numberOfAckNumbersArg.getValue())
				{
					ackReceived = true;
					seqNum++;
				}
			}
			catch (Exception e)
			{
				// try again
				debug.println(e.getLocalizedMessage());
			}
		}

		// send the args
		for (Chunk c : chunks)
		{	
			//TODO DELETE THE TOP OF THIS
			try
			{
				ByteArrayInputStream bais;
				ObjectInputStream ois;	
				bais = new ByteArrayInputStream(c.getBytes());
				ois = new ObjectInputStream(bais);
				Object o = ois.readObject();
				System.out.println(o.getClass().getName());
			}
			catch (ClassNotFoundException e1)
			{
				e1.printStackTrace();
			}
			
			
			ackReceived = false;
			while (!ackReceived)
			{
				try
				{
					ChunkFrame f = new ChunkFrame(c, seqNum);
					packet = f.toDatagramPacket(destinationAddress, destinationPort);
					socket.send(packet);

					socket.receive(ackPacket);
					if (new AckFrame(ackPacket).passedCheckSum())
					{
						ackReceived = true;
						seqNum++;
					}
				}
				catch (Exception e)
				{
					// try again
				}
			}
		}
		
		// setup and send start time to keep logs synced
		startTime = new Date().getTime();
		System.out.println("Start Time (sender): " + startTime);
		System.out.println("Start Time (sender): " + ByteLongConverter.convert(ByteLongConverter.convert(startTime)));
		
		Chunk c = new Chunk(ByteLongConverter.convert(startTime), 8);
		ChunkFrame cf = new ChunkFrame(c, seqNum);
		packet = cf.toDatagramPacket(destinationAddress, destinationPort);
		
		ackReceived = false;
		while (!ackReceived)
		{
			System.out.println(".");
			socket.send(packet);
			try
			{
				socket.receive(ackPacket);
				ackFrame = new AckFrame(ackPacket);
				if (ackFrame.getAckNumber() == seqNum % numberOfAckNumbersArg.getValue())
				{
					ackReceived = true;
					seqNum++;
				}
			}
			catch (Exception e)
			{
				// try again
			}
		}

		// restore timout value of socket
		socket.setSoTimeout(timeoutBackup);
		
	}
}
