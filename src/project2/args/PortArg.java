/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class PortArg extends Arg<Integer>
{

	/**
	 * @param flag
	 */
	public PortArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	public void processInlineArg(String s) throws Exception
	{
		value = Integer.parseInt(s);
	}


	/* (non-Javadoc)
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "The " + getClientName() + "'s port number";
	}
	
	protected abstract String getClientName();

}
