package project1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/** an object that assembles chunks into files
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileAssembler {

		String filename;
		LinkedList<Chunk> chunks = new LinkedList<Chunk>();
		
		/** Constructs a FileAssembler object
		 * @param filename the filename of the file to be assembled
		 */
		public FileAssembler(String filename)
		{
			this.filename = filename;
		}
		
		/** Accepts a chunk
		 * @param chunk the chunk to accept
		 */
		public void accept(Chunk chunk)
		{
			chunks.add(chunk);
		}
		
		/** Assembles a File from an Iterable collection of Chunks
		 * @return
		 * @throws IOException if there is a problem writing to file
		 */public File assembleFile() throws IOException
		{
			return assembleFile(filename, chunks);
		}
		
		
		/**
		 * @param filename the filename of the desired output file
		 * @param chunks an Iterable collection of chuncks to be assembled
		 * @return the file written
		 * @throws IOException if there is a problem writing to file
		 */
		public static File assembleFile(String filename, Iterable<Chunk> chunks) throws IOException
		{
			String path = filename;
			File file = new File(path);
			FileOutputStream out = new FileOutputStream(file);
			
			for(Chunk c : chunks)
			{
				out.write(c.getBytes());
			}
			out.close();
			return file;
		}
}