/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2;

import java.io.PrintStream;

import project2.args.DebugModeArg;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class DebugPrinter
{
	private Boolean debugMode;
	private PrintStream debugPrintStream;
	
	
	public DebugPrinter(DebugModeArg arg, PrintStream debugPrintStream)
	{
		this.debugMode = arg.getValue();
		this.debugPrintStream = debugPrintStream;
	}
	
	public DebugPrinter(Boolean debugMode, PrintStream debugPrintStream)
	{
		this.debugMode = debugMode;
		this.debugPrintStream = debugPrintStream;
	}

	public void print(String s)
	{
		if(debugMode)
			debugPrintStream.print(s);
	}
	
	public void println(String s)
	{
		if(debugMode)
			debugPrintStream.println(s);
	}
}
