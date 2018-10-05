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
	private DebugModeArg debugArg;
	private PrintStream normalPrintStream, debugPrintStream;
	
	
	public DebugPrinter(DebugModeArg arg, PrintStream debugPrintStream)
	{
		debugArg = arg;
		this.debugPrintStream = debugPrintStream;
	}
	
	public void print(String s)
	{
		if(debugArg.getValue())
			debugPrintStream.print(s);
	}
	
	public void println(String s)
	{
		if(debugArg.getValue())
			debugPrintStream.println(s);
	}
}
