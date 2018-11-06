/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import byteNumberConverter.ByteShortConverter;

/**
 * Frame abstract class
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Frame
{
	public enum Error
	{
		DROP, CORRUPT, NONE
	}

	private static final short BAD = 1;
	private static final short GOOD = 0;
	protected Error error = Error.NONE;
	protected short checkSum = GOOD;
	protected short length;

	/**
	 * Gets the length of the frame contained in the packet from the length
	 * bytes
	 * 
	 * @param packet
	 *            the packet to check the length of
	 * @return the length of the frame contained in the packet
	 */
	public static short getLength(DatagramPacket packet)
	{
		byte[] bytes = new byte[2];

		bytes[0] = packet.getData()[2];
		bytes[1] = packet.getData()[3];

		return ByteShortConverter.convert(bytes);
	}

	/**
	 * checks to see if the length of the packet is the lenght of an Ack packet
	 * 
	 * @param packet
	 *            the packet to check the length of
	 * @return true if the packet has a length equal to the length of an ack
	 *         packet, else false
	 */
	public static boolean isLengthOfAck(DatagramPacket packet)
	{
		return getLength(packet) == project2.Defaults.ACK_PACKET_LENGTH;
	}

	/**
	 * corrupts the Frame
	 *
	 */
	public void corrupt()
	{
		error = Error.CORRUPT;
		checkSum = BAD;
	}

	/**
	 * Drops the packet
	 *
	 */
	public void drop()
	{
		error = Error.DROP;
	}

	/**
	 * Checks to see if the check sum fails for the frame
	 * 
	 * @return true if the checksum fails else false
	 */
	public boolean failedCheckSum()
	{
		return !passedCheckSum();
	}

	/**
	 * gets the type of error
	 * 
	 * @return an Error value associated with the type of error, DROP if the
	 *         frame was dropped, DELAY if delayed, CORRUPT if corrupted, null
	 *         if no error
	 */
	public Error getError()
	{
		return error;
	}

	/**
	 * returns the length of the frame
	 * 
	 * @return the int length of the frame
	 */
	public int getLength()
	{
		return length;
	}

	/**
	 * checks to see if the frame is dropped
	 * 
	 * @return true if the frame is dropped else false
	 */
	public boolean isDropped()
	{
		return error == Error.DROP;
	}

	/**
	 * Checks to see if the CheckSum passes
	 * 
	 * @return true if it passes the checksum, false if corrupted
	 */
	public boolean passedCheckSum()
	{
		return checkSum == GOOD;
	}

	/**
	 * Send the frame
	 * 
	 * @param socket
	 *            the DatagramSocket to use to send the frame
	 * @param address
	 *            the InetAddress of the destination
	 * @param port
	 *            the int port of the destination
	 * @throws IOException
	 *             if an IOException results during the sending process.
	 */
	public void send(DatagramSocket socket, InetAddress address, int port) throws IOException
	{
		socket.send(this.toDatagramPacket(address, port));
	}

	/**
	 * Sets the error to the parameter
	 * 
	 * @param error
	 *            the error to set to
	 */
	public void setError(Error error)
	{
		this.error = error;
		if (error == Error.CORRUPT)
		{
			checkSum = BAD;
		}
		else
		{
			checkSum = GOOD;
		}
	}

	/**
	 * Creates a DatagramPacket
	 * 
	 * @param address
	 *            The INetAddress of the destination
	 * @param port
	 *            the int representation of the destination port
	 * @return a DatagramPacket that can be converted back into a frame
	 *         addressed to the destination
	 */
	public abstract DatagramPacket toDatagramPacket(InetAddress address, int port);

}
