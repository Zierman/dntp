/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/** command-line argument for a window size
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class WindowSizeArg extends Arg<Integer>
{
	private final static String HELP_MSG = "The size of the sliding window.";

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public WindowSizeArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws IllegalArgumentException
	{
		value = Integer.parseInt(s);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Integer getDefault()
	{
		return project2.Defaults.WINDOW_SIZE;
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

}
