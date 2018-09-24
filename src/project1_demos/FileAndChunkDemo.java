/**
 * File Created by Joshua Zierman on Sep 23, 2018
 */
package project1_demos;

import project1.FileAssembler;
import project1.FileSplitter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import project1.Chunk;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileAndChunkDemo
{
	/** Demonstrates the FileSplitter, FileAssembler, and Chunk classes in action.
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		String s = File.separator;
		LinkedList<Chunk> chunksSender, chunksReceaver;
		chunksReceaver = new LinkedList<Chunk>();
		chunksSender = new LinkedList<Chunk>();
		FileSplitter splitter = new FileSplitter("in.txt", 5);
		FileAssembler assembler = new FileAssembler("out.txt");
		File outputFile;

		try
		{
			// split the file into chunks
			splitter.overwrite(chunksSender);

			// display success msg
			System.out.println("File Split into " + chunksSender.size() + " Chunks");
			
		} catch (IOException e)
		{
			System.err.println("File Split failure");
			e.printStackTrace();
			return;
		}

		// simulate transfer of chunks
		for (Chunk tmpChunk : chunksSender)
		{
			chunksReceaver.add(tmpChunk);
		}

		// accept the chunks on the receiving side... ok to
		// assume perfect transfer for project 1
		for (Chunk tmpChunk : chunksReceaver)
		{
			assembler.accept(tmpChunk);
		}

		try
		{
			// assemble chunks
			outputFile = assembler.assembleFile();

			// display success msg
			System.out.println(outputFile.getName() + " assembled at " + outputFile.getAbsolutePath());
			
		} catch (IOException e)
		{
			// display failure msg
			System.err.println("file failed to be assembled");
			e.printStackTrace();
			return;
		}

	}
}
