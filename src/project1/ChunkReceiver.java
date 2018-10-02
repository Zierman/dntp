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
	private int INITIAL_BYTE_SIZE = 4;


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
		
		try {
			// set up socket
			DatagramSocket socket = new DatagramSocket(port);
			
			byte[] initialSize = new byte[INITIAL_BYTE_SIZE];
			DatagramPacket packet = new DatagramPacket(initialSize, initialSize.length);

			// Ouptut that receiver is running
			System.out.println("Ready to receive chunks");
			// receive first packet
			socket.receive(packet);
			
			// Determine the number of chunks to expect by converting the byte[] to int
			int numberOfChunks = ByteIntConverter.convert(packet.getData());
			log.add("Receiver got first datagram");
			log.addLine(" {" + Log.getHexString(packet.getData()) + "}");
			log.addLine("\tExpecting " + numberOfChunks + " Chunks");
			log.addLine("");

			// receive second packet and determine the max bytes per chunk
			socket.receive(packet);
			int maxSizeOfChunk = ByteIntConverter.convert(packet.getData());
			log.add("Receiver got second datagram");
			log.addLine(" {" + Log.getHexString(packet.getData()) + "}");
			log.addLine("\tExpecting " + maxSizeOfChunk + " as maximum bytes per chunk");
			log.addLine("");
			
			// Receive all chunks
			packet = new DatagramPacket(new byte[maxSizeOfChunk], maxSizeOfChunk);
			for(int i = 0; i < numberOfChunks; i++)
			{
				// receive a packet
				socket.receive(packet);
				
				// Make a new chunk from the data of the packet and put it in the collection
				byte[] tmp = new byte[packet.getLength()];
				for(int j = 0; j < tmp.length; j++)
				{
					tmp[j] = packet.getData()[j];
				}
				
				chunks.add(new Chunk(tmp, packet.getLength()));
				
				log.add("ChunkReceiver received datagram " + i + "-" + packet.getOffset() + "-" + packet.getOffset() + packet.getLength());
				log.addLine(" {" + Log.getHexString(tmp) + "}");
				log.addLine("");
			}

			// close socket
			socket.close();
		} catch (Exception e) {
			System.err.println("ChunkReceiver.receive() Failed");
		}


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
