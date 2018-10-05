/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class HelpArg extends ToggleArg
{

	private static final String DESIGNERS = "David Whitebird and Joshua Zierman with the help of Travis Peterson";
	private final String DESCRIPTION;

	/**
	 * @param flag
	 */
	public HelpArg(String flag, String description) {
		super(flag);
		this.DESCRIPTION = description;
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
		System.out.println("Darn Nice Transfer Protocol: File Tranfer");
		System.out.println("");
		System.out.println("Desinged by " + DESIGNERS);
		System.out.println("");
		System.out.println(DESCRIPTION);
		System.out.println("");
		System.out.println("list of inline arguments:");
		for (Arg<?> arg : ArgList.instance())
		{
			System.out.println(arg.getHelpLine());
		}
		System.exit(0);
	}
}
