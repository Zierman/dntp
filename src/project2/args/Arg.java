/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import log.Log;

/** Argument for a program
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Arg <T>
{
	String inlineFlag;
	T value;
	
	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public Arg(String flag)
	{
		inlineFlag = flag;
		setToDefault();
		ArgList.addArg(this);
	}
	
	/**Sets the value to the default (found in the Defaults class)
	 * 
	 */
	public void setToDefault()
	{
		value = getDefault();
	}

	/**Gets the inline flag for the argument
	 * @return String inline argument that precedes the value inline
	 */
	public String getFlag()
	{
		return inlineFlag;
	}
	
	
	/**Checks to see if the inlineFlag matches
	 * @param flag the string to check against the arugment's flag 
	 * @return true if the two match
	 */
	public boolean matchesFlag(String flag)
	{
		return flag.matches(inlineFlag);
	}
	
	/** gets the value held by the argument
	 * @return the T object that was set or the default value if it was not set explicitly of an argument
	 */
	public T getValue()
	{
		return value;
	}
	
	/** Sets the value to a new value
	 * @param newValue the T object to set the value to
	 */
	public void setValue(T newValue)
	{
		value = newValue;
	}
	
	/**Processes the inline argument
	 * @param s the String to be processed
	 * @throws Exception if any exception happens when processing the inline argument
	 */
	protected abstract void processInlineArg(String s) throws Exception;
	
	/** Gets the default value of the armument
	 * @return a T object that is the default value
	 */
	protected abstract T getDefault();

	
	/**Gets the help string that can be displayed for the argument 
	 * @return String of helpful text to be displayed by the help argument case.
	 */
	protected abstract String getHelpString();
	
	
	/** Gets the line of help text for this argument
	 * @return The String of help text for this argument
	 */
	protected String getHelpLine()
	{
		String argName = getClass().getSimpleName().replace("Arg", "");
		
		// get the needed white space to format nicely
		String ws1 = "";
		for(int i = getFlag().length(); i < ArgList.getMaxArgFlagLength(); i++)
		{
			ws1 += " ";
		}
		String ws2 = "";
		for(int i = argName.length(); i < ArgList.getMaxArgNameLength(); i++)
		{
			ws2 += " ";
		}
		
		return inlineFlag + ws1 + Log.TAB + argName + ws2 + Log.TAB + getHelpString();
	}

	
	
}
