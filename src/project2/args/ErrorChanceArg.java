package project2.args;

import project2.Defaults;

/**
 * command-line argument for giving the percent chance of error when introducing
 * artificial errors
 *
 */
public class ErrorChanceArg extends Arg<Integer>
{
	private final static String HELP_MSG = "Percent Chance of an error being generated.";

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public ErrorChanceArg(String flag)
	{
		super(flag);
	}

	@Override
	protected Integer getDefault()
	{

		return Defaults.CHANCE_OF_ERROR;
	}

	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = Integer.parseInt(s);
	}
}
