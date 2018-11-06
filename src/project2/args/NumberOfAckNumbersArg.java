package project2.args;

import project2.Defaults;

/** command-line argument for the number of ack numbers
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class NumberOfAckNumbersArg extends Arg<Integer>
{
	private static String HELP_MSG = "The number of valid ack numbers. (must be greater than 1)";

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public NumberOfAckNumbersArg(String flag)
	{
		super(flag);
	}

	@Override
	protected void processInlineArg(String s) throws Exception
	{
		int tmp = Integer.parseInt(s);
		if(tmp > 1)
		{
			value = tmp;
		}

	}

	@Override
	protected Integer getDefault()
	{
		return Defaults.NUMBER_OF_ACK_NUMBERS;
	}

	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

}
