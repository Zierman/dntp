/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import byteIntConverter.ByteIntConverter;
import byteIntConverter.ByteShortConverter;
import project2.Defaults;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class AckFrame extends Frame
{
	public AckFrame(ChunkFrame f)
	{
		super(f.getAckNumber());
		length = Defaults.ACK_PACKET_LENGTH;
	}
	
	public AckFrame(DatagramPacket p)
	{
		byte[] packetB = p.getData();
		int i = 0;
		byte[] checkSumB = new byte[2], lenB = new byte[2], acknoB = new byte[4];
		
		// gets check sum bytes
		for(byte b : checkSumB)
		{
			b = packetB[i++];
		}
		this.checkSum = byteIntConverter.ByteShortConverter.convert(checkSumB);
		
		// gets length bytes
		for(byte b : lenB)
		{
			b = packetB[i++];
		}
		this.length = byteIntConverter.ByteShortConverter.convert(lenB);
		
		// gets Ack number bytes
		for(byte b : acknoB)
		{
			b = packetB[i++];
		}
		this.ackNumber = byteIntConverter.ByteShortConverter.convert(acknoB);
		
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
