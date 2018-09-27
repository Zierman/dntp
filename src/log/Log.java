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
		printStream.print(log);
	}

	/** Gets the String representation of a byte array
	 * @param b byte array that will be represented in the string
	 * @return a String that represents the byte array
	 */
	public static String getString(byte[] b)
	{
		return getHexString(b);
	}

	/** Gets the String representation of a byte array in Hex form
	 * @param b byte array that will be represented in the string
	 * @return a String that represents the byte array in Hex form
	 */
	public static String getHexString(byte[] b)
	{
		String s = "";
		boolean first = true;
		for(byte tmp : b )
		{
			if(!first)
			{
				s += ", ";
			}
			String tmpString = Integer.toHexString(tmp).toUpperCase();
			s += "x";
			for(int i = tmpString.length(); i < 2; i++)
			{
				s += "0";
			}
			s += tmpString;
			first = false;
		}
		return s;
	}

	/** Gets the String representation of a byte array in integer form
	 * @param b byte array that will be represented in the string
	 * @return a String that represents the byte array in integer form
	 */
	public static String getIntegerString(byte[] b)
	{
		String s = "";
		boolean first = true;
		for(byte tmp : b )
		{
			if(!first)
			{
				s += ", ";
			}
			s += Integer.toString(tmp);
			first = false;
		}
		return s;
	}
	
	/** Gets the String representation of a byte array in binary form
	 * @param b byte array that will be represented in the string
	 * @return a String that represents the byte array in bynary form
	 */
	public static String getBinaryString(byte[] b)
	{
		String s = "";
		boolean first = true;
		for(byte tmp : b )
		{
			if(!first)
			{
				s += ", ";
			}
			String tmpString = Integer.toBinaryString(tmp);
			s += "b";
			for(int i = tmpString.length(); i < 8; i++)
			{
				s += "0";
			}
			s += tmpString;
			first = false;
		}
		return s;
	}
}
