package project2.args;

import project2.Defaults;

/** command-line argument for the starting delay
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class StartingDelayArg extends Arg<Integer>
{
	/** constructs an instance of this object
	 * @param flag
	 */
	public StartingDelayArg(String flag)
	{
		super(flag);
	}

	private static final String HELP_MSG = "The amount of time to delay the start of the data transfer after settings are tranfered";

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		setValue(Integer.parseInt(s));

	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Integer getDefault()
	{
		return Defaults.STARTING_DELAY;
	}

	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

}
