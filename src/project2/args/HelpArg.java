/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.io.PrintStream;

/** command-line argument for showing the help output
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class HelpArg extends ToggleArg
{


	private final String PROGRAM_TITLE;
	private final String PROGRAM_DESCRIPTION;
	private static final String[] DESIGNERS = project2.Defaults.DESIGNERS;
	private static final PrintStream out = System.out;

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 * @param title title of the program
	 * @param description of the program
	 */
	public HelpArg(String flag, String title, String description) {
		super(flag);
		this.PROGRAM_TITLE = title;
		this.PROGRAM_DESCRIPTION = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "Displays help information";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see project2.args.ToggleArg#processInlineArg()
	 */
	@Override
	protected void processInlineArg()
	{
		
		//print the project title
		out.println(project2.Defaults.PROJECT_TITLE);
		out.println("");
		
		//Show the course the project was assinged in
		out.println("Assinged in " + project2.Defaults.COURSE_STRING);
		out.println("");
		
		//print the designers
		out.print("Desinged by ");
		for(int i = 0; i < DESIGNERS.length; i++)
		{
			if(i!=0)
			{
				out.print(", ");
			}
			if(i == DESIGNERS.length - 1)
			{
				out.print("and ");
			}
			out.print(DESIGNERS[i]);
		}
		out.println(".");
		out.println("");
		
		out.println(PROGRAM_TITLE);
		out.println("");
		
		// print the Program Description
		out.println(PROGRAM_DESCRIPTION);
		out.println("");
		
		// Print the list of argumetn flags and info about them
		out.println("list of inline arguments:");
		for (Arg<?> arg : ArgList.instance())
		{
			out.println(arg.getHelpLine());
		}
		System.exit(0);
	}
}
