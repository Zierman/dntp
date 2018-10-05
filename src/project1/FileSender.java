/**
 * File Created by Joshua Zierman on Sep 25, 2018
 */
package project1;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

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
 */
public class FileSender implements Sender, Loggable
{
	private Log log = new Log();
	private InetAddress destinationIp;
	private int destinationPort, bytesPerChunk;
	private String filename;
	private LinkedList<Chunk> chunks = new LinkedList<Chunk>();
	private int next;
	
	FileSender(String filename, InetAddress ip, int port, int bytesPerChunk)
	{
		setBytesPerChunk(bytesPerChunk);
		setFilename(filename);
		setToAddress(ip);
		setToPort(port);
	}

	/** sets the number of bytes per chunk
	 * @param bytesPerChunk an integer value that represents the number of bytes per chunk
	 */
	private void setBytesPerChunk(int bytesPerChunk)
	{
		if(bytesPerChunk < 1 )
		{
			throw new IllegalArgumentException();
		}
		this.bytesPerChunk = bytesPerChunk;
	}

	/** sets the filename
	 * @param filename the String containing the filename
	 */
	private void setFilename(String filename)
	{
		if(filename.length() < 1 || filename.endsWith("."))
		{
			throw new IllegalArgumentException();
		}
		this.filename = filename;
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
		send();
		next++;
	}

	/** Sends the file
	 * 
	 */
	private void send()
	{
		
		try
		{
			FileSplitter splitter = new FileSplitter(filename, bytesPerChunk);
			splitter.overwrite(chunks);
			absorbLog(splitter);
			ChunkSender chunkSender = new ChunkSender(destinationIp, destinationPort);
			chunkSender.load(chunks);
			while(chunkSender.hasNext())
			{
				chunkSender.sendNext();
			}
			absorbLog(chunkSender);
			
		} catch (IOException e)
		{
			e.printStackTrace();
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
		return next < 1;
	}
	
	/** Sends a File to a destination
	 * @param args filename, bytesPerChunk, toIp, toPort
	 */
	public static void main(String[] args)
	{
		String filename = Project1.getInputFilename();
		int bytesPerChunk = Project1.getBytesPerChunk();
		InetAddress toIp = Project1.getDestinationIp();
		int toPort = Project1.getPort();
		
		// handle inline args (and default bytesPerChunk override for large files)
		if(args.length > 0)
		{
			filename = args[0];
		}
		if(args.length > 1)
		{
			bytesPerChunk = Integer.parseInt(args[1]);
		}
		else
		{
			try
			{
				long bytesInFile  = new File(filename).length();
				if(bytesInFile > Project1.getMinBytesInFileBeforeBpcOverride())
				{
					bytesPerChunk = (int)(bytesInFile / 20);
				}
				
			}
			catch(Exception e)
			{
				//dont do anything
			}
		}
		if(args.length > 2)
		{
			try
			{
				toIp = InetAddress.getByName(args[2]);
			}
			catch (UnknownHostException e)
			{
				throw new IllegalArgumentException("Invalid 3rd argument value. Must be a valid InetAddress name");
			}
		}if(args.length > 3)
		{
			toPort = Integer.parseInt(args[3]);
		}
		
		// sendAllFiles ... incidentally only one file
		FileSender sender = new FileSender(filename, toIp, toPort, bytesPerChunk);
		while(sender.hasNext())
		{
			sender.sendNext();
		}
		
		// print log
		sender.printLog(System.out);
		sender.clearLog();
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
		printStream.println("<FileSender Log Start>");
		log.print(printStream);
		printStream.println("<FileSender Log End>");
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
