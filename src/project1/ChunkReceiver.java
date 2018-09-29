package project1;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;

import byteIntConverter.ByteIntConverter;
import log.Log;
import log.Loggable;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 * compile with the following line (from project root):
 * javac -d bin -cp bin -sourcepath src src\project1\ChunkReceiver.java
 * 
 * run with the follwoing line (from project root):
 * java -cp bin project1.ChunkReceiver
 */
public class ChunkReceiver implements Loggable
{
	private Log log = new Log();
	private InetAddress ip;
	private int port;

	public ChunkReceiver(InetAddress ip, int port)
	{
		setReceivingAddress(ip);
		setReceivingPort(port);
	}

	private void setReceivingPort(int port)
	{
		this.port = port;
		
	}

	private void setReceivingAddress(InetAddress ip)
	{
		this.ip = ip;
		
	}
	
	public LinkedList<Chunk> receive() throws IOException
	{
		LinkedList<Chunk> chunks = new LinkedList<Chunk>();
		
		// Set up a socket
		
		
		// receive first packet
		
		
		
		// Determine the number of chunks to expect by converting the byte[] to int
		

		// Determine the max bytes per chunk
		
		
		// Receive all chunks
		
		
		// close socket
		
		
		// return list
		return chunks;
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
}
