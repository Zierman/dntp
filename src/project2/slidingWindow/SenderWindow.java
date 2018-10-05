/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.slidingWindow;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

import project1.Chunk;
import project2.Defaluts;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class SenderWindow extends SlidingWindow
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
	
	
	public SenderWindow(Queue<Chunk> chunkList, int windowSize, int packetSize, DatagramSocket socket, int timeout, InetAddress address, int port)
	{
		this.in = new Frame[windowSize];
		this.out = new Frame[windowSize];
		this.timeout = timeout;
		this.timedOut = new boolean[windowSize];
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
			while(running)
			{
				if(!allAcsReceived())//TODO check this
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
				else
				{
					
				}
			}
		}

		/**
		 * @return
		 */
		private boolean allAcsReceived() //TODO program logic to check for end of transimission ack
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
	/* (non-Javadoc)
	 * @see project2.slidingWindow.SlidingWindow#runReceiver()
	 */
	@Override
	protected void runReceiver()
	{
		Receiver receiver = new Receiver();
		receiver.run();
		
	}
	/* (non-Javadoc)
	 * @see project2.slidingWindow.SlidingWindow#runSender()
	 */
	@Override
	protected void runSender()
	{
		// TODO Auto-generated method stub
		
	}
}
