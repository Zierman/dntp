/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.printer;

import java.io.PrintStream;

import project2.args.TracePrinterArg;

/**
 * A printer that will allow printing only if the printer is on
 * 
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class TracePrinter extends Printer
{
	/**
	 * Constructs a TracePrinter object
	 * 
	 * @param printerIsOn
	 *            a boolean value indcating that the printer is on or off
	 * @param tracePrintStream
	 *            the printStream of the trace printer
	 */
	public TracePrinter(Boolean printerIsOn, PrintStream tracePrintStream)
	{
		super(printerIsOn, tracePrintStream);
	}

	/**
	 * Constructs a TracePrinter object
	 * 
	 * @param arg
	 *            TracePrinterArg that is linked to this printer
	 * @param tracePrintStream
	 *            the print stream that is linked to this printer
	 */
	public TracePrinter(TracePrinterArg arg, PrintStream tracePrintStream)
	{
		super(arg.getValue(), tracePrintStream);
	}

}
