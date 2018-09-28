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
	public static enum ByteDisplayMode {INTEGER, HEX, BINARY}
	private static ByteDisplayMode byteDisplayMode = ByteDisplayMode.HEX;
	
	/**
	 * @return the byteDisplayMode
	 */
	public static ByteDisplayMode getByteDisplayMode()
	{
		return byteDisplayMode;
	}

	/**
	 * @param byteDisplayMode the byteDisplayMode to set
	 */
	public static void setByteDisplayMode(ByteDisplayMode byteDisplayMode)
	{
		Log.byteDisplayMode = byteDisplayMode;
	}


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

	/** Gets the String representation of a byte
	 * @param b byte that will be represented in the string
	 * @return a String that represents the byte array
	 */
	public static String getString(byte b)
	{
		String s = null;
		try
		{
			switch (byteDisplayMode)
			{
			case HEX:
				s = getHexString(b);
				break;
			case BINARY:
				s = getBinaryString(b);
				break;
			case INTEGER:
				s = getIntegerString(b);
				break;
			default:
				throw new Exception();
			}
		} catch (Exception e)
		{
			// this should never happen
			e.printStackTrace();
		}
		return s;
	}
	
	/** Gets the String representation of a byte array
	 * @param bytes byte array that will be represented in the string
	 * @return a String that represents the byte array
	 */
	public static String getString(byte[] bytes)
	{
		String s = "";
		boolean first = true;
		for(byte b : bytes)
		{
			if(first)
			{
				first = false;
			}
			else
			{
				s += ", ";
			}
			s += getString(b);
		}
		
		return s;
	}

	/** Gets the String representation of a byte array in Hex form
	 * @param bytes byte array that will be represented in the string
	 * @return a String that represents the byte array in Hex form
	 */
	public static String getHexString(byte[] bytes)
	{
		String s = "";
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.HEX;
		getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}
	
	/** Gets the String representation of a byte in Hex form
	 * @param b byte that will be represented in the string
	 * @return a String that represents the byte in Hex form
	 */
	public static String getHexString(byte b)
	{
		String s = "x";
		String tmpString = Integer.toHexString(b).toUpperCase();
		for(int i = tmpString.length(); i < 2; i++)
		{
			s += "0";
		}
		s += tmpString;
		
		return s;
	}

	/** Gets the String representation of a byte array in integer form
	 * @param bytes byte array that will be represented in the string
	 * @return a String that represents the byte array in integer form
	 */
	public static String getIntegerString(byte[] bytes)
	{
		String s = "";
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.INTEGER;
		getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}
	
	/** Gets the String representation of a byte in integer form
	 * @param b byte that will be represented in the string
	 * @return a String that represents the byte in integer form
	 */
	public static String getIntegerString(byte b)
	{
		return Integer.toString(b);
	}
	
	/** Gets the String representation of a byte array in binary form
	 * @param bytes byte array that will be represented in the string
	 * @return a String that represents the byte array in binary form
	 */
	public static String getBinaryString(byte[] bytes)
	{
		String s = "";
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.BINARY;
		getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}
	
	/** Gets the String representation of a byte in binary form
	 * @param b byte that will be represented in the string
	 * @return a String that represents the byte in binary form
	 */
	public static String getBinaryString(byte b)
	{

		String s = "b";
		String tmpString = Integer.toBinaryString(b);
		
		for(int i = tmpString.length(); i < 8; i++)
		{
			s += "0";
		}
		s += tmpString;
		
		return s;
	}
	
	/** Logged bytes will be stored in Hex form after this is called
	 * 
	 */
	public static void setDisplayModeHex()
	{
		byteDisplayMode = ByteDisplayMode.HEX;
	}


	/** Logged bytes will be stored in Binary form after this is called
	 * 
	 */
	public static void setDisplayModeBinary()
	{
		byteDisplayMode = ByteDisplayMode.BINARY;
	}


	/** Logged bytes will be stored in Integer form after this is called
	 * 
	 */
	public static void setDisplayModeInteger()
	{
		byteDisplayMode = ByteDisplayMode.INTEGER;
	}
}
