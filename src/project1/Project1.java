package project1;

import java.net.InetAddress;
import java.net.UnknownHostException;

/** Contains constants for the project
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Project1
{
	private final static int DESTINATION_PORT = 2010;
	private static final String INPUT_FILENAME = "in.txt";
	private static final String OUTPUT_FILENAME = "out.txt";
	private static final int BYTES_PER_CHUNK = 4;
	
	/** gets the destination Address
	 * @return the InetAddress of the destination
	 */
	public static InetAddress getDestinationIp()
	{
		InetAddress address = null;
		try
		{
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e)
		{
			// program fails
			e.printStackTrace();
		}
		return address;
	}

	/** gets the port
	 * @return the port
	 */
	public static int getPort()
	{
		return DESTINATION_PORT;
	}

	/** gets the output filename
	 * @return the outputFilename
	 */
	public static String getOutputFilename()
	{
		return OUTPUT_FILENAME;
	}

	/** gets the input filename
	 * @return the inputFilename
	 */
	public static String getInputFilename()
	{
		return INPUT_FILENAME;
	}

	/**gets the number of bytes per chunk
	 * @return the bytesPerChunk
	 */
	public static int getBytesPerChunk()
	{
		return BYTES_PER_CHUNK;
	}
}
