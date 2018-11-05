/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2;

import java.io.PrintStream;

import project2.args.DebugModeArg;

/** A printer that will allow printing only if the printer is on
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class DebugPrinter
{
	private Boolean printerIsOn;
	private PrintStream debugPrintStream;
	
	
	/** Constructs a DebugPrinter object
	 * @param arg DebugModeArg that is linked to this printer
	 * @param debugPrintStream the print stream that is linked to this printer
	 */
	public DebugPrinter(DebugModeArg arg, PrintStream debugPrintStream)
	{
		this.printerIsOn = arg.getValue();
		this.debugPrintStream = debugPrintStream;
	}
	
	/** Constructs a DebugPrinter object
	 * @param printerIsOn a boolean value indcating that the printer is on or off 
	 * @param debugPrintStream
	 */
	public DebugPrinter(Boolean printerIsOn, PrintStream debugPrintStream)
	{
		this.printerIsOn = printerIsOn;
		this.debugPrintStream = debugPrintStream;
	}

	/** Prints a string if printer is on
	 * @param s the string to print
	 */
	public void print(String s)
	{
		if(printerIsOn)
			debugPrintStream.print(s);
	}

	/** Prints a string as a new line if printer is on
	 * @param s the string to print
	 */
	public void println(String s)
	{
		if(printerIsOn)
			debugPrintStream.println(s);
	}
}
