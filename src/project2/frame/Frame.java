/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

	/**Gets the acknowledgement number
	 * @return the int acknowledgement number
	 */
	public int getAckNumber()
	{
		return ackNumber;
	}
	
	/** gets the type of error 
	 * @return an Error value assosiated with the type of error, DROP if the frame was dropped, DELAY if delayed, CORRUPT if corrupted, null if no error
	 */
	public Error getError()
	{
		return error;
	}

	/** Checks to see if the CheckSum passes
	 * @return true if it passes the checksum, false if corrupted
	 */
	public boolean passedCheckSum()
	{
		return checkSum == GOOD;
	}
	
	public void delay()
	{
		error = Error.DELAY;
	}
	
	public void drop()
	{
		error = Error.DROP;
	}
	
	/**corrupts the Frame
	 * 
	 */
	public void corrupt()
	{
		error = Error.CORRUPT;
		checkSum = BAD;
	}
	
	public void send(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		socket.send(this.toDatagramPacket(address, port));
	}

//	public static final int DATA_PACKET_LENGTH = MAX_CHUNK_LENGTH + 12;
}
