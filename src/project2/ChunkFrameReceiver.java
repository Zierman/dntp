/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import oracle.jrockit.jfr.parser.ChunkParser;
import project1.Chunk;
import project1.FileAssembler;
import project2.args.*;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrameReceiver
{
	private static final Random RANDOM = new Random();
	
	// documentation constants
	private final static String RECEIVER_PROGRAM_TITLE = "Chunk Frame Sender Program";
	private final static String RECEIVER_PROGRAM_DESCRIPTION = "This program sends a file using our version of the Stop and Wait algorithm.";

	// Toggle Args
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameReceiver.RECEIVER_PROGRAM_TITLE, ChunkFrameReceiver.RECEIVER_PROGRAM_DESCRIPTION);

	// Destination vars
	private static InetAddress destinationAddress = Defaults.SENDER_ADDRESS;
	private static int destinationPort = Defaults.SENDER_PORT;

	// Printer
	private static DebugPrinter debug = new DebugPrinter(false, null);
	private static LogPrinter log = new LogPrinter(false, null, null, null, null);

	// Arguments to receive from sender
	private static File inFile = Defaults.INPUT_FILE;;
	private static InetAddress senderAddress = Defaults.SENDER_ADDRESS;
	private static InetAddress receiverAddress = Defaults.RECEIVER_ADDRESS;
	private static Integer senderPort = Defaults.SENDER_PORT;
	private static Integer receiverPort = Defaults.RECEIVER_PORT;
	private static Integer errorChance = Defaults.CHANCE_OF_ERROR;
	private static Short maxSizeOfChunk = Defaults.MAX_CHUNK_LENGTH;
	private static Integer numberOfAckNumbers = Defaults.NUMBER_OF_ACK_NUMBERS;
	private static Boolean introduceError = false;
	private static Boolean debugMode = false;
	private static Boolean logPrintingMode = false;
	private static Integer maxDelay = Defaults.MAX_DELAY;
	private static long startTime;
	private static File outFile;


	public static void main(String[] args) throws Exception
	{
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);

		
		// set up the socket
		int port = Defaults.RECEIVER_PORT;
		DatagramSocket socket = new DatagramSocket(port);
		
		// Get arg info from sender.
		getArgsFromSender(socket);
		
		// initialize the printers
		debug = new DebugPrinter(debugMode, System.out);
		log = new LogPrinter(logPrintingMode, System.out, (int) maxSizeOfChunk, inFile.length(), startTime);
		
		// set destination
		debug.println("");
		debug.println("setting destination");
		destinationAddress = senderAddress;
		destinationPort = senderPort;
		
		// update socket from default to specified values
		debug.println("");
		debug.println("updating socket");
		socket.close();
		socket = new DatagramSocket(receiverPort);
		
		
		// Determine Packet Size

		debug.println("");
		debug.println("determining max packet size");
		short chunkPacketSize = (short) (project2.Defaults.ACK_PACKET_LENGTH + 4 + maxSizeOfChunk);


		// set the expected sequence number to 0
		debug.println("");
		debug.println("setting expected sequence number to zero");
		int expectedSequenceNumber = 0;

		// receive all chunks using Stop and Wait
		debug.println("");
		debug.println("Receiving all chunks with stop and wait");
		AckFrame ackFrame;
		DatagramPacket ackPacket;
		Chunk chunk;
		ChunkFrame chunkFrame;
		ChunkFrame last = null;
		DatagramPacket chunkPacket = new DatagramPacket(new byte[chunkPacketSize], chunkPacketSize);
		Queue<Chunk> chunkList = new LinkedList<Chunk>();
		
		
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
					// receive chunk packet
					socket.receive(chunkPacket);
					
					// extract the chunk frame
					chunkFrame = new ChunkFrame(chunkPacket);
					
					// Log received packet info
					log.chunkReceived(chunkFrame, expectedSequenceNumber);

					
					// check if the ackFrame passed the check sum
					sumCheckPass = chunkFrame.passedCheckSum();
					
					// check if received chunk deserves an ack response
					seqDeservesAck = chunkFrame.getSequenceNumber() <= expectedSequenceNumber && sumCheckPass;
					
					// if it passed checksum and is a match to the expected ack number
					if(seqDeservesAck)
					{
						
						// make acknowledgement frame and packet
						ackFrame = new AckFrame(chunkFrame, numberOfAckNumbers);
						

						// if the chunk part of the frame is empty, signal for end of transmission
						if(chunkFrame.getLength() == ChunkFrame.HEADER_SIZE) 
						{
							end = true;
							ackPacket = ackFrame.toDatagramPacket(destinationAddress, destinationPort);
							socket.send(ackPacket);
						}
						else
						{
						
							// introduce errors
							if(introduceError)
							{
								// generate errors randomly using the generator
								ackFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChance));
							}
	
							ackPacket = ackFrame.toDatagramPacket(destinationAddress, destinationPort);
							
							// simulate drops
							if(ackFrame.isDropped())
							{
								// we don't actually send it
								
								// log the send
								log.sent(ackFrame, chunkFrame.getSequenceNumber(), expectedSequenceNumber);
							}
							
							// simulate sending corrupt package
							else if(ackFrame.failedCheckSum())
							{			
								// send the corrupt package
								socket.send(ackPacket);
	
								// log the sending
								log.sent(ackFrame, chunkFrame.getSequenceNumber(), expectedSequenceNumber);
							}
							
							// normal case
							else
							{
								// send the package
								socket.send(ackPacket);
	
								// log the sending
								log.sent(ackFrame, chunkFrame.getSequenceNumber(), expectedSequenceNumber);
							}
							
							
							// if the sequence number matches what we expect...
							if(chunkFrame.getSequenceNumber() == expectedSequenceNumber)
							{
								// store the chunk to the chunk list
								chunk = chunkFrame.toChunk();
								chunkList.add(chunk);
	
								// Increment expected sequence number
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
				}
				catch (Exception e)
				{
					e.printStackTrace(); // adjust this if desired
				}
			}
		}
		
		// close socket
		debug.println("");
		debug.println("closeing socket");
		socket.close();
		
		// assemble file from chunks
		debug.println("");
		debug.println("assembling file from recieved chunks");
		FileAssembler assembler = new FileAssembler(outFile);
		assembler.accept(chunkList);
		assembler.assembleFile();
		

		debug.println("");
		debug.println(outFile.getName() + " written.");
		
		debug.println("");
		debug.println("END");
	}

	private static int delay()
	{
		return RANDOM.nextInt(maxDelay);
	}

	private static void getArgsFromSender(DatagramSocket socket)
	{
		int expecting = 0;
		
		// Determine max length of initialization datagrams
		int len = getLengthOfInitializingDatagrams(socket, expecting++, numberOfAckNumbers, destinationAddress, destinationPort);

		// receive all initialization datapackets
		inFile = (File) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		senderAddress = (InetAddress) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		receiverAddress = (InetAddress) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		senderPort = (Integer) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		receiverPort = (Integer) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		errorChance = (Integer) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		maxSizeOfChunk = (Short) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		numberOfAckNumbers = (Integer) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		introduceError = (Boolean) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		debugMode = (Boolean) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		logPrintingMode = (Boolean) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		maxDelay = (Integer) receiveObject(socket, expecting++, len, numberOfAckNumbers, destinationAddress, destinationPort);
		
		// set start time
		startTime = getStartTime(socket, expecting, numberOfAckNumbers, destinationAddress, destinationPort);
		System.out.println("StartTime: " + startTime);
		// setUp outFile
		outFile = FileArg.getOutFile(inFile);
		
	}
	
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
				if(incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					startTime = byteNumberConverter.ByteLongConverter.convert(incomingFrame.toChunk().getBytes());
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
	
	private static int getLengthOfInitializingDatagrams(DatagramSocket socket, int expecting, int numberOfAckNumbers, InetAddress destinationAddress, int destinationPort)
	{
		ChunkFrame incomingFrame;
		int len = 0;
		boolean done = false;
		DatagramPacket initializationPacket = new DatagramPacket(new byte[8+4], 8+4);
		
		while (!done)
		{
			try
			{
				socket.receive(initializationPacket);
				incomingFrame = new ChunkFrame(initializationPacket);
				if(incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					len = byteNumberConverter.ByteIntConverter.convert(incomingFrame.toChunk().getBytes());
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
	
	private static Object receiveObject(DatagramSocket socket, int expecting, int lengthOfInitializationDatagrams, int numberOfAckNumbers, InetAddress destinationAddress, int destinationPort)
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
				socket.receive(initializationPacket);
				incomingFrame = new ChunkFrame(initializationPacket);
				if(incomingFrame.getSequenceNumber() < expecting)
				{
					AckFrame ackFrame = new AckFrame(incomingFrame, numberOfAckNumbers);
					socket.send(ackFrame.toDatagramPacket(destinationAddress, destinationPort));
				}
				else if(incomingFrame.getSequenceNumber() == expecting && incomingFrame.passedCheckSum())
				{
					socket.send(new AckFrame(incomingFrame, numberOfAckNumbers).toDatagramPacket(destinationAddress, destinationPort));
					byte[] bytes = incomingFrame.toChunk().getBytes();
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
				// we don't care about timeouts on reciever side
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return o;
	}
	
}
