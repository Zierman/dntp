package project1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

import log.Log;
import log.Loggable;

/** an object that assembles chunks into files
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileAssembler implements Loggable {
		private Log log = new Log();
		private String filename;
		private LinkedList<Chunk> chunks = new LinkedList<Chunk>();
		
		/** Constructs a FileAssembler object
		 * @param filename the filename of the file to be assembled
		 */
		public FileAssembler(String filename)
		{
			if(filename.length() < 1 || filename.startsWith(".") || filename.endsWith("."))
			{
				throw new IllegalArgumentException();
			}
			
			this.filename = filename;
		}
		
		/** Accepts a chunk
		 * @param chunk the chunk to accept
		 */
		public void accept(Chunk chunk)
		{
			chunks.add(chunk);
			log.addLine("Chunk " + chunk.toString() + " accepted by the FileAssembler");
		}
		
		/** Assembles a File from an Iterable collection of Chunks
		 * @return the File that is assembled
		 * @throws IOException if there is a problem writing to file
		 */public File assembleFile() throws IOException
		{

				String path = filename;
				log.addLine("path set to \"" + path + "\"");
				
				File file = new File(path);
				FileOutputStream out = new FileOutputStream(file);
				log.addLine("file oppend at " + file.getAbsolutePath());
				
				for(Chunk c : chunks)
				{
					out.write(c.getBytes());
					log.addLine("Chunk " + c.toString() + " written to " + file.getName());
				}
				out.close();
				log.addLine("Close file");
				
				return file;
		}
		
		
		/**
		 * @param filename the filename of the desired output file
		 * @param chunks an Iterable collection of chuncks to be assembled
		 * @return the file written
		 * @throws IOException if there is a problem writing to file
		 */
		public static File assembleFile(String filename, Iterable<Chunk> chunks) throws IOException
		{
			FileAssembler assembler = new FileAssembler(filename);
			for(Chunk c : chunks)
			{
				assembler.accept(c);
			}
			return assembler.assembleFile();
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