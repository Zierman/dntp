/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * command-line argument for a port
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class PortArg extends Arg<Integer>
{

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public PortArg(String flag)
	{
		super(flag);
	}

	protected abstract String getClientName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "The " + getClientName() + "'s port number";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = Integer.parseInt(s);
	}

}
