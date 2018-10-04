/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class TimeoutArg extends Arg<Integer>
{

	private final static String HELP_MSG = "The amount of time in ms before timeout.";
	
	/**
	 * @param flag
	 */
	public TimeoutArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	public void processInlineArg(String s) throws IllegalArgumentException
	{
		value = Integer.parseInt(s);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	public Integer getDefault()
	{
		return project2.Defaluts.TIMEOUT;
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
