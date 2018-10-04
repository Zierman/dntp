/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.net.UnknownHostException;

import log.Log;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public abstract class Arg <T>
{
	String inlineFlag;
	T value;
	
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
	
	public boolean matchesFlag(String flag)
	{
		return flag.matches(inlineFlag);
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void setValue(T newValue)
	{
		value = newValue;
	}
	
	/**Processes the inline argument
	 * @param s the String to be processed
	 * @throws Exception if any exception happens when processing the inline argument
	 */
	public abstract void processInlineArg(String s) throws Exception;
	
	public abstract T getDefault();

	/**
	 * @param e
	 */
	public void printErr(String arg, Exception e)
	{
		System.err.println(this.getClass().getName() + " failed to process " + inlineFlag + " " + arg);
	}
	
	/**Gets the help string that can be displayed for the argument 
	 * @return
	 */
	protected abstract String getHelpString();
	
	public String getHelpLine()
	{
		String argName = getClass().getSimpleName().replace("Arg", "");
		
		// get the needed white space to format nicely
		String ws = "";
		for(int i = argName.length(); i < ArgList.getMaxArgNameLength(); i++)
		{
			ws += " ";
		}
		
		return inlineFlag + Log.TAB + argName + ws + Log.TAB + getHelpString();
	}

	
	
}
