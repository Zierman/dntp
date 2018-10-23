/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import project2.frame.AckFrame;
import project2.frame.AckFrame.AckFrameLengthMismatchException;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class OldErrorProxy
{
	static final Random RAND = new Random();

	/**
	 * @param args
	 * @throws SocketException
	 * @throws AckFrameLengthMismatchException 
	 */
	public static void main(String[] args) throws SocketException, AckFrameLengthMismatchException
	{
		int destinationPort = 0;
		boolean delayedThisTime = false;
		Queue<DatagramPacket> delayedPackages = new LinkedList<DatagramPacket>();
		Frame frame;
		DatagramSocket socket = new DatagramSocket(Defaults.PROXY_PORT);
		DatagramPacket p = new DatagramPacket(new byte[0], 0);//TODO fix this
		while (true)
		{
			try
			{
				// if the number of delayed packets !=
				// winowSize - (number of sent + number of
				// dropped);
				if (true)// TODO fix that
				{
					socket.receive(p); // this will block
										// might need an in
										// if statment

					// determin if the 'from port' matches
					// the receiver or sender to determin
					// the frame type
					if (p.getPort() == Defaults.RECEIVER_PORT)
					{
						frame = new AckFrame(p);
						destinationPort = Defaults.SENDER_PORT;
					} else if (p.getPort() == Defaults.SENDER_PORT)
					{
						frame = new ChunkFrame(p);
						destinationPort = Defaults.RECEIVER_PORT;
					} else
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
						send(frame, p.getAddress(), destinationPort, socket);
						break;
					case DELAY:
						// frame.delay();
						delayedThisTime = true;
						delayedPackages.add(frame.toDatagramPacket(p.getAddress(), destinationPort));
						break;

					default:
						send(frame, p.getAddress(), destinationPort, socket);
						break;
					}

				}
				if ((!delayedThisTime || delayedPackages.size() > 1) && RAND.nextBoolean()) // TODO Max Delay
				{
					socket.send(delayedPackages.remove());
				}
				delayedThisTime = false;
			} catch (IOException e)
			{
				// Drop the packet
			}
		}
	}

	/**
	 * @param frame
	 * @param destinationPort
	 * @throws IOException
	 */
	private static void send(Frame frame, InetAddress address, int destinationPort, DatagramSocket socket)
			throws IOException
	{
		socket.send(frame.toDatagramPacket(address, destinationPort));

	}

	private static Frame.Error generateError()
	{
		Frame.Error error = null;
		if (RAND.nextFloat() < Defaults.CHANCE_OF_ERROR / 100)
		{
			switch (RAND.nextInt(3))
			{
			case 0:
				error = Frame.Error.DROP;
				break;
			case 1:
				error = Frame.Error.DELAY;
				break;
			case 2:
				error = Frame.Error.CORRUPT;
				break;

			default:
				break;
			}
		}
		return error;

	}
}
