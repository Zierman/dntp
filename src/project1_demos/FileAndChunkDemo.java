/**
 * File Created by Joshua Zierman on Sep 23, 2018
 */
package project1_demos;

import project1.FileAssembler;
import project1.FileSplitter;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;

import log.Log;
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
		final String SEPARATOR = File.separator;
		
		LinkedList<Chunk> chunksSender, chunksReceaver;
		
		chunksReceaver = new LinkedList<Chunk>();
		
		chunksSender = new LinkedList<Chunk>();
		
		FileSplitter splitter = new FileSplitter("in.txt", 5);
		splitter.printLog(System.out);
		splitter.clearLog();
		
		FileAssembler assembler = new FileAssembler("out.txt");
		assembler.printLog(System.out);
		assembler.clearLog();
		
		File outputFile;

		// print log of splitter
		try
		{
			// split the file into chunks
			splitter.overwrite(chunksSender);
			splitter.printLog(System.out);
			splitter.clearLog();

			// display success msg
			System.out.println("File Split into " + chunksSender.size() + " Chunks");
			
		} catch (IOException e)
		{
			// display failure msg
			System.err.println("File Split failure");
			e.printStackTrace();
			return;
		}

		// simulate transfer of chunks
		for (Chunk tmpChunk : chunksSender)
		{
			chunksReceaver.add(tmpChunk);
			System.out.println("Chunk " + tmpChunk.toString() + " tranfered");
		}

		// accept the chunks on the receiving side... ok to
		// assume perfect transfer for project 1
		for (Chunk tmpChunk : chunksReceaver)
		{
			assembler.accept(tmpChunk);
			assembler.printLog(System.out);
			assembler.clearLog();
		}

		try
		{
			// assemble chunks
			outputFile = assembler.assembleFile();
			assembler.printLog(System.out);
			assembler.clearLog();

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
