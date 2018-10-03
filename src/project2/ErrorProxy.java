/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ErrorProxy
{
	static final Random RAND = new Random();
	/**
	 * @param args
	 * @throws SocketException
	 */
	public static void main(String[] args) throws SocketException
	{
		int destinationPort = 0;
		boolean delayed = false;
		LinkedList<DatagramPacket> delayedPackages = new LinkedList<DatagramPacket>();
		Frame frame;
		DatagramSocket socket = new DatagramSocket(Project2.PROXY_PORT);
		DatagramPacket p = new DatagramPacket(new byte[Project2.MAX_PACKET_LENGTH], 0);
		while(true)
		{
			try
			{
				socket.receive(p);
				
				// determin if the 'from port' matches the receiver or sender to determin the frame type
				if(p.getPort() == Project2.RECEIVER_PORT)
				{
					frame = new AckFrame(p);
					destinationPort = Project2.SENDER_PORT;
				}
				else if (p.getPort() == Project2.SENDER_PORT)
				{
					frame = new ChunkFrame(p);
					destinationPort = Project2.RECEIVER_PORT;
				}
				else
				{
					throw new IOException();
				}
				switch (generateError())
				{
				case DROP:
					// do nothing... the packet is gone
					break;
				case CORRUPT:
					frame.corrupt();
					break;
				case DELAY:
					delayed = true;
					delayedPackages.add(frame.toDatagramPacket(p.getAddress(), destinationPort));
					break;

				default:
					//send as normal
					break;
				}
				
				if((!delayed || delayedPackages.size() > 1) && RAND.nextBoolean() && !delayedPackages.isEmpty() )
				{
					socket.send(delayedPackages.remove());
				}
				delayed = false;
			} catch (IOException e)
			{
				//Drop the packet
			}
		}
	}
	
	private static Frame.Error generateError()
	{
		return null;//TODO
		
	}
}
