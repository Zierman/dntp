package project1;

import java.util.Iterator;

/**
 * A chunk of bytes
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Chunk implements Iterable<Byte>
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

	/**
	 * Constructs a chunk with the given bytes
	 * 
	 * @param bytes an array of bytes that holds the data for the chunk
	 */
	public Chunk(byte[] bytes) {
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
	}
}
