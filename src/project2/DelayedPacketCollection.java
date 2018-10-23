package project2;

import java.net.DatagramPacket;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;


public class DelayedPacketCollection
{
	private class Node implements Comparable<Node>
	{
		DatagramPacket packet;
		Long triggerTime;
		
		Node(DatagramPacket packet, Long triggerTime)
		{
			this.packet = packet;
			this.triggerTime = triggerTime;
		}
		
		@Override
		public int compareTo(Node arg0)
		{
			return this.triggerTime.compareTo(arg0.triggerTime);
		}
	}
	
	private PriorityQueue<Node> queue = new PriorityQueue<>();
	
	public void add(DatagramPacket p, int msDelay)
	{
		long triggerTime = new Date().getTime() + msDelay;
		queue.add(new Node(p, triggerTime));
	}
	
	public Queue<DatagramPacket> getAllReadyPackets()
	{
		PriorityQueue<DatagramPacket> triggered = new PriorityQueue<>();
		while(!queue.isEmpty() && queue.peek().triggerTime.compareTo(new Date().getTime()) > 0)
		{
			triggered.add(queue.remove().packet);
		}
		return triggered;
	}
}
