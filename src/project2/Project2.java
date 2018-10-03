/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import project1.Project1;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Project2
{
	public static final int PROXY_PORT = 1011;
	public static final int MAX_PACKET_LENGTH = 20000;
	public static final int SENDER_PORT = 1012;
	public static final int RECEIVER_PORT = Project1.getPort();
	final int TIMEOUT = 2000; // 2000ms
}
