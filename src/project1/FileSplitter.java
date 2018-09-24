package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class FileSplitter {
	private String filename;
	private int bytesPerChunk;
	
	/** creates a filesplitter object
	 * @param filename the filename of the file that will split
	 * @param bytesPerChunk the integer max number of bytes stored in a chunk
	 */
	public FileSplitter(String filename, int bytesPerChunk)
	{
		this.filename = filename;
		this.bytesPerChunk = bytesPerChunk;
	}
	
	/** Overwrites the queue of chunks with the chunks made from the file
	 * @param chunkQueue The queue to be overwritten with the chunks
	 * @throws IOException if there is any problem with IO that cannot be handled in the method
	 */
	public void overwrite(Queue<Chunk> chunkQueue) throws IOException
	{	
		// Clear the queue
		chunkQueue.clear();
		
		// Ready a list for Bytes in file
		LinkedList<Byte> bytes = new LinkedList<Byte>();
		
		// set filepath
		String path = filename; // we can change this to use a folder if we want
		
		// read file
		File file = new File(path);
		FileInputStream in;
		try
		{
			in = new FileInputStream(file);
		} catch (FileNotFoundException e)
		{
			System.err.println("File to assemble not found.");
			FileOutputStream out = new FileOutputStream(file);
			out.close();
			System.err.println(file.getAbsolutePath() + " created");
			in = new FileInputStream(file);
		}
		Integer i;
		while((i = in.read()) != -1)
		{
			bytes.add(i.byteValue());
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
				bArray = new byte[bytesPerChunk];
				bArray[j++] = b;
			}
		}
		if(j != 0)
		{
			Byte[] tmp = new Byte[j];
			for(int k = 0; k < j; k++)
				tmp[k] = bArray[k];
			chunkQueue.add(new Chunk(bArray));
		}
		
	}
}