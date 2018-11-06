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
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import byteNumberConverter.ByteIntConverter;
import byteNumberConverter.ByteLongConverter;
import project1.Chunk;
import project1.FileSplitter;
import project2.args.Arg;
import project2.args.ArgList;
import project2.args.ErrorChanceArg;
import project2.args.FileArg;
import project2.args.HelpArg;
import project2.args.IntroduceErrorArg;
import project2.args.LogPrintingArg;
import project2.args.MaxSizeOfChunkArg;
import project2.args.NumberOfAckNumbersArg;
import project2.args.ReceiverAddressArg;
import project2.args.ReceiverPortArg;
import project2.args.SenderAddressArg;
import project2.args.SenderPortArg;
import project2.args.StartingDelayArg;
import project2.args.TimeoutArg;
import project2.args.TracePrinterArg;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.EndFrame;
import project2.printer.LogPrinter;
import project2.printer.TracePrinter;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrameSender
{
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
	private static StartingDelayArg startingDelayArg = new StartingDelayArg("-startDelay");

	// Toggle Args
	private static IntroduceErrorArg introduceErrorArg = new IntroduceErrorArg("-e");
	private static TracePrinterArg tracePrinterArg = new TracePrinterArg("-trace");
	private static LogPrintingArg logPrintingArg = new LogPrintingArg("-reqlog");
	@SuppressWarnings("unused")
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameSender.SENDER_PROGRAM_TITLE, ChunkFrameSender.SENDER_PROGRAM_DESCRIPTION);

	// Printers
	private static TracePrinter tracePrinter = new TracePrinter(false, null);
	private static LogPrinter log = new LogPrinter(false, null, null);

	// start time
	private static Long startTime;

	// offsets for logging
	private static long startOffset = 0;
	private static long endOffset = -1;

	/**
	 * Runs the sender program
	 * 
	 * @param args
	 *            use -help arg to see about valid args
	 */
	public static void main(String[] args)
	{
		int sequenceNumber = 0;
		InetAddress destinationAddress;
		int destinationPort;

		// handle the command line arguments
		try
		{
			ArgList.updateFromMainArgs(args);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// set up tracePrinter printer
		tracePrinter = new TracePrinter(tracePrinterArg, System.out);

		// set destination
		tracePrinter.println("");
		tracePrinter.println("setting destination");
		destinationAddress = receiverAddressArg.getValue();
		destinationPort = receiverPortArg.getValue();

		// gets the input file
		tracePrinter.println("");
		tracePrinter.println("geting input file");
		File inFile = fileArg.getValue();
		tracePrinter.println("inFile = " + inFile.getAbsolutePath());

		// split up the file into chunks
		tracePrinter.println("");
		tracePrinter.println("splitting up the file into chunks");
		LinkedList<Chunk> chunkList = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter(inFile.getAbsolutePath(), maxSizeOfChunkArg.getValue());
		try
		{
			splitter.overwrite(chunkList);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// set up the socket
		tracePrinter.println("");
		tracePrinter.println("setting up the socket");
		DatagramSocket socket = null;

		try
		{
			socket = new DatagramSocket(senderPortArg.getValue());
			socket.setSoTimeout(timeoutArg.getValue());

			// make connection and transmit setup info
			tracePrinter.println("");
			tracePrinter.println("making connection and transmitting setup info");
			sendArgs(socket, destinationAddress, destinationPort);

			// set up log printer (has to happen after the start time is
			// established)
			tracePrinter.println("");
			tracePrinter.println("Setting up the log printer");
			log = new LogPrinter(logPrintingArg.getValue(), System.out, startTime);

			// tell receiver how many chunks will be sent

			// frame and send all chunks using Stop and Wait
			tracePrinter.println("");
			tracePrinter.println("sending all chunks with stop and wait");
			while (!chunkList.isEmpty())
			{
				tracePrinter.println("\t" + "chunkList.size(): " + chunkList.size());
				ChunkFrame chunkFrame = null;

				// get the next chunk if we are not handling a delayed frame
				tracePrinter.println("\t" + "chunk removed from chunk list");

				Chunk chunk = chunkList.remove();

				// frame the chunk
				tracePrinter.println("\t" + "chunk framed");
				chunkFrame = new ChunkFrame(chunk, sequenceNumber);

				tracePrinter.println("\t\t" + "chunkFrame.getSequenceNumber(): " + chunkFrame.getSequenceNumber());
				tracePrinter.println("\t\t" + "chunkFrame.getLength(): " + chunkFrame.getLength());
				tracePrinter.println("\t\t" + "chunkFrame.failedCheckSum(): " + chunkFrame.failedCheckSum());

				// Send it with the stop and wait
				tracePrinter.println("\t" + "send chunk frame with stop and wait");
				sendWithStopAndWait(chunkFrame, socket, destinationAddress, destinationPort);

				// progress to next sequence number
				tracePrinter.println("\t" + "sequence number increased");
				sequenceNumber++;
			}

			// signal end of transmission
			sendEndFlag(sequenceNumber++, socket, destinationAddress, destinationPort);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// close socket
		socket.close();

		tracePrinter.println("");
		tracePrinter.println("END");
	}

	/**
	 * Sends the needed arguments to the sender
	 * 
	 * @param socket
	 *            the socket that the datagram will be sent with
	 * @param destinationAddress
	 *            the address of the destination for this ChunkFrame
	 * @param destinationPort
	 *            the destination for this ChunkFrame
	 */
	private static void sendArgs(DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
	{
		try
		{
			int maxLength = 0;
			boolean ackReceived = false;
			ByteArrayOutputStream baos;
			ObjectOutputStream oos;
			Queue<Chunk> chunks = new LinkedList<Chunk>();
			Queue<Arg<?>> args = new LinkedList<Arg<?>>();
			DatagramPacket packet;
			DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.ACK_SIZE], AckFrame.ACK_SIZE);
			AckFrame ackFrame;

			// add needed Args to the args queue
			args.add(fileArg);
			args.add(senderAddressArg);
			args.add(senderPortArg);
			args.add(receiverPortArg);
			args.add(errorChanceArg);
			args.add(maxSizeOfChunkArg);
			args.add(numberOfAckNumbersArg);
			args.add(introduceErrorArg);
			args.add(tracePrinterArg);
			args.add(logPrintingArg);

			// backup the timout setting of the socket and use default timeout
			// for
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
					tracePrinter.println(e.getLocalizedMessage());
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
			startTime = new Date().getTime() + startingDelayArg.getValue();
			Chunk c = new Chunk(ByteLongConverter.convert(startTime), 8);
			ChunkFrame cf = new ChunkFrame(c, seqNum);
			packet = cf.toDatagramPacket(destinationAddress, destinationPort);

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
				}
			}

			// catch delayed packets
			int tmp = socket.getSoTimeout();
			socket.setSoTimeout(1);
			while (new Date().getTime() < startTime)
			{
				try
				{
					socket.receive(ackPacket);
				}
				catch (Exception e)
				{
					// keep trying
				}
			}
			socket.setSoTimeout(tmp);

			// restore timout value of socket
			socket.setSoTimeout(timeoutBackup);

			// catch delayed acks
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Sends the End Flag, an empty payload ChunkFrame
	 * 
	 * @param seqNumber
	 *            The sequence number for the ChunkFrame
	 * @param socket
	 *            the socket that the datagram will be sent with
	 * @param destinationAddress
	 *            the address of the destination for this ChunkFrame
	 * @param destinationPort
	 *            the destination for this ChunkFrame
	 */
	private static void sendEndFlag(int seqNumber, DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
	{
		int numberOfTries = 10;
		EndFrame endFrame = new EndFrame(seqNumber);
		boolean done = false;
		boolean ackMatch = false;
		boolean sumCheckPass = false;
		boolean first = true;
		DatagramPacket endPacket = endFrame.toDatagramPacket(destinationAddress, destinationPort);
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.ACK_SIZE], AckFrame.ACK_SIZE);
		int expectedAckNumber = endFrame.getSequenceNumber() % numberOfAckNumbersArg.getValue();

		// Update offsets
		int digitLengthOfOffset = Long.toString(startOffset).length();

		while (!done && numberOfTries-- > 0)
		{
			try
			{
				// send the package
				socket.send(endPacket);

				// log the sending
				if (first)
				{

					log.sent(endFrame, digitLengthOfOffset);
					first = false;
				}
				else
				{
					log.resent(endFrame, digitLengthOfOffset);
				}

				// receive a package or timeout
				socket.receive(ackPacket);

				// extract ack frame from package
				AckFrame ackFrame = new AckFrame(ackPacket);

				// log the received ack
				log.ackReceived(ackFrame, expectedAckNumber, endFrame.getSequenceNumber(), numberOfAckNumbersArg.getValue());

				// check if received ack number matches expected ack number
				ackMatch = ackFrame.getAckNumber() == expectedAckNumber;

				// check if the ackFrame passed the check sum
				sumCheckPass = ackFrame.passedCheckSum();

				// Determines if we are done trying to send the packet again
				done = ackMatch && sumCheckPass;
			}
			catch (SocketTimeoutException e)
			{
				log.timeout(endFrame);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends a ChunkFrame with stop and wait.
	 * 
	 * @param chunkFrame
	 *            the ChunkFrame to send
	 * @param socket
	 *            the socket that the datagram will be sent with
	 * @param destinationAddress
	 *            the address of the destination for this ChunkFrame
	 * @param destinationPort
	 *            the destination for this ChunkFrame
	 */
	private static void sendWithStopAndWait(ChunkFrame chunkFrame, DatagramSocket socket, InetAddress destinationAddress, int destinationPort)
	{
		boolean done = false;
		boolean ackMatch = false;
		boolean sumCheckPass = false;
		boolean first = true;
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.ACK_SIZE], AckFrame.ACK_SIZE);
		int expectedAckNumber = chunkFrame.getSequenceNumber() % numberOfAckNumbersArg.getValue();

		// Update offsets
		startOffset = endOffset + 1;
		endOffset = startOffset + chunkFrame.getLength() - ChunkFrame.HEADER_SIZE - 1;

		while (!done)
		{
			try
			{
				// generate errors randomly using the generator
				if (introduceErrorArg.getValue())
				{
					chunkFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChanceArg.getValue()));
				}

				// simulate drops
				if (chunkFrame.isDropped())
				{
					// log the drop
					if (first)
					{

						log.sent(chunkFrame, startOffset, endOffset);
						first = false;
					}
					else
					{
						log.resent(chunkFrame, startOffset, endOffset);
					}

					// do not actually send

				}

				// simulate sending corrupt package
				else if (chunkFrame.failedCheckSum())
				{

					// log the sending
					if (first)
					{

						log.sent(chunkFrame, startOffset, endOffset);
						first = false;
					}
					else
					{
						log.resent(chunkFrame, startOffset, endOffset);
					}

					// send the package
					chunkFrame.send(socket, destinationAddress, destinationPort);

				}

				// normal case
				else
				{

					// log the sending
					if (first)
					{

						log.sent(chunkFrame, startOffset, endOffset);
						first = false;
					}
					else
					{
						log.resent(chunkFrame, startOffset, endOffset);
					}

					// send the package
					chunkFrame.send(socket, destinationAddress, destinationPort);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{

				// receive a package or timeout
				socket.receive(ackPacket);

				// extract ack frame from package
				AckFrame ackFrame = new AckFrame(ackPacket);

				// log the received ack
				log.ackReceived(ackFrame, expectedAckNumber, chunkFrame.getSequenceNumber(), numberOfAckNumbersArg.getValue());

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
				e.printStackTrace();
			}
		}
	}
}
