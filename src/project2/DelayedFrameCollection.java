package project2;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;

import project2.frame.Frame;


/** A collection of delayed frames that can be retrieved only after the trigger time
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 * @param <F> The type of frame collected
 */
public class DelayedFrameCollection <F extends Frame>
{
	private class Node implements Comparable<Node>
	{
		F frame;
		Long triggerTime;
		
		Node(F frame, Long triggerTime)
		{
			this.frame = frame;
			this.triggerTime = triggerTime;
		}
		
		@Override
		public int compareTo(Node arg0)
		{
			return this.triggerTime.compareTo(arg0.triggerTime);
		}
	}
	
	private PriorityQueue<Node> queue = new PriorityQueue<>();
	
	/** Add a frame with a specified delay
	 * @param frame the frame to add to the delayed frame collection
	 * @param msDelay how long to delay the frame in ms
	 */
	public void add(F frame, int msDelay)
	{
		long triggerTime = new Date().getTime() + msDelay;
		queue.add(new Node(frame, triggerTime));
	}
	
	/** Get all ready frames as a queue
	 * @return a Queue of Frames that are ready.
	 */
	public Queue<F> getAllReadyFrames()
	{
		PriorityQueue<F> triggered = new PriorityQueue<>();
		
		// while the queue is not empty and the lowest timed value is greater than the current time
		while(!queue.isEmpty() && queue.peek().triggerTime.compareTo(new Date().getTime()) > 0)
		{
			// add the dequeued frame to the results that will be returned
			triggered.add(queue.poll().frame);
		}
		return triggered;
	}
	
	/** Get the next ready frame
	 * @return a Frame if one is ready or null otherwise
	 */
	public F getNextReadyFrame()
	{
		F frame = null;
		
		// if the queue is not empty and the lowest timed value is greater than the current time
		if(!queue.isEmpty() && queue.peek().triggerTime.compareTo(new Date().getTime()) > 0)
		{
			// set the return Frame to the dequeued frame
			frame = queue.poll().frame;
		}
		
		return frame;
	}
}
