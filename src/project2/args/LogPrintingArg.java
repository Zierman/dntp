/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;


/** command-line argument for enabling a printer to print the required output
 *
 */
public class LogPrintingArg extends ToggleArg
{

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
    public LogPrintingArg(String flag) {
        super(flag);
    }

    private static final String HELP_MSG = "Prints required output.";

    /* (non-Javadoc)
     * @see project2.args.ToggleArg#processInlineArg()
     */
    @Override
    protected void processInlineArg()
    {

    }

    /* (non-Javadoc)
     * @see project2.args.Arg#getHelpString()
     */
    @Override
    protected String getHelpString()
    {
        return HELP_MSG;
    }

}
