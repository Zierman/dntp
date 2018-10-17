package project2;

import java.io.PrintStream;

import project2.args.RequiredLogArg;


public class RequirementsLog
{
    private RequiredLogArg reqLogArg;
    private PrintStream reqLogPrintStream;


    public RequirementsLog(RequiredLogArg arg, PrintStream reqLogPrintStream)
    {
        reqLogArg = arg;
        this.reqLogPrintStream = reqLogPrintStream;
    }

    public void print(String s)
    {
        if(reqLogArg.getValue())
            reqLogPrintStream.print(s);
    }

    public void println(String s)
    {
        if(reqLogArg.getValue())
            reqLogPrintStream.println(s);
    }
}