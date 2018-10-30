/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class DebugModeArg extends ToggleArg
{

	/**
	 * @param flag
	 */
	public DebugModeArg(String flag) {
		super(flag);
	}

	private static final String HELP_MSG = "Prints additional debugging output.";


	/* (non-Javadoc)
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}


	@Override
	protected void processInlineArg()
	{
		System.out.println("Debug mode is on");
	}

}
