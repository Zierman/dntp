/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ArgList implements Iterable<Arg<?>>
{
	private final static LinkedList<Arg<?>> list = new LinkedList<Arg<?>>();
	private final static ArgList argList = new ArgList();
	private static int maxArgNameLength = 0;
	
	private static final long serialVersionUID = 5503336292592915469L;
	private ArgList()
	{
		
	}
	
	public static ArgList instance()
	{
		return argList;
	}
	
	public static void addArg(Arg<?> arg)
	{
		list.add(arg);
		int tmp = arg.getClass().getSimpleName().replace("Arg", "").length();
		if(tmp > maxArgNameLength)
		{
			maxArgNameLength = tmp;
		}
	}
	
	public static void updateFromMainArgs(String[] args) throws Exception
	{
		Exception exeption = null;
		for(int i = 0; i < args.length; i++) // for each String argument in args
		{
			for(Arg<?> a : argList) // for each Arg in argList
			{
				if(a.matchesFlag(args[i]))
				{
					if(a instanceof HelpArg)
					{
						a.processInlineArg(null);
						((HelpArg) a).handle();
					}
					else
					{
						try
						{
							a.processInlineArg(args[i+1]);
						}
						catch (Exception e) {
							a.printErr(args[i+1], e);
							throw e;
						}
					}
				}
			}
		}
	}

	/**
	 * @return the mAX_ARG_NAME_LENGTH
	 */
	protected static int getMaxArgNameLength()
	{
		return maxArgNameLength;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Arg<?>> iterator()
	{
		return list.iterator();
	}
}
