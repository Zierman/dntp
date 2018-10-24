package project2;

import java.io.PrintStream;
import project2.args.LogPrintingArg;
import project2.frame.ChunkFrame;


public class LogPrinter
{
	private Boolean logMode;
	private PrintStream logPrintStream;
	
	
	public LogPrinter(LogPrintingArg arg, PrintStream logPrintStream, int maxChunkPakageSize, int ackPackageSize)
	{
		this.logMode = arg.getValue();
		this.logPrintStream = logPrintStream;
	}
	
	public LogPrinter(Boolean logMode, PrintStream logPrintStream)
	{
		this.logMode = logMode;
		this.logPrintStream = logPrintStream;
	}
	
	public void sent(ChunkFrame f)
	{
		println("SENDing " + f.getSequenceNumber() + "OTHERSTUFF");//TODO finish
	}

	public void print(String s)
	{
		if(logMode)
			logPrintStream.print(s);
	}
	
	public void println(String s)
	{
		if(logMode)
			logPrintStream.println(s);
	}
}