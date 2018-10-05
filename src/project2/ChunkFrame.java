/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.net.DatagramPacket;
import java.net.InetAddress;

import project1.Chunk;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkFrame extends Frame
{
	private Chunk chunk;
	
	public ChunkFrame(Chunk c, byte n)
	{
		super();
		frameNumber = n;
		chunk = c;
	}
	
	public ChunkFrame(DatagramPacket packet)
	{
		super();
		//TODO
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
		// TODO Auto-generated method stub
		return null;
	}
}
