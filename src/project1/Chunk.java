package project1;

import java.io.PrintStream;
import java.net.DatagramPacket;
import java.util.Iterator;

import log.Log;
import log.Loggable;

/**
 * A chunk of bytes
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Chunk implements Iterable<Byte>, Loggable
{

	/**
	 * The iterator that makes Chunk iterable
	 * 
	 * @author Joshua Zierman [py1422xs@metrostate.edu]
	 *
	 */
	private class byteIterator implements Iterator<Byte>
	{
		int i = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return i < bytes.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Byte next()
		{
			return bytes[i++];
		}

	}

	/**
	 * An array of bytes that is a chunck of a larger file
	 */
	private byte[] bytes;
	
	/** the log for this instance's behaviour
	 * 
	 */
	private Log log = new Log();

	/**
	 * Constructs a chunk with the given bytes
	 * 
	 * @param bytes an array of bytes that holds the data for the chunk
	 */
	public Chunk(byte[] bytes) {
		// log
		log.addLine("Chunk constructor called");
		
		setBytes(bytes);
	}

	/**
	 * Gets the bytes stored in the Chunk
	 * 
	 * @return the bytes
	 */
	public byte[] getBytes()
	{
		return bytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Byte> iterator()
	{
		return new byteIterator();
	}

	/**
	 * Sets the bytes stored in the chunk
	 * 
	 * @param bytes
	 *            the bytes to set
	 */
	private void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
		
		// log
		log.add("Chunk bytes set to: ");
		boolean first = true;
		for(byte b : bytes)
		{
			if(!first)
			{
				log.add(", ");
			}
			else
			{
				first = false;
			}
			log.add("" + b);
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "{" + Log.getString(bytes) + "}";
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
