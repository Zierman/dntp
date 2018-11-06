package project2.frame;

import project1.Chunk;

/**
 * A special ChunkFrame that contains no data that is used to flag end of
 * transmission
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class EndFrame extends ChunkFrame
{
	/**
	 * Creates a new EndFrame with the specified sequenceNumber
	 * 
	 * @param sequenceNumber
	 *            the integer sequence number of the ChunkFrame
	 */
	public EndFrame(int sequenceNumber)
	{
		super(new Chunk(new byte[0], 0), sequenceNumber);
	}

}
