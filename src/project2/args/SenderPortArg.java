/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class SenderPortArg extends PortArg
{
	private final static String CLIENT_NAME = "sender";

	/**
	 * @param flag
	 */
	public SenderPortArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.PortArg#getClientName()
	 */
	@Override
	protected String getClientName()
	{
		return CLIENT_NAME;
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	public Integer getDefault()
	{
		return project2.Defaluts.SENDER_PORT;
	}

}
