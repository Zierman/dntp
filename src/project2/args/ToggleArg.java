/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;

/**
 * command-line argument for a toggle type argument
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class ToggleArg extends Arg<Boolean>
{
	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public ToggleArg(String flag)
	{
		super(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Boolean getDefault()
	{
		return false;
	}

	/**
	 * Processes the In-line arg
	 *
	 */
	protected abstract void processInlineArg();

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = true;
		processInlineArg();
	}
}
