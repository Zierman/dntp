/**
 * File Created by Joshua Zierman on Sep 25, 2018
 */
package project1;

import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;

import byteNumberConverter.ByteIntConverter;
import log.Log;
import log.Loggable;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 * 
 * 
 */
public class ChunkSender implements Sender, Loggable
{
	private Log log = new Log();
	private InetAddress destinationIp;
	private int destinationPort;
	private Chunk[] chunks;
	private int next = 0; // index of next
	private DatagramSocket socket;
	private int maxBytesInChunk = 0;
	
	
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
		log.addLine("Loading chunks into ChunkSender");
		this.chunks = new Chunk[chunks.length];
		int i = 0;
		for(Chunk c : chunks)
		{
			this.chunks[i++] = c;
			if(c.getBytes().length > maxBytesInChunk)
			{
				maxBytesInChunk = c.getBytes().length;
			}
		}
		this.chunks = chunks;

		log.addLine("Finished Loading chunks into ChunkSender");
	}
	
	/** loads the sender with Chunks
	 * @param chunks the Queue of Chunks
	 */
	public void load(Queue<Chunk> chunks)
	{
		log.addLine("Loading chunks into ChunkSender");
		this.chunks = new Chunk[chunks.size()];
		int i = 0;
		for(Chunk c : chunks)
		{
			this.chunks[i++] = c;
			if (c.getBytes().length > maxBytesInChunk)
			{
				maxBytesInChunk = c.getBytes().length;
			}
		}

		log.addLine("Finished Loading chunks into ChunkSender");
	}
	
	/* (non-Javadoc)
	 * @see project1.Sender#setToAddress(java.net.InetAddress)
	 */
	@Override
	public void setToAddress(InetAddress ip)
	{
		destinationIp = ip;
		log.addLine("ChunkSender set destination address set to " + ip.getCanonicalHostName());
	}

	/* (non-Javadoc)
	 * @see project1.Sender#setToPort(int)
	 */
	@Override
	public void setToPort(int port)
	{
		destinationPort = port;
		log.addLine("ChunkSender set destinationPort set to " + port);
	}

	/* (non-Javadoc)
	 * @see project1.Sender#sendNext()
	 */
	@Override
	public void sendNext()
	{
		sendChunk(chunks[next]);
		next++;
		if(!hasNext())
		{
			close();
		}
	}

	/** sends a Chunk
	 * @param c Chunk to send
	 */
	private void sendChunk(Chunk c)
	{
		if(c == null)
		{
			throw new IllegalArgumentException("chunk was null");
		}
		try
		{
				if(socket == null)
				{
					socket = new DatagramSocket();
					log.addLine("create new datagram socket");
					log.addLine("");
				}
				if(next == 0)
				{
					// Send how many chunks to expect
					int numberOfChunks = chunks.length;
					byte[] numberOfChunkBytes = ByteIntConverter.convert(numberOfChunks);
					DatagramPacket packet = new DatagramPacket(numberOfChunkBytes, numberOfChunkBytes.length, destinationIp, destinationPort);
					socket.send(packet);

					log.add("sent packet telling receiver to expect " + ByteIntConverter.convert(numberOfChunkBytes) + " Chunks");
					log.addLine(" {" + Log.getString(packet.getData()) + "}");
					log.addLine("");

					
					// Send max number of bytes per chunk
					packet = new DatagramPacket(ByteIntConverter.convert(getMaxBytesInChunk()), 4, destinationIp, destinationPort);
					socket.send(packet);

					log.add("sent packet telling receiver that the max number of bytes in a chunk is " + ByteIntConverter.convert(packet.getData()));
					log.addLine(" {" + Log.getString(packet.getData()) + "}");
					log.addLine("");

				}
				
				DatagramPacket packet = new DatagramPacket(c.getBytes(), c.getBytes().length, destinationIp, destinationPort);
				socket.send(packet);
				log.add("ChunkSender sent datagram " + next + "-" + packet.getOffset() + "-"  + (packet.getOffset() + packet.getLength()));
				log.addLine(" {" + Log.getString(packet.getData()) + "}");
				log.addLine("");
			
		}
		catch (Exception e) {
			System.err.println("ChunkSender.sendChunk() Failed");
		}
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
	
	/** Close the socket of the ChunkSender
	 * 
	 */
	public void close()
	{
		if(socket != null)
		{
			socket.close();
			log.addLine("socket closed");
		}
	}

	/* (non-Javadoc)
	 * @see log.Loggable#getLog()
	 */
	@Override
	public Log getLog()
	{
		
		return log;
	}

	/* (non-Javadoc)
	 * @see log.Loggable#printLog(java.io.PrintStream)
	 */
	@Override
	public void printLog(PrintStream printStream)
	{
		log.print(printStream);
		
	}

	/* (non-Javadoc)
	 * @see log.Loggable#clearLog()
	 */
	@Override
	public void clearLog()
	{
		log.clear();
		
	}
	
	/* (non-Javadoc)
	 * @see log.Loggable#absorbLog(log.Loggable)
	 */
	@Override
	public void absorbLog(Loggable l)
	{
		log.absorb(l.getLog());
	}

	/** gets the max number of bytes in any chunk
	 * @return the maxBytesInChunk
	 */
	protected int getMaxBytesInChunk()
	{
		return maxBytesInChunk;
	}
}
