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
public class Printer
{
	private DebugModeArg dbma;
	private PrintStream normalPrintStream, debugPrintStream;
	
	
	public Printer(DebugModeArg arg, PrintStream normalPrintStream, PrintStream debugPrintStream)
	{
		dbma = arg;
		this.normalPrintStream = normalPrintStream;
		this.debugPrintStream = debugPrintStream;
	}
	
	public void print(String s)
	{
		normalPrintStream.print(s);
	}
	
	public void println(String s)
	{
		normalPrintStream.println(s);
	}
	
	public void debug(String s)
	{
		debugPrintStream.print(s);
	}
	
	public void debugln(String s)
	{
		debugPrintStream.println(s);
	}
}
