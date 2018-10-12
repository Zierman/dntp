package project2.args;

import project2.Defaults;

public class NumberOfAckNumbersArg extends Arg<Integer>
{
	private static String HELP_MSG = "The number of valid ack numbers. (must be greater than 1)";

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
