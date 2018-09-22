package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class FileSplitter {
	private String filename;
	private int bytesPerChunk;
	
	/** creates a filesplitter object
	 * @param filename the filename of the file that will split
	 */
	public FileSplitter(String filename, int bytesPerChunk)
	{
		this.filename = filename;
		this.bytesPerChunk = bytesPerChunk;
	}
	
	/** Overwrites the queue of chunks with the chunks made from the file
	 * @param chunkQueue The queue to be overwritten with the chunks
	 * @return the queue that was overwritten.
	 * @throws IOException 
	 */
	public <Q extends Queue<Chunk>> void overwrite(Q chunkQueue) throws IOException
	{	
		// Clear the queue
		chunkQueue.clear();
		
		// Ready a list for Bytes in file
		LinkedList<Byte> bytes = new LinkedList<Byte>();
		
		// set filepath
		String path = filename; // we can change this to use a folder if we want
		
		// read file
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
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
				chunkQueue.add(new Chunk(bArray));
				j = 0;
			}
		}
		if(j != 0)
		{
			chunkQueue.add(new Chunk(bArray));
		}
		
	}
}