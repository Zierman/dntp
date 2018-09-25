package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import log.Log;
import log.Loggable;

public class FileSplitter implements Loggable {
	private Log log = new Log();
	private String filename;
	private int bytesPerChunk;
	
	/** creates a filesplitter object
	 * @param filename the filename of the file that will split
	 * @param bytesPerChunk the integer max number of bytes stored in a chunk
	 */
	public FileSplitter(String filename, int bytesPerChunk)
	{
		log.addLine("FileSplitter constructor called");
		
		this.filename = filename;
		log.addLine("filename set to \"" + this.filename + "\"");
		
		this.bytesPerChunk = bytesPerChunk;
		log.addLine("bytesPerChunk set to " + this.bytesPerChunk);
	}
	
	/** Overwrites the queue of chunks with the chunks made from the file
	 * @param chunkQueue The queue to be overwritten with the chunks
	 * @throws IOException if there is any problem with IO that cannot be handled in the method
	 */
	public void overwrite(Queue<Chunk> chunkQueue) throws IOException
	{	
		log.addLine("FileSplitter.overwrite() called");
		
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
			
		} catch (FileNotFoundException e)
		{
			byte[] tmpByteArray;
			log.addLine("Failed to open file at " + file.getAbsolutePath());
			
			final String NEWLINE = System.lineSeparator();
			
			System.err.println("File to assemble not found.");
			
			FileOutputStream out = new FileOutputStream(file);
			log.addLine("New file created at " + file.getAbsolutePath());
			
			out.write(tmpByteArray = new String("Hello World!").getBytes());
			log.addLine("Writing {" + Log.getStringFromBytes(tmpByteArray) + "}(bytes for \"Hello World!\" to file)");
			
			for(Integer i = 0; i < 100; i++)
			{
				out.write(tmpByteArray = NEWLINE.getBytes());
				log.addLine("Writing {" + Log.getStringFromBytes(tmpByteArray) + "} to file (bytes for system's NEWLINE)");
				
				out.write(tmpByteArray = i.toString().getBytes());
				log.addLine("Writing {" + Log.getStringFromBytes(tmpByteArray) + "} to file (bytes for string \"" + i + "\")");
			}
			
			out.write(tmpByteArray = NEWLINE.getBytes());
			log.addLine("Writing {" + Log.getStringFromBytes(tmpByteArray) + "} to file (bytes for system's NEWLINE)");
			
			out.write(tmpByteArray = new String("-JoshuaZierman").getBytes());
			log.addLine("Writing {" + Log.getStringFromBytes(tmpByteArray) + "} (bytes for \"-JoshuaZierman\")");
			
			out.close();
			log.addLine("close file");
			
			System.err.println(file.getAbsolutePath() + " created");
			in = new FileInputStream(file);
			log.addLine("open file at " + path);
		}
		
		Integer i;
		while((i = in.read()) != -1)
		{
			bytes.add(i.byteValue());
			log.addLine("read byte " + i.byteValue() + " from " + file.getName());
		}
		in.close();
		
		// split into chunks and fill queue
		byte[] bArray = new byte[bytesPerChunk];
		int j = 0;
		for(Byte b : bytes)
		{
			if(j < bytesPerChunk)
			{
				bArray[j++] = b;
			}
			else
			{
				j = 0;
				chunkQueue.add(new Chunk(bArray));
				
				// Logg the chunk added
				log.addLine("add Chunk with bytes {" + Log.getStringFromBytes(bArray) + "} added to chunks");
				
				bArray = new byte[bytesPerChunk];
				bArray[j++] = b;
			}
		}
		if(j != 0)
		{
			byte[] tmp = new byte[j];
			for(int k = 0; k < j; k++)
				tmp[k] = bArray[k];
			chunkQueue.add(new Chunk(bArray));

			// Log the chunk added
			log.add("add Chunk with bytes {" + Log.getStringFromBytes(tmp) + "}");
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
}