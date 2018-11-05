package project2.args;


public class IntroduceErrorArg extends ToggleArg
{

    public IntroduceErrorArg(String flag)
    {
        super(flag);
    }

    @Override
    protected void processInlineArg()
    {
        //don't do anything
    }

    @Override
    protected String getHelpString()
    {
        return "Use to introduce artificial errors into transmission";
    }
}
