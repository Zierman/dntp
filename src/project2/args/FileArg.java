/**
 * File Created by Joshua Zierman on Oct 4, 2018
 */
package project2.args;

import java.io.File;

import project2.Defaluts;

/**
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FileArg extends Arg<File>
{
	private final static String HELP_MSG = "The Filepath of input file.";

	/**
	 * @param flag the String that precedes the filearg value inline
	 */
	public FileArg(String flag) {
		super(flag);
	}

	/* (non-Javadoc)
	 * @see args.Arg#processInlineArg(java.lang.String)
	 */
	@Override
	public void processInlineArg(String s)
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

	/**
	 * @param s
	 * @return
	 */
	private String clean(String s)
	{
		s = s.trim();
		return s;
	}

	/**
	 * @return
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
		
		return isOk;
	}

	/* (non-Javadoc)
	 * @see args.Arg#getDefault()
	 */
	@Override
	public File getDefault()
	{
		return project2.Defaluts.INPUT_FILE;
	}
	
	public File getOutFile()
	{
		if(value == null)
		{
			throw new NullPointerException("FileArg value needs to be set before getOutFile() is called");
		}
		String path = value.getPath();
		int i = path.lastIndexOf(".");
		if (i < 0)
		{
			path = path + project2.Defaluts.COPY_SUFIX;
		}
		else
		{
			String start = path.substring(0, i);
			String end = path.substring(i);
			path = start + project2.Defaluts.COPY_SUFIX + end;
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
