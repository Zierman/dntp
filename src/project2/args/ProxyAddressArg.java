/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.net.InetAddress;

/** command-line argument for error proxy's address
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ProxyAddressArg extends AddressArg
{

	private final static String CLIENT_NAME = "proxy";
	
	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public ProxyAddressArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.AddressArg#getClientName()
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
	protected InetAddress getDefault()
	{
		return project2.Defaults.PROXY_ADDRESS;
	}

}
