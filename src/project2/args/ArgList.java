/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.util.Iterator;
import java.util.LinkedList;

/** List of Arguments
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class ArgList implements Iterable<Arg<?>>
{
	private final static LinkedList<Arg<?>> list = new LinkedList<Arg<?>>();
	private final static ArgList argList = new ArgList(0);
	private static int maxArgNameLength = 0;
	private static int maxArgFlagLength = 0;
	
	
	/**
	 * Private Constructor
	 * @param i int that surves no purpous other than giving a different signature from public constructor
	 */
	private ArgList(int i)
	{
		
	}
	
	/** public constructor
	 * @throws Exception if called, use ArgList.instance() to get the instance of this singleton class
	 */
	public ArgList() throws Exception
	{
		throw new Exception("please use ArgList.instance()");
	}
	
	/** gets the instance of this class
	 * @return the ArgList instance
	 */
	public static ArgList instance()
	{
		return argList;
	}
	
	/** adds an Arg to the list
	 * @param arg the Arg to add to the list
	 */
	public static void addArg(Arg<?> arg)
	{
		list.add(arg);
		
		// set max name length
		int tmp = arg.getClass().getSimpleName().replace("Arg", "").length();
		if(tmp > maxArgNameLength)
		{
			maxArgNameLength = tmp;
		}
		
		// set max flag length
		tmp = arg.getFlag().length();
		if(tmp > maxArgFlagLength)
		{
			maxArgFlagLength = tmp;
		}
	}
	
	/** Updates from a string array as passed in by a main function
	 * @param args String[] of arguments passed by main()
	 * @throws Exception if there are any issues processing any arguments
	 */
	public static void updateFromMainArgs(String[] args) throws Exception
	{
		for(int i = 0; i < args.length; i++) // for each String argument in args
		{
			for(Arg<?> a : argList) // for each Arg in argList
			{
				if(a.matchesFlag(args[i])) // if the flag maches
				{
					if(a instanceof ToggleArg) // and if a toggle
					{
						((ToggleArg)a).processInlineArg(""); // handle inline of toggle
					}
					else // else (if not a toggle)
					{
						try
						{
							a.processInlineArg(args[i+1]); // attempt to process
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

	/** gets the length of the longest argument name
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

	/** gets the longest argument flag length
	 * @return int representing the length of the longest flag armament
	 */
	public static int getMaxArgFlagLength()
	{
		return maxArgFlagLength ;
	}
}
