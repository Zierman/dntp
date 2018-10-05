/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.io.File;
import java.net.InetAddress;

import project1.Project1;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Defaluts
{
	// Chunk and packet length
	public static final int MAX_CHUNK_LENGTH = 500;
	public static final int DATA_PACKET_LENGTH = MAX_CHUNK_LENGTH + 12;
	public static final int ACK_PACKET_LENGTH = 8;
	
	// Addresses
	public static final InetAddress SENDER_ADDRESS = project1.Project1.getDestinationIp();
	public static final InetAddress PROXY_ADDRESS = project1.Project1.getDestinationIp();
	public static final InetAddress RECEIVER_ADDRESS = project1.Project1.getDestinationIp();
	
	// Ports
	public static final int SENDER_PORT = 1012;
	public static final int PROXY_PORT = 1011;
	public static final int RECEIVER_PORT = Project1.getPort();
	
	// percent chance of error
	public static final int CHANCE_OF_ERROR = 20;
	
	// timeout time in ms
	public final static int TIMEOUT = 2000;
	
	// size of the sliding window
	public final static int WINDOW_SIZE = 4;
	
	// filename and path
	public static final File INPUT_FILE = new File("in.txt");
	public static final String COPY_SUFIX = "_copy";
}
