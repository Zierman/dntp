/**
 * File Created by Joshua Zierman on Sep 25, 2018
 */
package project1;

import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Queue;

import byteIntConverter.ByteIntConverter;
import log.Log;
import log.Loggable;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 * compile with the following line (from project root):
 * javac -d bin -cp bin -sourcepath src src\project1\FileSender.java
 * 
 * run with the follwoing line (from project root):
 * java -cp bin project1.FileSender
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
		this.chunks = new Chunk[chunks.size()];
		int i = 0;
		for(Chunk c : chunks)
		{
			this.chunks[i++] = c;
		}
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
				}
				if(next == 0)
				{
					int numberOfChunks = chunks.length;
					byte[] numberOfChunkBytes = ByteIntConverter.convert(numberOfChunks);
					DatagramPacket packet = new DatagramPacket(numberOfChunkBytes, numberOfChunkBytes.length, destinationIp, destinationPort);
					socket.send(packet);
				}
				DatagramPacket packet = new DatagramPacket(c.getBytes(), c.getBytes().length, destinationIp, destinationPort);
				socket.send(packet);
				log.addLine("ChunkSender sent datagram " + next + "-" + packet.getOffset() + "-"  + (packet.getOffset() + packet.getLength()));
				log.addLine("\t{" + Log.getHexString(packet.getData()) + "}");
				log.addLine("-----------------------------------------------");
			
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
	
	public void close()
	{
		if(socket != null)
			socket.close();
	}

	@Override
	public Log getLog()
	{
		
		return log;
	}

	@Override
	public void printLog(PrintStream printStream)
	{
		log.print(printStream);
		
	}

	@Override
	public void clearLog()
	{
		log.clear();
		
	}
}
