/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.io.File;

/** command-line argument for indicating a file
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileArg extends Arg<File>
{
	private final static String HELP_MSG = "The Filepath of input file.";

	/** Constructs an instance of this class
	 * @param flag the string that is a flag in the command line argument
	 */
	public FileArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	protected void processInlineArg(String s)
	{
		s = clean(s);
		if(isBadArg(s))
		{
			throw new IllegalArgumentException();
		}
		else
		{
			setValue(new File(s));
		}
	}

	/**Cleans the string
	 * @param s the string to be cleaned
	 * @return the clean string
	 */
	private String clean(String s)
	{
		s = s.trim();
		return s;
	}

	/** Checks to see if the string is a bad argument
	 * @param s the String to check
	 * @return true if it is a bad argument
	 */
	private boolean isBadArg(String s)
	{
		boolean isOk = true;
		
		if(s.endsWith("."))
		{
			isOk = false;
		}
		else if(s.length() < 1)
		{
			isOk = false;
		}
		
		return !isOk;
	}

	/* (non-Javadoc)
	 * @see args.Arg#getDefault()
	 */
	@Override
	protected File getDefault()
	{
		return project2.Defaults.INPUT_FILE;
	}
	
	public static File getOutFile(File f)
	{
		if(f == null)
		{
			throw new NullPointerException("FileArg value needs to be set before getOutFile() is called");
		}
		String path = f.getPath();
		int i = path.lastIndexOf(".");
		if (i < 0)
		{
			path = path + project2.Defaults.COPY_SUFFIX;
		}
		else
		{
			String start = path.substring(0, i);
			String end = path.substring(i);
			path = start + project2.Defaults.COPY_SUFFIX + end;
		}
		return new File(path);
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
