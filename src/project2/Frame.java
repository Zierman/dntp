/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Frame
{
	private final Byte FLAG = 0x7E;
	private final Byte ESC = 0x7D;
	public enum Error {DROP, DELAY, CORRUPT}
	protected Error error = null;
	protected Byte frameNumber;
	
	protected Frame(byte frameNumber)
	{
		this.frameNumber = frameNumber;
	}
	
	public Frame()
	{
		
	}
	
	public abstract DatagramPacket toDatagramPacket(InetAddress address, int port);

	public Byte getFrameNumber()
	{
		return frameNumber;
	}
	
	public Error getError()
	{
		return error;
	}
	
	private static Byte toggle(Byte b)
	{
		return (byte) (b ^ 0x20);
	}


	/**
	 * 
	 */
	public void corrupt()
	{
		// TODO Auto-generated method stub
		error = Error.CORRUPT;
	}

}
