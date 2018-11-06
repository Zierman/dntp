package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import log.Log;
import log.Loggable;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileSplitter implements Loggable
{
	private static final boolean SHOW = false;
	private Log log = new Log();
	private String filename;
	private int bytesPerChunk;

	/**
	 * creates a filesplitter object
	 * 
	 * @param filename
	 *            the filename of the file that will split
	 * @param bytesPerChunk
	 *            the integer max number of bytes stored in a chunk
	 */
	public FileSplitter(String filename, int bytesPerChunk)
	{
		if (bytesPerChunk < 1 || filename.length() < 1 || filename.startsWith(".") || filename.endsWith("."))
		{
			throw new IllegalArgumentException();
		}

		this.filename = filename;
		log.addLine("filename set to \"" + this.filename + "\"");

		this.bytesPerChunk = bytesPerChunk;
		log.addLine("bytesPerChunk set to " + this.bytesPerChunk);
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

	/**
	 * Overwrites the queue of chunks with the chunks made from the file
	 * 
	 * @param chunkQueue
	 *            The queue to be overwritten with the chunks
	 * @throws IOException
	 *             if there is any problem with IO that cannot be handled in the
	 *             method
	 */
	public void overwrite(Queue<Chunk> chunkQueue) throws IOException
	{

		// Clear the queue
		chunkQueue.clear();
		log.addLine("Chunk Queue cleared");

		// Ready a list for Bytes in file
		LinkedList<Byte> bytes = new LinkedList<Byte>();
		log.addLine("Create a list to hold bytes");

		// set filepath
		String path = filename; // we can change this to use a folder if we want
		log.addLine("path set to: " + path);

		// read file
		File file = new File(path);
		FileInputStream in;
		try
		{
			log.addLine("Try to open file at " + file.getAbsolutePath());

			in = new FileInputStream(file);
			log.addLine("File opened and input stream created");

		}
		catch (FileNotFoundException e)
		{
			byte[] tmpByteArray;
			log.addLine("Failed to open file at " + file.getAbsolutePath());

			final String NEWLINE = System.lineSeparator();

			System.err.println("File to assemble not found.");

			FileOutputStream out = new FileOutputStream(file);
			log.addLine("New file created at " + file.getAbsolutePath());

			out.write(tmpByteArray = new String("Hello World!").getBytes());
			log.addLine("Writing {" + Log.getString(tmpByteArray) + "}(bytes for \"Hello World!\" to file)");

			for (Integer i = 0; i < 100; i++)
			{
				out.write(tmpByteArray = NEWLINE.getBytes());
				log.addLine("Writing {" + Log.getString(tmpByteArray) + "} to file (bytes for system's NEWLINE)");

				out.write(tmpByteArray = i.toString().getBytes());
				log.addLine("Writing {" + Log.getString(tmpByteArray) + "} to file (bytes for string \"" + i + "\")");
			}

			out.write(tmpByteArray = NEWLINE.getBytes());
			log.addLine("Writing {" + Log.getString(tmpByteArray) + "} to file (bytes for system's NEWLINE)");

			out.write(tmpByteArray = new String("-JoshuaZierman").getBytes());
			log.addLine("Writing {" + Log.getString(tmpByteArray) + "} (bytes for \"-JoshuaZierman\")");

			out.close();
			log.addLine("close file");

			System.err.println(file.getAbsolutePath() + " created");
			in = new FileInputStream(file);
			log.addLine("open file at " + path);
		}

		// split into chunks and fill queue
		byte[] bArray = new byte[bytesPerChunk];
		Integer i;
		int j = 0;
		int cnt = 0; // used for spacing out output
		while ((i = in.read()) != -1)
		{
			// // let user know that progress is being made
			// if(cnt++ % 1000 == 0)
			// {
			// System.out.println(in.available() + " remaining to be read in
			// file");
			// }
			if (SHOW)
			{
				log.addLine("read byte " + Log.getString(i.byteValue()) + " from " + file.getName());
			}

			if (j < bytesPerChunk)
			{
				bArray[j] = i.byteValue();
			}
			else // j == bytesPerChunk
			{
				chunkQueue.add(new Chunk(bArray, bArray.length));

				// Log the chunk added
				if (SHOW)
				{
					log.addLine("add Chunk with bytes {" + Log.getString(bArray) + "} to Chunk collection");
				}

				j = 0;
				bArray = new byte[bytesPerChunk];
				bArray[j] = i.byteValue();
			}

			j++;
			// //>
			// System.out.println(in.available());
			// bytes.add(i.byteValue());
			// log.addLine("read byte " + Log.getString(i.byteValue()) + " from
			// " + file.getName());
			// //<
		}
		in.close();
		if (j != 0)
		{
			byte[] tmp = new byte[j];
			for (int k = 0; k < j; k++)
			{
				tmp[k] = bArray[k];
			}
			chunkQueue.add(new Chunk(tmp, tmp.length));

			// Log the chunk added
			if (SHOW)
			{
				log.addLine("add Chunk with bytes {" + Log.getString(tmp) + "} to Chunk collection");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see log.Loggable#printLog(java.io.PrintStream)
	 */
	@Override
	public void printLog(PrintStream printStream)
	{
		log.print(printStream);

	}
}