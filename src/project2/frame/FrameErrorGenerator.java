package project2.frame;

import java.util.Random;

public class FrameErrorGenerator
{
	private static final Random RAND = new Random();

	public static Frame.Error generateError(int chanceOfError) throws Exception
	{
		Frame.Error error = null;
		if (RAND.nextFloat() < chanceOfError / 100)
		{
			switch (RAND.nextInt(3))
			{
			case 0:
				error = Frame.Error.DROP;
				break;
			case 1:
				error = Frame.Error.DELAY;
				break;
			case 2:
				error = Frame.Error.CORRUPT;
				break;

			default:
				throw new Exception();
			}
		}
		return error;

	}
}
