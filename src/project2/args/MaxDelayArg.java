package project2.args;

import project2.Defaults;

public class MaxDelayArg extends Arg<Integer>
{

	private static final String HELP_MSG = "The maximum time a packet will be delayed in ms.";

	public MaxDelayArg(String flag)
	{
		super(flag);
	}

	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = Integer.parseInt(s);
	}

	@Override
	protected Integer getDefault()
	{
		return Defaults.MAX_DELAY;
	}

	@Override
	protected String getHelpString()
	{
		return HELP_MSG;
	}

}
