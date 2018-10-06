/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.net.DatagramPacket;
import java.net.InetAddress;

import byteIntConverter.ByteIntConverter;
import byteIntConverter.ByteShortConverter;
import project1.Chunk;
import project2.Defaults;
import project2.frame.Frame.Error;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrame extends Frame
{
	private Chunk chunk;
	protected int sequenceNumber;
	
	public ChunkFrame(Chunk c, int sequenceNumber, int ackNumber)
	{
		super(ackNumber);
		this.sequenceNumber = sequenceNumber;
		chunk = c;
		length = (short) (c.getBytes().length + 4 + Defaults.ACK_PACKET_LENGTH);
	}
	
	public ChunkFrame(DatagramPacket packet)
	{
		super();
		
		byte[] packetB = packet.getData();
		int i = 0;
		byte[] checkSumB = new byte[2], 
				lenB = new byte[2], 
				acknoB = new byte[4],
				seqB = new byte[4],
				dataB;
		
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
		
		// gets ack number bytes
		for(byte b : acknoB)
		{
			b = packetB[i++];
		}
		this.ackNumber = byteIntConverter.ByteShortConverter.convert(acknoB);
		
		// gets the sequence bytes
		for(byte b : seqB)
		{
			b = packetB[i++];
		}
		this.ackNumber = byteIntConverter.ByteShortConverter.convert(acknoB);
		
		// gets the data bytes
		dataB = new byte[length - Defaults.ACK_PACKET_LENGTH - 4];
		for(byte b : dataB)
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
	
	public Chunk toChunk()
	{
		return chunk;
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
		
		// pack sequenceNumber number
		for(byte b : ByteIntConverter.convert(sequenceNumber))
		{
			bytes[i++] = b;
		}
		
		// pack data
		for(byte b : chunk)
		{
			bytes[i++] = b;
		}
		
		return new DatagramPacket(bytes, length, address, port);
	}
}
