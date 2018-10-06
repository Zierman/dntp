/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.slidingWindow;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;

import project2.Defaults;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class SlidingWindow implements Runnable
{
	protected boolean running = false;
	protected abstract void runReceiver();
	
	protected abstract void runSender();
	
	public void run()
	{
		runReceiver();
		runSender();
		running = true;
	}
}
