package byteNumberConverter;

/** A tool that allows for easy conversion from long to byte[] and byte[] to long
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ByteLongConverter
{
	/** converts from byte to long
	 * @param b byte to be converted
	 * @return long value of the byte
	 */
	public static long convert(byte b)
	{
		return b & 0xff;
	}
	
	/** converts from long to byte[]
	 * @param long the integer to be converted
	 * @return byte array converted from the long where the lowest significant byte is in stored at index 0
	 */
	public static byte[] convert(long longVal)
	{
		byte[] b = new byte[8];
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (byte)((longVal >>> i * 8) & 0xff);
		}
		return b;
	}
	
	/** converts from a byte array to a long
	 * @param bytes a byte array with the least significant byte stored at index 0
	 * @return an int that was stored in the bytes.
	 */
	public static long convert(byte[] bytes)
	{
		if(bytes.length > 8)
		{
			throw new IllegalArgumentException();
		}
		int val = 0;
		for(int i = 0; i < bytes.length; i++)
		{
			val |= (bytes[i] << (i * 8)) & (0xff << (i * 8));
		}
		return val;
	}
}
