/**
 * File Created by Joshua Zierman on Sep 24, 2018
 */
package log;

import java.io.PrintStream;

/** Interface to allow a object's behavior to be logged for output
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public interface Loggable
{
	/** Gets the Log of the loggable
	 * @return the Log of the loggable
	 */
	public Log getLog();
	
	/** Prints the log
	 * @param printStream the print stream the log will be printed to.
	 */
	public void printLog(PrintStream printStream);
	
	/**	Clears the log
	 * 
	 */
	public void clearLog();
}
