/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * command-line argument for the receiver port
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ReceiverPortArg extends PortArg
{
	private final static String CLIENT_NAME = "receiver";

	/**
	 * Constructs an instance of this class
	 * 
	 * @param flag
	 *            the string that is a flag in the command line argument
	 */
	public ReceiverPortArg(String flag)
	{
		super(flag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.PortArg#getClientName()
	 */
	@Override
	protected String getClientName()
	{
		return CLIENT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Integer getDefault()
	{
		return project2.Defaults.RECEIVER_PORT;
	}

}
