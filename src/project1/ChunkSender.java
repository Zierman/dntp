/**
 * File Created by Joshua Zierman on Sep 25, 2018
 */
package project1;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ChunkSender implements Sender
{
	private InetAddress destinationIp;
	private int destinationPort;
	private Chunk[] chunks;
	private int next = 0; // index of next
	
	
	/** Constructs a ChunkSender
	 * @param ip InetAddress of destination
	 * @param port
	 */
	public ChunkSender(InetAddress ip, int port)
	{
		setToAddress(ip);
		setToPort(port);
	}
	
	/** loads the sender with Chunks
	 * @param chunks the array of Chunks
	 */
	public void load(Chunk[] chunks)
	{
		this.chunks = chunks;
	}
	
	/** loads the sender with Chunks
	 * @param chunks the Queue of Chunks
	 */
	public void load(Queue<Chunk> chunks)
	{
		this.chunks = (Chunk[])chunks.toArray();
	}
	
	/* (non-Javadoc)
	 * @see project1.Sender#setToAddress(java.net.InetAddress)
	 */
	@Override
	public void setToAddress(InetAddress ip)
	{
		destinationIp = ip;
	}

	/* (non-Javadoc)
	 * @see project1.Sender#setToPort(int)
	 */
	@Override
	public void setToPort(int port)
	{
		destinationPort = port;
	}

	/* (non-Javadoc)
	 * @see project1.Sender#sendNext()
	 */
	@Override
	public void sendNext()
	{
		Chunk c = chunks[next++];
		sendChunk(c);
	}

	/** sends a Chunk
	 * @param c Chunk to send
	 */
	private void sendChunk(Chunk c)
	{
		// TODO this method needs to be completed
	}

	/* (non-Javadoc)
	 * @see project1.Sender#resetNextPosition()
	 */
	@Override
	public void resetNextPosition()
	{
		next = 0;		
	}

	/* (non-Javadoc)
	 * @see project1.Sender#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return next < chunks.length;
	}

}
