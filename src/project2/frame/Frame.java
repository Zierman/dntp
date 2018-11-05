/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import byteNumberConverter.ByteShortConverter;

/** Frame abstract class
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Frame
{
	public enum Error {DROP, CORRUPT, NONE}
	private static final short BAD = 1;
	private static final short GOOD = 0;
	protected Error error = Error.NONE;
	protected short checkSum = GOOD;
	protected short length;

	//TODO document
	public abstract DatagramPacket toDatagramPacket(InetAddress address, int port);

	
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

	//TODO document
	public boolean failedCheckSum()
	{
		return !passedCheckSum();
	}

	//TODO document
	public void drop()
	{
		error = Error.DROP;
	}

	//TODO document
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

	//TODO document
	public void send(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		socket.send(this.toDatagramPacket(address, port));
	}


	//TODO document
	public static short getLength(DatagramPacket packet)
	{
		byte[] bytes = new byte[2];

		bytes[0] = packet.getData()[2];
		bytes[1] = packet.getData()[3];
		
		return ByteShortConverter.convert(bytes);
	}

	//TODO document
	public static boolean isLengthOfAck(DatagramPacket packet)
	{
		return getLength(packet) == project2.Defaults.ACK_PACKET_LENGTH;
	}

	//TODO document
	public int getLength()
	{
		return length;
	}

	//TODO document
	public void setError(Error error)
	{
		this.error = error;
		if(error == Error.CORRUPT)
		{
			checkSum = BAD;
		}
	}
	
	
}
