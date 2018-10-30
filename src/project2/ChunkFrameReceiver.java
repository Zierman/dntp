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

import project1.Chunk;
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

	// Args
	private static FileArg fileArg = new FileArg("-f");
	private static ReceiverAddressArg receiverAddressArg = new ReceiverAddressArg("-ra");
	private static ReceiverPortArg receiverPortArg = new ReceiverPortArg("-rp");

	// Toggle Args
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameReceiver.RECEIVER_PROGRAM_TITLE, ChunkFrameReceiver.RECEIVER_PROGRAM_DESCRIPTION);

	// Destination vars
	private static InetAddress destinationAddress;
	private static int destinationPort;

	// Printer
	private static DebugPrinter debug;
	private static LogPrinter log;

	// Arguments to receive from sender
	private static File file;
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

	// Delayed Frames
	private static DelayedFrameCollection<AckFrame> delayedFrames;

	private static long startTime;


	public static void main(String[] args) throws Exception
	{
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);

		// set up the socket
		DatagramSocket socket = new DatagramSocket(receiverPortArg.getValue());

		// Get arg info from sender.
		getArgsFromSender(socket);
		
		// initialize the printers
		debug = new DebugPrinter(debugMode, System.out);
		log = new LogPrinter(logPrintingMode, System.out, maxSizeOfChunk + 8, file.length(), startTime);

		// set destination
		destinationAddress = senderAddress;
		destinationPort = senderPort;
		
		// update socket from default to specified values
		socket = new DatagramSocket(receiverPort);
		
		// set up the delayed frame collection
		delayedFrames = new DelayedFrameCollection<AckFrame>(socket, destinationAddress, destinationPort);

		// Determine Packet Size
		short chunkPacketSize = (short) (project2.Defaults.ACK_PACKET_LENGTH + 4 + maxSizeOfChunk);


		// set the expected ackNum to 0
		int expectedSequenceNumber = 0;

		// frame, package, and send all chunks using Stop and Wait
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
						// make acknowledgement frame
						ackFrame = new AckFrame(chunkFrame);
						
						// introduce errors
						if(introduceError)
						{
							// generate errors randomly using the generator
							ackFrame.setError(project2.frame.FrameErrorGenerator.generateError(errorChance));
						}
						
						// simulate delays
						if(ackFrame.isDelayed())
						{
							/*
							 * The delayedFrames collection is essentially a funnel that we put the frames we want to delay. 
							 * The collection contains a runnable sender that will automatically send the frames when the 
							 * delay elapses. 
							 */
							delayedFrames.add(ackFrame, delay());


							// log the sending
							log.sent(ackFrame, chunkFrame.getSequenceNumber());
						}
						
						// simulate drops
						else if(ackFrame.isDropped())
						{
							// we don't send
						}
						
						// simulate sending corrupt package
						else if(ackFrame.failedCheckSum())
						{						
							// send the corrupt package
							socket.send(chunkPacket);

							// log the sending
							log.sent(ackFrame, chunkFrame.getSequenceNumber());
						}
						
						// normal case
						else
						{
							// send the package
							socket.send(chunkPacket);

							// log the sending
							log.sent(ackFrame, chunkFrame.getSequenceNumber());
						}
						
						// if the chunk part of the frame is empty...
						if(chunkFrame.getLength() == AckFrame.LENGTH + 4) 
						{
							end = true;
						}
						
						// otherwise if the sequence number matches what we expect...
						else if(chunkFrame.getSequenceNumber() == expectedSequenceNumber)
						{
							// store the chunk to the chunk list
							chunk = chunkFrame.toChunk();
							chunkList.add(chunk);
						}

						// Increment expected sequence number TODO
						expectedSequenceNumber++;

						// we are done with this one
						done = true;
					}
					else if(sumCheckPass) // if it is valid but not the ack number we expect...
					{
						// check to see if it was already successfully received
						if(chunkFrame.getSequenceNumber() <= last.getSequenceNumber())
						{
							//if so send acknowledgement
							ackFrame = new AckFrame(chunkFrame);
							ackPacket = ackFrame.toDatagramPacket(destinationAddress, destinationPort);
							socket.send(ackPacket);
						}
					}
				}
				catch (SocketTimeoutException e)
				{
					// We don't care about timeouts on the receiver
				}
				catch (Exception e)
				{
					e.printStackTrace(); // TODO adjust this if desired
				}
			}
		}
		socket.close();
	}

	private static int delay()
	{
		return RANDOM.nextInt(maxDelay);
	}

	private static void getArgsFromSender(DatagramSocket socket)
	{
		boolean initilizationDone = false;
		Queue<Serializable> receivableArg = new LinkedList<Serializable>();
		receivableArg.add(file);
		receivableArg.add(senderAddress);
		receivableArg.add(receiverAddress);
		receivableArg.add(senderPort);
		receivableArg.add(receiverPort);
		receivableArg.add(errorChance);
		receivableArg.add(maxSizeOfChunk);
		receivableArg.add(numberOfAckNumbers);
		receivableArg.add(introduceError);
		receivableArg.add(debugMode);
		receivableArg.add(logPrintingMode);
		receivableArg.add(maxDelay);
		ByteArrayInputStream bais;
		ObjectInputStream ois;
		DatagramPacket initializationPacket = new DatagramPacket(new byte[4], 4);
		ChunkFrame incomingFrame;
		int len = 0;
		int expecting = 0;
		boolean done;
		
		// Determine max length of initialization datagrams
		done = false;
		while (!done)
		{
			try
			{
				socket.receive(initializationPacket);
				len = byteNumberConverter.ByteIntConverter.convert(initializationPacket.getData());
				done = true;
				expecting++;
			}
			catch (Exception e)
			{
				// try again
			}
		}

		// receive all initialization datapackets
		initializationPacket = new DatagramPacket(new byte[len], len);
		for (Object o : receivableArg)
		{
			done = false;
			while (!done)
			{
				try
				{
					socket.receive(initializationPacket);
					incomingFrame = new ChunkFrame(initializationPacket);
					if(incomingFrame.getAckNumber() < expecting)
					{
						socket.send(new AckFrame(incomingFrame).toDatagramPacket(destinationAddress, destinationPort));
					}
					else if(incomingFrame.getAckNumber() == expecting && incomingFrame.passedCheckSum())
					{
						socket.send(new AckFrame(incomingFrame).toDatagramPacket(destinationAddress, destinationPort));
						expecting++;
						byte[] bytes = incomingFrame.toChunk().getBytes();
						bais = new ByteArrayInputStream(bytes);
						ois = new ObjectInputStream(bais);
						if(o instanceof Integer)
						{
							o = (Integer)ois.readObject();
						}
						else if(o instanceof File)
						{
							o = (File)ois.readObject();
						}
						else if(o instanceof InetAddress)
						{
							o = (InetAddress)ois.readObject();
						}
						else if(o instanceof Boolean)
						{
							o = (Boolean)ois.readObject();
						}
						else if(o instanceof Short)
						{
							o = (Short)ois.readObject();
						}
						else
						{
							o = ois.readObject();
						}
					}
				}
				catch (Exception e)
				{
					// try again
				}
			}
		}
		
		// set start time
		initializationPacket = new DatagramPacket(new byte[8], 8);
		done = false;
		while (!done)
		{
			try
			{
				socket.receive(initializationPacket);
				startTime = byteNumberConverter.ByteLongConverter.convert(initializationPacket.getData());
				done = true;
				expecting++;
			}
			catch (Exception e)
			{
				// try again
			}
		}

	}
}
