/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

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
	protected abstract void processInlineArg(String s) throws Exception;
	
	protected abstract T getDefault();

	/**
	 * @param e
	 */
	protected void printErr(String arg, Exception e)
	{
		System.err.println(this.getClass().getName() + " failed to process " + inlineFlag + " " + arg);
	}
	
	/**Gets the help string that can be displayed for the argument 
	 * @return
	 */
	protected abstract String getHelpString();
	
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
