/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import project1.Chunk;
import project1.FileAssembler;
import project2.args.ArgList;
import project2.args.FileArg;
import project2.args.HelpArg;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.printer.LogPrinter;
import project2.printer.TracePrinter;

/**
 * A program that receives chunks and assembles them into a file
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrameReceiver
{

	// documentation constants
	private final static String RECEIVER_PROGRAM_TITLE = "Chunk Frame Receiver Program";
	private final static String RECEIVER_PROGRAM_DESCRIPTION = "This program receives a file using our version of the Stop and Wait algorithm.";

	// Toggle Args
	@SuppressWarnings("unused")
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameReceiver.RECEIVER_PROGRAM_TITLE, ChunkFrameReceiver.RECEIVER_PROGRAM_DESCRIPTION);

	// Destination vars
	private static InetAddress destinationAddress = Defaults.SENDER_ADDRESS;
	private static int destinationPort = Defaults.SENDER_PORT;

	// Printer
	private static TracePrinter tracePrinter = new TracePrinter(false, null);
	private static LogPrinter log = new LogPrinter(false, null, null);

	// Arguments to receive from sender
	private static File inFile = Defaults.INPUT_FILE;;
	private static InetAddress senderAddress = Defaults.SENDER_ADDRESS;
	private static Integer senderPort = Defaults.SENDER_PORT;
	private static Integer receiverPort = Defaults.RECEIVER_PORT;
	private static Integer errorChance = Defaults.CHANCE_OF_ERROR;
	private static Short maxSizeOfChunk = Defaults.MAX_CHUNK_LENGTH;
	private static Integer numberOfAckNumbers = Defaults.NUMBER_OF_ACK_NUMBERS;
	private static Boolean introduceError = false;
	private static Boolean debugMode = false;
	private static Boolean logPrintingMode = false;
	private static long startTime;
	private static File outFile;

	/**
	 * Runs the receiver program
	 * 
	 * @param args
	 *            command line arguments (not used)
	 */
	public static void main(String[] args)
	{
		try
		{
			// handle the command line arguments
			ArgList.updateFromMainArgs(args);

			// set up the socket
			int port = Defaults.RECEIVER_PORT;
			DatagramSocket socket = new DatagramSocket(port);

			// Get arg info from sender.
			getArgsFromSender(socket);

			// initialize the printers
			tracePrinter = new TracePrinter(debugMode, System.out);
			log = new LogPrinter(logPrintingMode, System.out, startTime);
			// set destination
			tracePrinter.println("");
			tracePrinter.println("setting destination");
			destinationAddress = senderAddress;
			destinationPort = senderPort;

			// update socket from default to specified values
			tracePrinter.println("");
			tracePrinter.println("updating socket");
			socket.close();
			socket = new DatagramSocket(receiverPort);

			// Determine Packet Size
			tracePrinter.println("");
			tracePrinter.println("determining max packet size");
			short chunkPacketSize = (short) (project2.Defaults.ACK_PACKET_LENGTH + 4 + maxSizeOfChunk);

			// set the expected sequence number to 0
			tracePrinter.println("");
			tracePrinter.println("setting expected sequence number to zero");
			int expectedSequenceNumber = 0;

			// receive all chunks using Stop and Wait
			tracePrinter.println("");
			tracePrinter.println("Receiving all chunks with stop and wait");
			AckFrame ackFrame;
			Chunk chunk;
			ChunkFrame chunkFrame;
			DatagramPacket chunkPacket = new DatagramPacket(new byte[chunkPacketSize], chunkPacketSize);
			Queue<Chunk> chunkList = new LinkedList<Chunk>();

			boolean first = true;
			Integer last = Integer.MIN_VALUE;
			boolean end = false;
			while (!end)
			{
				boolean done = false;
				boolean seqDeservesAck = false;
				boolean sumCheckPass = false;

				while (!done)
				{
					try
					{
						tracePrinter.println("\t" + "expecting sequence number " + expectedSequenceNumber);

						// receive chunk packet
						tracePrinter.println("\t" + "receive chunk DatagramPacket");

						socket.receive(chunkPacket);

						// extract the chunk frame
						tracePrinter.println("\t" + "Extract Chunk Frame");

						chunkFrame = new ChunkFrame(chunkPacket);

						tracePrinter.println("\t\t" + "chunkFrame.getSequenceNumber(): " + chunkFrame.getSequenceNumber());
						tracePrinter.println("\t\t" + "chunkFrame.getLength(): " + chunkFrame.getLength());
						tracePrinter.println("\t\t" + "chunkFrame.failedCheckSum(): " + chunkFrame.failedCheckSum());

						// Log received packet info
						tracePrinter.println("\t" + "Log received packet info");

						log.chunkReceived(chunkFrame, expectedSequenceNumber);

						// check if the ackFrame passed the check sum
						tracePrinter.println("\t" + "check if the ackFrame passed the check sum");

						sumCheckPass = chunkFrame.passedCheckSum();
						tracePrinter.println("\t\t" + "sumCheck: " + sumCheckPass);

						// check if received chunk deserves an ack response
						tracePrinter.println("\t" + "check if received chunk deserves an ack response");

						seqDeservesAck = chunkFrame.getSequenceNumber() <= expectedSequenceNumber && sumCheckPass;
						tracePrinter.println("\t\t" + "seqDeservesAck: " + seqDeservesAck);

						// if it passed checksum and is a match to the expected
						// ack number
						if (seqDeservesAck)
						{
							// check to see if this is the first time we acked
							// this
							if (last < chunkFrame.getSequenceNumber())
							{
								tracePrinter.println("\t" + "First time we acked this");
								tracePrinter.println("\t\t" + "last:" + last);
								tracePrinter.println("\t\t" + "chunkFrame.getSequenceNumber():" + chunkFrame.getSequenceNumber());
								tracePrinter.println("\t\t" + "last < chunkFrame.getSequenceNumber():" + (last < chunkFrame.getSequenceNumber()));

								first = true;
								last = chunkFrame.getSequenceNumber();

								tracePrinter.println("\t\t" + "last set to:" + last);
								tracePrinter.println("\t\t" + "first set to:" + first);
							}
							else
							{

								tracePrinter.println("\t" + "Not the First time we acked this");
								tracePrinter.println("\t\t" + "last:" + last);
								tracePrinter.println("\t\t" + "chunkFrame.getSequenceNumber():" + chunkFrame.getSequenceNumber());
								tracePrinter.println("\t\t" + "last < chunkFrame.getSequenceNumber():" + (last < chunkFrame.getSequenceNumber()));

								first = false;

								tracePrinter.println("\t\t" + "first set to:" + first);
							}

							// make acknowledgement frame
							tracePrinter.println("\t" + "make acknowledgement frame");

							ackFrame = new AckFrame(chunkFrame, numberOfAckNumbers);

							// if the chunk part of the frame is empty, signal
							// for end of transmission
							if (chunkFrame.getLength() == ChunkFrame.HEADER_SIZE)
							{
								tracePrinter.println("\t" + "Case: EndFrame");

								end = true;

								// log the ack
								if (first)
								{

									log.sent(ackFrame, chunkFrame.getSequenceNumber());
									first = false;
								}
								else
								{
									log.resent(ackFrame, chunkFrame.getSequenceNumber());
								}

								// send the ack
								ackFrame.send(socket, destinationAddress, destinationPort);
							}
							else
							{

								// introduce errors
								if (introduceError)
								{
									// generate errors randomly using the
									// generator
									ackFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChance));
								}

								// simulate drops
								if (ackFrame.isDropped())
								{
									tracePrinter.println("\t" + "Case: Drop");

									// log the send
									if (first)
									{

										log.sent(ackFrame, chunkFrame.getSequenceNumber());
									}
									else
									{
										log.resent(ackFrame, chunkFrame.getSequenceNumber());
									}

									// we don't actually send it

								}

								// simulate sending corrupt package
								else if (ackFrame.failedCheckSum())
								{
									tracePrinter.println("\t" + "Case: corrupt");

									// log the sending
									if (first)
									{

										log.sent(ackFrame, chunkFrame.getSequenceNumber());
									}
									else
									{
										log.resent(ackFrame, chunkFrame.getSequenceNumber());
									}

									// send the ack
									ackFrame.send(socket, destinationAddress, destinationPort);
								}

								// normal case
								else
								{

									tracePrinter.println("\t" + "Case: Normal");

									// log the sending
									if (first)
									{

										log.sent(ackFrame, chunkFrame.getSequenceNumber());
										first = false;
									}
									else
									{
										log.resent(ackFrame, chunkFrame.getSequenceNumber());
									}

									// send the ack
									ackFrame.send(socket, destinationAddress, destinationPort);
								}

								// if the sequence number matches what we
								// expect...
								if (chunkFrame.getSequenceNumber() == expectedSequenceNumber)
								{
									// add the chunk to the chunk list
									tracePrinter.println("\t" + "add the chunk to the chunklist");
									chunk = chunkFrame.extractChunk();
									chunkList.add(chunk);

									// Increment expected sequence number
									tracePrinter.println("\t" + "increment the expected sequence number");
									expectedSequenceNumber++;
								}
							}
							// we are done with this one
							done = true;
						}

					}
					catch (SocketTimeoutException e)
					{
						// We don't care about timeouts on the receiver
						tracePrinter.println("\t" + "timeout");
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}

			// close socket
			tracePrinter.println("");
			tracePrinter.println("closing socket");
			socket.close();

			// assemble file from chunks
			tracePrinter.println("");
			tracePrinter.println("assembling file from received chunks");
			FileAssembler assembler = new FileAssembler(outFile);
			assembler.accept(chunkList);
			assembler.assembleFile();

			tracePrinter.println("");
			tracePrinter.println(outFile.getName() + " written.");

			tracePrinter.println("");
			tracePrinter.println("END");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the args from sender to make demonstrating the program easier
	 * 
	 * @param socket
	 *            The socket that we are using to receive the datagrams
	 */
	private static void getArgsFromSender(DatagramSocket socket)
	{
		int expecting = 0;

		// Determine max length of initialization datagrams
		int len = getLengthOfInitializingDatagrams(socket, expecting++, numberOfAckNumbers, destinationAddress, destinationPort);

		// receive all initialization datapackets
		inFile = (File) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		senderAddress = (InetAddress) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		senderPort = (Integer) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		receiverPort = (Integer) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		errorChance = (Integer) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		maxSizeOfChunk = (Short) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		numberOfAckNumbers = (Integer) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		introduceError = (Boolean) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		debugMode = (Boolean) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		logPrintingMode = (Boolean) receiveObjectForSetup(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);

		// set start time
		startTime = getStartTime(socket, expecting, numberOfAckNumbers, destinationAddress, destinationPort);

		// setUp outFile
		outFile = FileArg.getOutFile(inFile);

		// catch delayed packets
		int tmp = 0;
		try
		{
			tmp = socket.getSoTimeout();
			socket.setSoTimeout(1);
		}
		catch (SocketException e1)
		{
			e1.printStackTrace();
		}
		DatagramPacket p = new DatagramPacket(new byte[1], 1);
		while (new Date().getTime() < startTime)
		{
			try
			{
				socket.receive(p);
			}
			catch (Exception e)
			{
				// keep trying
			}
		}
		try
		{
			socket.setSoTimeout(tmp);
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Gets the length of the initilizing datagrams
	 * 
	 * @param socket
	 *            The socket that we are using to receive the datagrams
	 * @param expecting
	 *            The sequence number we are expecting
	 * @param numberOfAckNumbers
	 *            the number of ack numbers valid
	 * @param destinationAddress
	 *            the destination address for our ack packets
	 * @param destinationPort
	 *            the destination port for our ack packets
	 * @return int representation of the length of inizilizing datagrams
	 */
	private static int getLengthOfInitializingDatagrams(DatagramSocket socket, int expecting, int numberOfAckNumbers, InetAddress destinationAddress, int destinationPort)
	{
		ChunkFrame incomingFrame;
		int len = 0;
		boolean done = false;
		DatagramPacket initializationPacket = new DatagramPacket(new byte[8 + 4], 8 + 4);

		while (!done)
		{
			try
			{
				socket.receive(initializationPacket);
				incomingFrame = new ChunkFrame(initializationPacket);
				if (incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					len = byteNumberConverter.ByteIntConverter.convert(incomingFrame.extractChunk().getBytes());
					socket.send(new AckFrame(incomingFrame, numberOfAckNumbers).toDatagramPacket(destinationAddress, destinationPort));
					done = true;
				}
			}
			catch (SocketTimeoutException stoe)
			{
				// We don't care about timeouts on receiver side
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return len;
	}

	/**
	 * Gets the start time of in ms to keep output times synced
	 * 
	 * @param socket
	 *            The socket that we are using to receive the datagrams
	 * @param expecting
	 *            The sequence number we are expecting
	 * @param numberOfAckNumbers
	 *            the number of ack numbers valid
	 * @param destinationAddress
	 *            the destination address for our ack packets
	 * @param destinationPort
	 *            the destination port for our ack packets
	 * @return long representation of ms to keep output times synced
	 */
	private static long getStartTime(DatagramSocket socket, int expecting, int numberOfAckNumbers, InetAddress destinationAddress, int destinationPort)
	{
		long startTime = 0;
		ChunkFrame incomingFrame;
		int chunkSize = 8;
		int packetSize = chunkSize + ChunkFrame.HEADER_SIZE;
		DatagramPacket initializationPacket = new DatagramPacket(new byte[packetSize], packetSize);
		boolean done = false;
		while (!done)
		{
			try
			{
				socket.receive(initializationPacket);
				incomingFrame = new ChunkFrame(initializationPacket);
				if (incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					startTime = byteNumberConverter.ByteLongConverter.convert(incomingFrame.extractChunk().getBytes());
					socket.send(new AckFrame(incomingFrame, numberOfAckNumbers).toDatagramPacket(destinationAddress, destinationPort));
					done = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return startTime;
	}

	/**
	 * Receives and returns an object
	 * 
	 * @param socket
	 *            The socket that we are using to receive the datagrams
	 * @param expecting
	 *            The sequence number we are expecting
	 * @param lengthOfInitializationDatagrams
	 *            The largest length of the initialization datagrams
	 * @param numberOfAckNumbers
	 *            the number of ack numbers valid
	 * @param destinationAddress
	 *            the destination address for our ack packets
	 * @param destinationPort
	 *            the destination port for our ack packets
	 * @return Object that was received
	 */
	private static Object receiveObjectForSetup(DatagramSocket socket, int expecting, int lengthOfInitializationDatagrams, int numberOfAckNumbers, InetAddress destinationAddress, int destinationPort)
	{
		Object o = null;
		ByteArrayInputStream bais;
		ObjectInputStream ois;
		DatagramPacket initializationPacket = new DatagramPacket(new byte[lengthOfInitializationDatagrams], lengthOfInitializationDatagrams);
		ChunkFrame incomingFrame;
		boolean done = false;

		while (!done)
		{
			try
			{
				// receive datagram packet
				socket.receive(initializationPacket);

				// convert into a ChunkFrame
				incomingFrame = new ChunkFrame(initializationPacket);

				// if we receive a valid Chunkframe that is sequenced less than
				// what we are expecting we ack but don't do anything with data
				if (incomingFrame.getSequenceNumber() < expecting && incomingFrame.passedCheckSum())
				{
					AckFrame ackFrame = new AckFrame(incomingFrame, numberOfAckNumbers);
					socket.send(ackFrame.toDatagramPacket(destinationAddress, destinationPort));
				}
				// if we receive a valid ChunkFrame that is sequenced equal to
				// what we expect, we ack and return the object in the
				// ChunkFrame
				else if (incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					// ack
					socket.send(new AckFrame(incomingFrame, numberOfAckNumbers).toDatagramPacket(destinationAddress, destinationPort));

					// set the return value equal to the converted contents of
					// the chunkFrame
					byte[] bytes = incomingFrame.extractChunk().getBytes();
					bais = new ByteArrayInputStream(bytes);
					ois = new ObjectInputStream(bais);
					o = ois.readObject();
					done = true;
					ois.close();
					bais.close();

				}
			}
			catch (SocketTimeoutException stoe)
			{
				// we don't care about timeouts on reciever side... just try
				// again
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return o;
	}

}
