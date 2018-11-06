/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * command-line argument for the max size of chunk
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class MaxSizeOfChunkArg extends Arg<Short>
{

	private static final String HELP_MSG = "The maximum size of a chunk in bytes.";

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public MaxSizeOfChunkArg(String flag)
	{
		super(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Short getDefault()
	{
		return project2.Defaults.MAX_CHUNK_LENGTH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = Short.parseShort(s);

	}

}
