/**
 * File Created by Joshua Zierman on Sep 24, 2018
 */
package log;

import java.io.PrintStream;

/** A Log that can be used with Loggable
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Log
{
	/** The string holding the log
	 * 
	 */
	private String log = "";
	
	/** The newline string for the system
	 * 
	 */
	private final static String NEWLINE = System.lineSeparator();
	
	/** Adds a string to the log followed by a newline
	 * @param s the string to add to log
	 */
	public void addLine(String s)
	{
		log += s + NEWLINE;
	}
	
	/** Adds a string to the log
	 * @param s the string to add to log
	 */
	public void add(String s)
	{
		log += s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return log;
	}
	
	/** Clears the log
	 * 
	 */
	public void clear()
	{
		log = "";
	}
	
	/** Prints the log
	 * @param printStream the PrintStream to print the log to.
	 */
	public void print(PrintStream printStream)
	{
		printStream.println(log);
	}

	public static String getStringFromBytes(byte[] b)
	{
		String s = "";
		boolean first = true;
		for(byte tmp : b )
		{
			if(!first)
			{
				s += ", ";
			}
			s += tmp;
			first = false;
		}
		return s;
	}
}
