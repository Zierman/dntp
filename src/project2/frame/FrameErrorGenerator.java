package project2.frame;

import java.util.Random;

public class FrameErrorGenerator
{
	private static final Random RAND = new Random();

	public static Frame.Error generateError(int chanceOfError) throws Exception
	{
		float rand = RAND.nextFloat();
		float errChance = (float) (chanceOfError / 100.0);
		Frame.Error error = Frame.Error.NONE;
		
		
		if (rand < errChance)
		{
			switch (RAND.nextInt(2))
			{
			case 0:
				error = Frame.Error.DROP;
				break;
			case 1:
				error = Frame.Error.CORRUPT;
				break;

			default:
				throw new Exception();
			}
		}
		return error;

	}
}
