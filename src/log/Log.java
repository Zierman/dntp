/**
 * File Created by Joshua Zierman on Sep 24, 2018
 */
package log;

import java.io.PrintStream;

/**
 * A Log that can be used with Loggable
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Log
{
	public static enum ByteDisplayMode
	{
		INTEGER, HEX, BINARY
	}

	private static ByteDisplayMode byteDisplayMode = ByteDisplayMode.HEX;

	/**
	 * The newline string for the system
	 *
	 */
	private final static String NEWLINE = System.lineSeparator();

	public static final String TAB = " \t";

	/**
	 * The string holding the logStr
	 *
	 */
	private String logStr = "";

	/**
	 * Gets the String representation of a byte in binary form
	 * 
	 * @param b
	 *            byte that will be represented in the string
	 * @return a String that represents the byte in binary form
	 */
	public static String getBinaryString(byte b)
	{

		String s = "b";
		String tmpString = Integer.toBinaryString(Byte.toUnsignedInt(b));

		for (int i = tmpString.length(); i < 8; i++)
		{
			s += "0";
		}
		s += tmpString;

		return s;
	}

	/**
	 * Gets the String representation of a byte array in binary form
	 * 
	 * @param bytes
	 *            byte array that will be represented in the string
	 * @return a String that represents the byte array in binary form
	 */
	public static String getBinaryString(byte[] bytes)
	{
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.BINARY;
		String s = getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}

	/**
	 * @return the byteDisplayMode
	 */
	public static ByteDisplayMode getByteDisplayMode()
	{
		return byteDisplayMode;
	}

	/**
	 * Gets the String representation of a byte in Hex form
	 * 
	 * @param b
	 *            byte that will be represented in the string
	 * @return a String that represents the byte in Hex form
	 */
	public static String getHexString(byte b)
	{
		String s = "x";
		String tmpString = Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase();
		for (int i = tmpString.length(); i < 2; i++)
		{
			s += "0";
		}
		s += tmpString;

		return s;
	}

	/**
	 * Gets the String representation of a byte array in Hex form
	 * 
	 * @param bytes
	 *            byte array that will be represented in the string
	 * @return a String that represents the byte array in Hex form
	 */
	public static String getHexString(byte[] bytes)
	{
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.HEX;
		String s = getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}

	/**
	 * Gets the String representation of a byte in integer form
	 * 
	 * @param b
	 *            byte that will be represented in the string
	 * @return a String that represents the byte in integer form
	 */
	public static String getIntegerString(byte b)
	{
		return Integer.toString(Byte.toUnsignedInt(b));
	}

	/**
	 * Gets the String representation of a byte array in integer form
	 * 
	 * @param bytes
	 *            byte array that will be represented in the string
	 * @return a String that represents the byte array in integer form
	 */
	public static String getIntegerString(byte[] bytes)
	{
		ByteDisplayMode tmp = byteDisplayMode;
		byteDisplayMode = ByteDisplayMode.INTEGER;
		String s = getString(bytes);
		byteDisplayMode = tmp;
		return s;
	}

	/**
	 * Gets the String representation of a byte
	 * 
	 * @param b
	 *            byte that will be represented in the string
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
		}
		catch (Exception e)
		{
			// this should never happen
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Gets the String representation of a byte array
	 * 
	 * @param bytes
	 *            byte array that will be represented in the string
	 * @return a String that represents the byte array
	 */
	public static String getString(byte[] bytes)
	{
		String s = "";
		boolean first = true;
		for (byte b : bytes)
		{
			if (first)
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

	/**
	 * @param byteDisplayMode
	 *            the byteDisplayMode to set
	 */
	public static void setByteDisplayMode(ByteDisplayMode byteDisplayMode)
	{
		Log.byteDisplayMode = byteDisplayMode;
	}

	/**
	 * Logged bytes will be stored in Binary form after this is called
	 *
	 */
	public static void setDisplayModeBinary()
	{
		byteDisplayMode = ByteDisplayMode.BINARY;
	}

	/**
	 * Logged bytes will be stored in Hex form after this is called
	 *
	 */
	public static void setDisplayModeHex()
	{
		byteDisplayMode = ByteDisplayMode.HEX;
	}

	/**
	 * Logged bytes will be stored in Integer form after this is called
	 *
	 */
	public static void setDisplayModeInteger()
	{
		byteDisplayMode = ByteDisplayMode.INTEGER;
	}

	/**
	 * Absorbs another log
	 * 
	 * @param otherLog
	 *            the other log to be absorbed
	 */
	public void absorb(Log otherLog)
	{
		logStr += otherLog.toString();
		otherLog.clear();
	}

	/**
	 * Adds a string to the logStr
	 * 
	 * @param s
	 *            the string to add to logStr
	 */
	public void add(String s)
	{
		logStr += s;
	}

	/**
	 * Adds a string to the logStr followed by a newline
	 * 
	 * @param s
	 *            the string to add to logStr
	 */
	public void addLine(String s)
	{
		logStr += s + NEWLINE;
	}

	/**
	 * Clears the logStr
	 *
	 */
	public void clear()
	{
		logStr = "";
	}

	/**
	 * Prints the logStr
	 * 
	 * @param printStream
	 *            the PrintStream to print the logStr to.
	 */
	public void print(PrintStream printStream)
	{
		printStream.print(logStr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return logStr;
	}
}
