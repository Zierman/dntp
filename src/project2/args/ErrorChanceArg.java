package project2.args;

import project2.Defaluts;

public class ErrorChanceArg extends Arg <Integer>
{
    private final static String HELP_MSG = "Percent Chance of an error being generated.";

    public ErrorChanceArg (String flag)
    {
        super(flag);
    }

    @Override
    protected void processInlineArg(String s) throws Exception
    {
        value = Integer.parseInt(s);
    }

    @Override
    protected Integer getDefault()
    {

        return Defaluts.CHANCE_OF_ERROR;
    }

    @Override
    protected String getHelpString()
    {
        return HELP_MSG;
    }
}
