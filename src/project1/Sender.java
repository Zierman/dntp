/**
 * File Created by Joshua Zierman on Sep 25, 2018
 */
package project1;

import java.net.InetAddress;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public interface Sender
{
	/**
	 * checks if there is a next value to send
	 * 
	 * @return true if sendNext will not be out of bounds, else false
	 */
	public abstract boolean hasNext();

	/**
	 * sets the index of next to the start
	 *
	 */
	public abstract void resetNextPosition();

	/**
	 * sends the next datagram
	 *
	 */
	public abstract void sendNext();

	/**
	 * sets the sender's destination address
	 * 
	 * @param ip
	 *            the InetAddress of the destination
	 */
	public abstract void setToAddress(InetAddress ip);

	/**
	 * sets the destination port
	 * 
	 * @param port
	 *            the integer of the destination port
	 */
	public abstract void setToPort(int port);
}
