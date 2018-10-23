/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.text.Position.Bias;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import project1.Chunk;
import project1.FileSplitter;
import project2.args.*;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;
import project2.slidingWindow.SenderWindow;
import project2.slidingWindow.SlidingWindow;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrameReceiver
{
	private final static String RECEIVER_PROGRAM_TITLE = "Chunk Frame Sender Program";
	private final static String RECEIVER_PROGRAM_DESCRIPTION = "This program sends a file using our version of the Stop and Wait algorithm.";

	// Args
	private static FileArg fileArg = new FileArg("-f");
	private static ReceiverAddressArg receiverAddressArg = new ReceiverAddressArg("-ra");
	private static ReceiverPortArg receiverPortArg = new ReceiverPortArg("-rp");

	// Toggle Args
	private static DebugModeArg debugModeArg = new DebugModeArg("-debug");
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameReceiver.RECEIVER_PROGRAM_TITLE, ChunkFrameReceiver.RECEIVER_PROGRAM_DESCRIPTION);

	// Destination vars
	private static InetAddress destinationAddress;
	private static int destinationPort;

	// Printer
	private static final DebugPrinter debug = new DebugPrinter(debugModeArg, System.out);

	// Arguments to receive from sender
	private static File file;
	private static InetAddress senderAddress;
	private static InetAddress receiverAddress;
	private static Integer senderPort;
	private static Integer receiverPort;
	private static Integer timeout;
	private static Integer errorChance;
	private static Integer maxSizeOfChunk;
	private static Integer numberOfAckNumbers;
	private static Boolean introduceError;
	private static Boolean debugMode;
	private static Boolean requiredLogArg;

	public static void main(String[] args) throws Exception
	{
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);

		// set up the socket
		DatagramSocket socket = new DatagramSocket(receiverPortArg.getValue());
		socket.setSoTimeout(Defaults.TIMEOUT);

		// Get arg info from sender.
		getArgsFromSender(socket);

		// Determine Packet Size
		short packetSize = (short) (project2.Defaults.ACK_PACKET_LENGTH + 4 + maxSizeOfChunk);

		// set destination
		destinationAddress = senderAddress;
		destinationPort = senderPort;

		// set the expected ackNum to 0
		int expectedAck = 0;

		// frame, package, and send all chunks using Stop and Wait
		AckFrame ackFrame;
		DatagramPacket ackPacket;
		Chunk chunk;
		ChunkFrame chunkFrame;
		ChunkFrame last = null;
		DatagramPacket chunkPacket = new DatagramPacket(new byte[maxSizeOfChunk + AckFrame.LENGTH + 4], maxSizeOfChunk + AckFrame.LENGTH + 4);
		Queue<Chunk> chunkList = new LinkedList<Chunk>();
		
		boolean end = false;
		while (!end)
		{
			boolean done = false;
			boolean ackMatch = false;
			boolean sumCheckPass = false;

			while (!done)
			{
				try
				{
					// receive chunk packet
					socket.receive(chunkPacket);
					
					// extract the chunk frame
					chunkFrame = new ChunkFrame(chunkPacket);

					// check if received ack number matches expected ack number
					ackMatch = chunkFrame.getAckNumber() == expectedAck;
					
					// check if the ackFrame passed the check sum
					sumCheckPass = chunkFrame.passedCheckSum();
					
					// if it passed checksum and is a match to the expected ack number
					if(ackMatch && sumCheckPass)
					{
						// send acknowledgement
						ackFrame = new AckFrame(chunkFrame);
						ackPacket = ackFrame.toDatagramPacket(destinationAddress, destinationPort);
						socket.send(ackPacket);
						
						// set last to the chunk frame
						last = chunkFrame;
						
						// Increment expected ack number
						expectedAck++;
						expectedAck %= numberOfAckNumbers;
						
						// if the chunk part of the frame is empty we end, if not we add the chunk to the list
						if(chunkFrame.getLength() == AckFrame.LENGTH + 4) // ChunkFrame has no data
						{
							end = true;
						}
						else
						{
							chunk = chunkFrame.toChunk();
							chunkList.add(chunk);
						}
						

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
					// TODO write error output for Timeout
				}
				catch (Exception e)
				{
					e.printStackTrace(); // TODO adjust this if desired
				}
			}
		}
		socket.close();
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
		receivableArg.add(timeout);
		receivableArg.add(errorChance);
		receivableArg.add(maxSizeOfChunk);
		receivableArg.add(numberOfAckNumbers);
		receivableArg.add(introduceError);
		receivableArg.add(debugMode);
		receivableArg.add(requiredLogArg);
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
				len = byteIntConverter.ByteIntConverter.convert(initializationPacket.getData());
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

	}
}
