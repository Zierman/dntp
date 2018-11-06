/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;

/** A command-line argument for using a trace printer
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class TracePrinterArg extends ToggleArg
{

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public TracePrinterArg(String flag) {
		super(flag);
	}

	private static final String HELP_MSG = "Prints additional trace output.";


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
		System.out.println("trace mode is on");
	}

}
