/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import project1.Chunk;
import project1.FileSplitter;
import project2.args.Arg;
import project2.args.ArgList;
import project2.args.FileArg;
import project2.args.HelpArg;
import project2.args.MaxSizeOfChunkArg;
import project2.args.ProxyAddressArg;
import project2.args.ProxyPortArg;
import project2.args.ReceiverAddressArg;
import project2.args.ReceiverPortArg;
import project2.args.SenderAddressArg;
import project2.args.SenderPortArg;
import project2.args.TimeoutArg;
import project2.args.WindowSizeArg;
import project2.frame.ChunkFrame;
import project2.slidingWindow.SenderWindow;
import project2.slidingWindow.SlidingWindow;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Sender
{
	private final static String SENDER_PROGRAM_DESCRIPTION = "<description not done>"; // TODO write description of the sender program
	static FileArg fileArg = new FileArg("-f");	 
	static SenderAddressArg senderAddressArg = new SenderAddressArg("-sa");
	static ProxyAddressArg errorProxyAddressArg = new ProxyAddressArg("-pa"); 
	static ReceiverAddressArg receiverAddressArg = new ReceiverAddressArg("-ra"); 
	static SenderPortArg senderPortArg = new SenderPortArg("-sp");
	static ProxyPortArg errorProxyPortArg = new ProxyPortArg("-pp");
	static ReceiverPortArg receiverPortArg = new ReceiverPortArg("-rp"); 
	static WindowSizeArg windowSizeArg = new WindowSizeArg("-w");
	static TimeoutArg timeoutArg = new TimeoutArg("-t");
	static MaxSizeOfChunkArg maxSizeOfChunkArg = new MaxSizeOfChunkArg("-s");
	static HelpArg helpArg = new HelpArg("-help", SENDER_PROGRAM_DESCRIPTION);
	
	public static void main(String[] args) throws Exception
	{
		// handle the inline arguments
		ArgList.updateFromMainArgs(args);
		
		// gets the input file
		File inFile = fileArg.getValue();
		
		
		// split up the file into chunks
		LinkedList<Chunk> chunkList = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter(inFile.getAbsolutePath(), maxSizeOfChunkArg.getValue());
		splitter.overwrite(chunkList);
		
		// convert chunks into frames
		ChunkFrame[] window = new ChunkFrame[windowSizeArg.getValue()];
		int seqNum = 0;
		int ackNum = 0;
		while(!chunkList.isEmpty())
		{
			SenderWindow window = new SenderWindow(windowSize, , socket, timeoutArg.getValue(), errorProxyAddressArg.getValue(), errorProxyPortArg.getValue())
			window.run();
		}
		
		
	}
}
