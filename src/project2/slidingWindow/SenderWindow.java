/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.slidingWindow;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;

import project2.Defaluts;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class SenderWindow
{
	private project2.frame.Frame[] in;
	private project2.frame.Frame[] out;
	private boolean[] timedOut;
	private boolean[] ackReceived;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private int timeout;
	private Frame tmp;
	private InetAddress address;
	private int port;
	
	
	public SenderWindow(int windowSize, int packetSize, DatagramSocket socket, int timeout, InetAddress address, int port)
	{
		this.in = new Frame[windowSize];
		this.out = new Frame[windowSize];
		this.timeout = timeout;
		this.timedOut = new Boolean[windowSize];
		this.socket = socket;
		this.packet = new DatagramPacket(new byte[packetSize],packetSize);
		this.address = address;
		this.port = port;
		this.ackReceived = new boolean[windowSize];
		
		
		
	}
	private class Receiver implements Runnable
	{

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			while(!ackReceivedAllTrue() && not)
			{
				try
				{
					socket.receive(packet);
					tmp = new AckFrame(packet);
					ackReceived[tmp.getAckNumber()] = true;
					
				} catch (IOException e)
				{
					System.err.println(e.getMessage());
				}
			}
		}

		/**
		 * @return
		 */
		private boolean ackReceivedAllTrue()
		{
			boolean allTrue = true;
			for(Boolean b : ackReceived)
			{
				if(!b)
				{
					allTrue = false;
					break;
				}
			}
			
			return allTrue;
		}
		
	}
	private class Sender implements Runnable
	{

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			//TODO
		}
		
	}
}
