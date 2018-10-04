/**
 * File Created by Joshua Zierman on Oct 3, 2018
 */
package project2;

import java.net.InetAddress;

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

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Sender
{
	static FileArg fileArg = new FileArg("-f");	 
	static SenderAddressArg senderAddress = new SenderAddressArg("-sa");
	static ProxyAddressArg errorProxyAddress = new ProxyAddressArg("-pa"); 
	static ReceiverAddressArg receiverAddress = new ReceiverAddressArg("-ra"); 
	static SenderPortArg senderPort = new SenderPortArg("-sp");
	static ProxyPortArg errorProxyPort = new ProxyPortArg("-pp");
	static ReceiverPortArg receiverPort = new ReceiverPortArg("-rp"); 
	static WindowSizeArg windowSizeArg = new WindowSizeArg("-w");
	static TimeoutArg timeoutArg = new TimeoutArg("-t");
	static MaxSizeOfChunkArg maxSizeOfChunk = new MaxSizeOfChunkArg("-s");
	static HelpArg helpArg = new HelpArg("-help");
	
	public static void main(String[] args) throws Exception
	{
		ArgList.updateFromMainArgs(args);
	}
}
