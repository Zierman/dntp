/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Frame
{
	public enum Error {DROP, DELAY, CORRUPT}
	private static final short BAD = 1;
	private static final short GOOD = 0;
	
	protected Error error = null;
	protected short checkSum = GOOD;
	protected short length;
	protected int ackNumber;
	
	protected Frame(int ackNumber)
	{
		this.ackNumber = ackNumber;
	}
	
	public Frame()
	{
		
	}
	
	public abstract DatagramPacket toDatagramPacket(InetAddress address, int port);

	public int getAckNumber()
	{
		return ackNumber;
	}
	
	public Error getError()
	{
		return error;
	}


	/**
	 * 
	 */
	public void corrupt()
	{
		error = Error.CORRUPT;
		checkSum = BAD;
	}

}
