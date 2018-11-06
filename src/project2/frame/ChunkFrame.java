/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2.frame;

import java.net.DatagramPacket;
import java.net.InetAddress;

import byteNumberConverter.ByteIntConverter;
import byteNumberConverter.ByteShortConverter;
import project1.Chunk;

/**Data Chunk Frame
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrame extends Frame
{
	private Chunk chunk;
	protected int sequenceNumber;
	public static final int HEADER_SIZE = 8;

	/** Constructs a ChunkFrame
	 * @param c the Chunk of data held in this frame as payload
	 * @param sequenceNumber The int sequenceNumber of this ChunkFrame
	 */
	public ChunkFrame(Chunk c, int sequenceNumber)
	{
		super();
		this.sequenceNumber = sequenceNumber;
		chunk = c;
		length = (short) (c.getBytes().length + HEADER_SIZE);
	}

	/** Constructs a ChunkFrame from a packet
	 * @param packet the DatagramPacket to extract a ChunkFrame from
	 */
	public ChunkFrame(DatagramPacket packet)
	{
		super();
		
		byte[] packetB = packet.getData();
		int i = 0;
		int j = 0;
		byte[] checkSumB = new byte[2], 
				lenB = new byte[2], 
				seqB = new byte[4],
				dataB;
		
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
		
		// gets the sequence bytes
		for(j = 0; j < seqB.length; j++, i++)
		{
			seqB[j] = packetB[i];
		}
		this.sequenceNumber = byteNumberConverter.ByteIntConverter.convert(seqB);
		
		// gets the data bytes
		dataB = new byte[length - HEADER_SIZE];
		for(j = 0; j < dataB.length; j++, i++)
		{
			dataB[j] = packetB[i];
		}
		this.chunk = new Chunk(dataB, dataB.length);		
		
		//check for corruption
		if(failedCheckSum())
		{
			this.error = Error.CORRUPT;
		}
	}

	/** Extracts Chunk from the ChunkFrame
	 * @return the Chunk contained in the ChunkFrame
	 */
	public Chunk extractChunk()
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
		
		// pack sequenceNumber number
		for(byte b : ByteIntConverter.convert(sequenceNumber))
		{
			bytes[i++] = b;
		}
		
		// pack data
		for(byte b : chunk.getBytes())
		{
			bytes[i++] = b;
		}
		
		return new DatagramPacket(bytes, length, address, port);
	}

	
	/**Gets the sequence number
	 * @return the int sequence number
	 */
	public int getSequenceNumber()
	{
		return sequenceNumber;
	}


	
}
