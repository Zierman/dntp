/**
 * File Created by Joshua Zierman on Oct 2, 2018
 */
package project2;

import java.io.File;
import java.net.InetAddress;
import project1.Project1;

/** Default constants for program 2
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class Defaults
{
	// Project Details
	public static final String PROJECT_TITLE = "Darn Nice Transfer Protocol: Project 2";
	public static final String COURSE_STRING = "ICS 460-01";	
	public static final String[] DESIGNERS = {"David Whitebird","Joshua Zierman","Travis Peterson"};
	
	
	// Chunk and packet length
	public static final short MAX_CHUNK_LENGTH = 500;
	public static final short ACK_PACKET_LENGTH = 8;
	
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
	public static final int TIMEOUT = 2000;
	
	// maximum delay for delayed packets in ms
	public static final Integer MAX_DELAY = 10000;
	
	// size of the sliding window
	public final static int WINDOW_SIZE = 4;
	
	// filename and path
	public static final File INPUT_FILE = new File("in.txt");
	public static final String COPY_SUFFIX = "_copy";
	public static final Integer NUMBER_OF_ACK_NUMBERS = 2; // must be > 1

	// logging
	public static final Boolean REQUIRED_LOGGING = true;
	public static final Integer STARTING_DELAY = 1000;
}
