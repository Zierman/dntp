/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class AckFrame extends Frame
{
	public AckFrame(ChunkFrame f)
	{
		super(f.getFrameNumber());
	}
	
	public AckFrame(DatagramPacket p)
	{
		//TODO
	}

	/* (non-Javadoc)
	 * @see project2.Frame#toDatagramPacket(java.net.InetAddress, int)
	 */
	@Override
	public DatagramPacket toDatagramPacket(InetAddress address, int port)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
