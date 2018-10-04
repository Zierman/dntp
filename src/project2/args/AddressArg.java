/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class AddressArg extends Arg<InetAddress>
{

	/**
	 * @param flag
	 */
	public AddressArg(String flag) {
		super(flag);
		
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	public void processInlineArg(String s) throws IllegalArgumentException, UnknownHostException
	{
		value = InetAddress.getByName(s);
	}


	/* (non-Javadoc)
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "The " + getClientName() + "'s address";
	}
	
	protected abstract String getClientName();

}
