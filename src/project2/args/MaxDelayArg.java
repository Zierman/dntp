package project2.args;

import project2.Defaults;

/**
 * command-line argument for the max delay when artificially delaying frames
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class MaxDelayArg extends Arg<Integer>
{

	private static final String HELP_MSG = "The maximum time a packet will be delayed in ms.";

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public MaxDelayArg(String flag)
	{
		super(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Integer getDefault()
	{
		return Defaults.MAX_DELAY;
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
		value = Integer.parseInt(s);
	}

}
