package project2.args;

/**
 * command-line argument for artificially introducing errors
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class IntroduceErrorArg extends ToggleArg
{

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public IntroduceErrorArg(String flag)
	{
		super(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "Use to introduce artificial errors into transmission";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.ToggleArg#processInlineArg()
	 */
	@Override
	protected void processInlineArg()
	{
		// don't do anything
	}
}
