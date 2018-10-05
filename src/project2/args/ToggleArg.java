/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class ToggleArg extends Arg<Boolean>
{
	/**
	 * @param flag
	 */
	public ToggleArg(String flag) {
		super(flag);
	}

	protected abstract void processInlineArg();

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = true;
		processInlineArg();
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Boolean getDefault()
	{
		return false;
	}
}
