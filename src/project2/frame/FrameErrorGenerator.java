package project2.frame;

import java.util.Random;

/** A Error Generator for introducing artificial errors
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class FrameErrorGenerator
{
	private static final Random RAND = new Random();

	/** Generates an error
	 * @param chanceOfError the int percent chance of error
	 * @return Frame.Error that correlates to a pseudo randomly generated error
	 */
	public static Frame.Error generateError(int chanceOfError)
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
				new Exception().printStackTrace();
			}
		}
		return error;

	}
}
