package project1;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import log.Log;
import log.Loggable;

public class FileReceiver implements Loggable
{
	private Log log = new Log();
	private InetAddress ip;
	private int port;
	private String filename;

	FileReceiver(String filename, InetAddress ip, int port)
	{
		this.filename = filename;
		setFilename(filename);
		setReceivingAddress(ip);
		setReceivingPort(port);
	}

	/**
	 * Receives a file
	 * 
	 * @param args
	 *            filename, ip, port
	 */
	public static void main(String[] args)
	{
		String filename = Project1.getOutputFilename();
		InetAddress ip = Project1.getDestinationIp();
		int port = Project1.getPort();

		// handle arguments
		if (args.length > 0)
		{
			filename = args[0];
		}
		if (args.length > 1)
		{
			try
			{
				ip = InetAddress.getByName(args[1]);
			}
			catch (UnknownHostException e)
			{
				throw new IllegalArgumentException("Invalid 2nd argument value. Must be a valid InetAddress name");
			}
		}
		if (args.length > 2)
		{
			port = Integer.parseInt(args[2]);
		}

		// Receive File
		FileReceiver receiver = new FileReceiver(filename, ip, port);
		try
		{
			receiver.receiveFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Output Log
		receiver.printLog(System.out);
		receiver.clearLog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see log.Loggable#absorbLog(log.Loggable)
	 */
	@Override
	public void absorbLog(Loggable l)
	{
		log.absorb(l.getLog());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see log.Loggable#clearLog()
	 */
	@Override
	public void clearLog()
	{
		log.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see log.Loggable#getLog()
	 */
	@Override
	public Log getLog()
	{
		return log;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see log.Loggable#printLog(java.io.PrintStream)
	 */
	@Override
	public void printLog(PrintStream printStream)
	{
		printStream.println("<FileReceiver Log Start>");
		log.print(printStream);
		printStream.println("<FileReceiver Log End>");
	}

	public void receiveFile() throws IOException
	{
		ChunkReceiver receiver = new ChunkReceiver(ip, port);
		absorbLog(receiver);

		LinkedList<Chunk> chunks = receiver.receive();
		absorbLog(receiver);

		FileAssembler assembler = new FileAssembler(filename);
		for (Chunk c : chunks)
		{
			assembler.accept(c);
		}
		assembler.assembleFile();
		absorbLog(assembler);
	}

	/**
	 * Sets the filename
	 * 
	 * @param filename
	 *            a string that is a filename
	 */
	private void setFilename(String filename)
	{
		if (filename.length() < 1 || filename.startsWith(".") || filename.endsWith("."))
		{
			throw new IllegalArgumentException();
		}
		this.filename = filename;

	}

	/**
	 * sets the address
	 * 
	 * @param ip
	 *            the InetAddress
	 */
	private void setReceivingAddress(InetAddress ip)
	{
		this.ip = ip;

	}

	/**
	 * sets the port
	 * 
	 * @param port
	 *            the integer of the port
	 */
	private void setReceivingPort(int port)
	{
		this.port = port;

	}
}
