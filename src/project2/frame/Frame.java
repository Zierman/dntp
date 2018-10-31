/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import byteNumberConverter.ByteShortConverter;
import project2.frame.Frame.Error;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Frame
{
	
	public static class CheckSumFailException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CheckSumFailException()
		{
			super("The check sum failed");
		}
	}
	public enum Error {DROP, CORRUPT}
	private static final short BAD = 1;
	private static final short GOOD = 0;
	
	protected Error error = null;
	
	// From specs
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
	
	public boolean failedCheckSum()
	{
		return !passedCheckSum();
	}
	
	public void drop()
	{
		error = Error.DROP;
	}

	public boolean isDropped()
	{
		return error == Error.DROP;
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
	
	public static short getLength(DatagramPacket packet)
	{
		byte[] bytes = new byte[2];

		bytes[0] = packet.getData()[2];
		bytes[1] = packet.getData()[3];
		
		return ByteShortConverter.convert(bytes);
	}
	
	public static boolean isLengthOfAck(DatagramPacket packet)
	{
		return getLength(packet) == project2.Defaults.ACK_PACKET_LENGTH;
	}
	
	public int getLength()
	{
		return length;
	}

	public void setError(Error error)
	{
		this.error = error;
	}
	
	
}
