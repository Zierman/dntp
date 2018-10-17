/**
 * File Created by Joshua Zierman on Oct 5, 2018
 */
package project2.args;


public class RequiredLogArg extends ToggleArg
{

    /**
     * @param flag
     */
    public RequiredLogArg(String flag) {
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
