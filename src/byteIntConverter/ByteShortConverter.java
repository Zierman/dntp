package byteIntConverter;

/** A tool that allows for easy conversion from int to byte[] and byte[] to int
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ByteShortConverter
{
	/** converts from byte to int
	 * @param b byte to be converted
	 * @return integer value of the byte
	 */
	public static int convert(byte b)
	{
		return b & 0xff;
	}
	
	/** converts from int to byte[]
	 * @param integer the integer to be converted
	 * @return byte array converted from the int where the lowest significant byte is in stored at index 0
	 */
	public static byte[] convert(short short0)
	{
		byte[] b = new byte[2];
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (byte)((short0 >>> i * 8) & 0xff);
		}
		return b;
	}
	
	/** converts from a byte array to an int
	 * @param bytes a byte array with the least significant byte stored at index 0
	 * @return an int that was stored in the bytes.
	 */
	public static short convert(byte[] bytes)
	{
		if(bytes.length > 2)
		{
			throw new IllegalArgumentException();
		}
		short val = 0;
		for(int i = 0; i < bytes.length; i++)
		{
			val |= (bytes[i] << (i * 8)) & (0xff << (i * 8));
		}
		return val;
	}
}
