/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;

import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public  class Window
{
	private project2.frame.Frame[] in;
	private project2.frame.Frame[] out;
	private Boolean[] timedOut;
	private Boolean[] ackReceived;
	private DatagramSocket socket;
	private DatagramPacket packet;
	private int timeout;
	private Frame tmp;
	private InetAddress address;
	private int port;
	
	
	public Window(int windowSize, int packetSize, DatagramSocket socket, int timeout, InetAddress address, int port)
	{
		this.in = new Frame[windowSize];
		this.out = new Frame[windowSize];
		this.timeout = timeout;
		this.timedOut = new Boolean[windowSize];
		this.socket = socket;
		this.packet = new DatagramPacket(new byte[packetSize],packetSize);
		this.address = address;
		this.port = port;
		
		
	}
	private class listener implements Runnable
	{

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			while(true)//TODO make condition
			{
				try
				{
					socket.receive(packet);
					if(packet.getLength() == project2.Defaluts.ACK_PACKET_LENGTH)
					{
						tmp = new AckFrame(packet);
						ackReceived[tmp.getAckNumber()] = true;
					}
					else
					{
						tmp = new ChunkFrame(packet);
						if(tmp.passedCheckSum())
						{
							in[tmp.getAckNumber()] = tmp;
							socket.send(new AckFrame((ChunkFrame) tmp).toDatagramPacket(address, port));
						}
					}
					
				} catch (IOException e)
				{
					System.err.println(e.getMessage());
				}
			}
		}
		
	}
	private class sender implements Runnable
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
