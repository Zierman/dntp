/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

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
public class ChunkFrameSender
{
	private final static String SENDER_PROGRAM_TITLE = "Chunk Frame Sender Program";
	private final static String SENDER_PROGRAM_DESCRIPTION = "This program sends a file using our version of the Stop and Wait algorithm."; 

	// Args
	private static FileArg fileArg = new FileArg("-f");
	private static SenderAddressArg senderAddressArg = new SenderAddressArg("-sa");
	private static ProxyAddressArg errorProxyAddressArg = new ProxyAddressArg("-pa");
	private static ReceiverAddressArg receiverAddressArg = new ReceiverAddressArg("-ra");
	private static SenderPortArg senderPortArg = new SenderPortArg("-sp");
	private static ProxyPortArg errorProxyPortArg = new ProxyPortArg("-pp");
	private static ReceiverPortArg receiverPortArg = new ReceiverPortArg("-rp");
//	private static WindowSizeArg windowSizeArg = new WindowSizeArg("-w"); // Disabled due to change in project requirements
	private static TimeoutArg timeoutArg = new TimeoutArg("-t");
	private static ErrorChanceArg errorChanceArg = new ErrorChanceArg("-d");
	private static MaxSizeOfChunkArg maxSizeOfChunkArg = new MaxSizeOfChunkArg("-s");
	private static NumberOfAckNumbersArg numberOfAckNumbersArg = new NumberOfAckNumbersArg("-acknums");
	private static IntroduceErrorArg introduceErrorArg = new IntroduceErrorArg("-e");

	// Toggle Args
	private static DebugModeArg debugModeArg = new DebugModeArg("-debug");
	private static HelpArg helpArg = new HelpArg("-help", ChunkFrameSender.SENDER_PROGRAM_TITLE, ChunkFrameSender.SENDER_PROGRAM_DESCRIPTION);

	// Destination vars
	private static InetAddress destinationAddress;
	private static int destinationPort;

	// Printer
	private static final DebugPrinter debug = new DebugPrinter(debugModeArg, System.out);

	public static void main(String[] args) throws Exception
	{
		// handle the command line arguments
		ArgList.updateFromMainArgs(args);

		// Determine Packet Size
		short packetSize = (short) (project2.Defaults.ACK_PACKET_LENGTH + 4 + maxSizeOfChunkArg.getValue());

		// set destination
		if (introduceErrorArg.getValue())
		{
			destinationAddress = errorProxyAddressArg.getValue();
			destinationPort = errorProxyPortArg.getValue();
		}
		else
		{

			destinationAddress = receiverAddressArg.getValue();
			destinationPort = receiverPortArg.getValue();
		}

		// gets the input file
		File inFile = fileArg.getValue();

		// split up the file into chunks
		LinkedList<Chunk> chunkList = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter(inFile.getAbsolutePath(), maxSizeOfChunkArg.getValue());
		splitter.overwrite(chunkList);

		// set up the socket
		DatagramSocket socket = new DatagramSocket(destinationPort);
		socket.setSoTimeout(timeoutArg.getValue());

		// set the seqNum and ackNum to 0
		int seqNum = 0;
		int ackNum = 0;

		// frame, package, and send all chunks using Stop and Wait
		AckFrame ackFrame;
		DatagramPacket ackPacket = new DatagramPacket(new byte[AckFrame.LENGTH], AckFrame.LENGTH);
		Chunk chunk;
		ChunkFrame chunkFrame;
		DatagramPacket chunkPacket;

		while (!chunkList.isEmpty())
		{
			boolean done = false;
			boolean ackMatch = false;
			boolean sumCheckPass = false;
			
			// get the next chunk
			chunk = chunkList.remove();
			
			// frame the chunk
			chunkFrame = new ChunkFrame(chunk, seqNum, ackNum);
			
			// package the frame
			chunkPacket = chunkFrame.toDatagramPacket(destinationAddress, destinationPort);
			do
			{
				try {
				// send the package
				socket.send(chunkPacket);
				
				// receive a package
				socket.receive(ackPacket);
				
				// extract ack frame from package
				ackFrame = new AckFrame(ackPacket);
				
				// check if received ack number matches expected ack number
				ackMatch = ackFrame.getAckNumber() == ackNum;
				
				// check if the ackFrame passed the check sum
				sumCheckPass = ackFrame.passedCheckSum();
				
				// Determines if we are done trying to resend the packet
				done = ackMatch && sumCheckPass;
				}catch (SocketTimeoutException e) {
					//TODO write error output for Timeout
				}catch (Exception e)
				{
					e.printStackTrace(); //TODO adjust this if desired
				}
			}
			while (!done);
			ackNum++;
			ackNum %= numberOfAckNumbersArg.getValue();
			seqNum++;
		}
		socket.close();
	}
}
