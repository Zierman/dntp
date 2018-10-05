/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;


/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class HelpArg extends Arg<Boolean>
{

	/**
	 * @param flag
	 */
	public HelpArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s) throws Exception
	{
		value = true;
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getDefault()
	 */
	@Override
	protected Boolean getDefault()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see project2.args.Arg#getHelpString()
	 */
	@Override
	protected String getHelpString()
	{
		return "Displays help information";
	}

	public void handle()
	{
		if (value == true)
		{
			System.out.println("Darn Nice Transfer Protocol: File Tranfer");
			System.out.println("");
			System.out.println("Desinged by David Whitebird and Joshua Zierman");
			System.out.println("");
			System.out.println("list of inline arguments:");
			for(Arg<?> arg : ArgList.instance())
			{
				System.out.println(arg.getHelpLine());
			}
			System.exit(0);
		}
	}
}
