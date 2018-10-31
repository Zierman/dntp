/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import byteNumberConverter.ByteIntConverter;
import byteNumberConverter.ByteShortConverter;
import project2.Defaults;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class AckFrame extends Frame
{

	private int ackNumber;
	

	/**Gets the acknowledgement number
	 * @return the int acknowledgement number
	 */
	public int getAckNumber()
	{
		return ackNumber;
	}
	
	public static class AckFrameLengthMismatchException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		AckFrameLengthMismatchException()
		{
			super("AckFrame's length data and expected length do not match.");
		}
	}
	
	public static final int ACK_SIZE = Defaults.ACK_PACKET_LENGTH;

	public AckFrame(ChunkFrame f, int numberOfAckNumbers)
	{
		super();
		ackNumber = f.getSequenceNumber() % numberOfAckNumbers;
		length = ACK_SIZE;
	}
	
	public AckFrame(DatagramPacket p) throws AckFrameLengthMismatchException
	{
		byte[] packetB = p.getData();
		int i = 0;
		int j = 0;
		byte[] checkSumB = new byte[2], lenB = new byte[2], acknoB = new byte[4];
		
		// gets check sum bytes
		for(j = 0; j < checkSumB.length; j++, i++)
		{
			checkSumB[j] = packetB[i];
		}
		this.checkSum = byteNumberConverter.ByteShortConverter.convert(checkSumB);
		
		// gets length bytes
		for(j = 0; j < lenB.length; j++, i++)
		{
			lenB[j] = packetB[i];
		}
		this.length = byteNumberConverter.ByteShortConverter.convert(lenB);
		
		if(this.length != ACK_SIZE)
		{
			throw new AckFrameLengthMismatchException();
		}
		// gets Ack number bytes
		for(j = 0; j < acknoB.length; j++, i++)
		{
			acknoB[j] = packetB[i];
		}
		this.ackNumber = byteNumberConverter.ByteIntConverter.convert(acknoB);
		
		//check for corruption
		if(failedCheckSum())
		{
			this.error = Error.CORRUPT;
		}
		
	}

	/* (non-Javadoc)
	 * @see project2.Frame#toDatagramPacket(java.net.InetAddress, int)
	 */
	@Override
	public DatagramPacket toDatagramPacket(InetAddress address, int port)
	{

		int i = 0;
		byte[] bytes = new byte[length];
		
		// pack checksum;
		for(byte b : ByteShortConverter.convert(checkSum))
		{
			bytes[i++] = b;
		}
		
		// pack length
		for(byte b : ByteShortConverter.convert(length))
		{
			bytes[i++] = b;
		}
		
		// pack ack number
		for(byte b : ByteIntConverter.convert(ackNumber))
		{
			bytes[i++] = b;
		}
		
		return new DatagramPacket(bytes, length, address, port);
	}
	
}
