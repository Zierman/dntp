package project2;

import java.io.PrintStream;
import java.util.Date;
import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.Frame;

/** A printer for the required output
 * @author Joshua Zierman [py1422xs@metrostate.edu]
 *
 */
public class LogPrinter extends Printer
{
	private Long startTime;

	/** Constructs a LogPrinter
	 * @param printerIsOn the starting state of this printer
	 * @param logPrintStream the print stream that this printer will output to
	 * @param startTime
	 */
	public LogPrinter(Boolean printerIsOn, PrintStream logPrintStream, Long startTime)
	{
		super(printerIsOn, logPrintStream);
		this.startTime = startTime;
	}

	/** Logs sent ChunkFrame
	 * @param f ChunkFrame sent to be logged
	 * @param startOffset the starting offset of the data
	 * @param endOffset the ending offset of the data
	 */
	public void sent(ChunkFrame f, long startOffset, long endOffset)
	{
		if (printerIsOn)
		{
			long start = startOffset;
			long end = endOffset;
			println("SENDing " + f.getSequenceNumber() + " " + start + ":" + end + " " + time() + " " + sendErr(f));
		}
	}

	/** Logs resent ChunkFrame
	 * @param f ChunkFrame resent to be logged
	 * @param startOffset the starting offset of the data
	 * @param endOffset the ending offset of the data
	 */
	public void resent(ChunkFrame f, long startOffset, long endOffset)
	{
		if (printerIsOn)
		{
			long start = startOffset;
			long end = endOffset;
			println("ReSend. " + f.getSequenceNumber() + " " + start + ":" + end + " " + time() + " " + sendErr(f));
		}
	}

	/** Logs sent EndFrame
	 * @param f EndFrame sent to be logged
	 * @param digitLengthOfOffset the length of the largest offset in digits
	 */
	public void sent(EndFrame f, int digitLengthOfOffset)
	{
		if (printerIsOn)
		{
			String s = "";
			for (int i = 0; i < digitLengthOfOffset; i++)
			{
				s += "-";
			}
			println("SENDing " + f.getSequenceNumber() + " " + s + ":" + s + " " + time() + " " + sendErr(f));
		}
	}
	
	/** Logs resent EndFrame
	 * @param f EndFrame resent to be logged
	 * @param digitLengthOfOffset the length of the largest offset in digits
	 */
	public void resent(EndFrame f,  int digitLengthOfOffset)
	{
		if (printerIsOn)
		{
			String s = "";
			for (int i = 0; i < digitLengthOfOffset; i++)
			{
				s += "-";
			}
			println("ReSend " + f.getSequenceNumber() + " " + s + ":" + s + " " + time() + " " + sendErr(f));
		}
	}

	/** Logs sent AckFrame
	 * @param f AckFrame sent to be logged
	 * @param chunkSequenceNumber sequence number of the linked ChunkFrame
	 */
	public void sent(AckFrame f, int chunkSequenceNumber)
	{
		if (printerIsOn)
		{
			println("SENDing ACK " + chunkSequenceNumber + " " + time() + " " + sendErr(f));
		}
	}

	/** Logs resent AckFrame
	 * @param f AckFrame resent to be logged
	 * @param chunkSequenceNumber sequence number of the linked ChunkFrame
	 */
	public void resent(AckFrame f, int chunkSequenceNumber)
	{
		if (printerIsOn)
		{
			println("ReSend. ACK " + chunkSequenceNumber + " " + time() + " " + sendErr(f));

		}
	}

	public void chunkReceived(ChunkFrame f, int expectedSequenceNumber)
	{
		if (printerIsOn)
		{
			// if there is an error we output differently
			if (f.failedCheckSum())
			{
				println("RECV " + f.getSequenceNumber() + " " + "CRPT");
			}
			else if (f.getSequenceNumber() != expectedSequenceNumber)
			{
				if (f.getSequenceNumber() < expectedSequenceNumber)
				{
					// duplicate

					println("DUPL " + time() + " " + f.getSequenceNumber() + " " + "!Seq");
				}
				else
				{
					println("RECV " + time() + " " + f.getSequenceNumber() + " " + "!Seq"); 
				}
			}
			// if there is no error we move window because this is stop and wait
			else
			{
				println("RECV " + time() + " " + f.getSequenceNumber() + " " + "RECV");
			}

		}
	}

	public void ackReceived(AckFrame ackFrame, int expectedAckNumber, int sequenceNumber)
	{
		if (printerIsOn)
		{
			try
			{
				// if the ack matches we have to assume that the ack was for the
				// current chunkFrame
				if (ackFrame.getAckNumber() == expectedAckNumber)
				{
					// if there is an error we output differently
					if (ackFrame.failedCheckSum())
					{
						println("AckRcvd " + sequenceNumber + " " + "ErrAck.");
					}
					// if there is no error we move window because this is stop
					// and wait
					else
					{
						println("AckRcvd " + sequenceNumber + " " + "MoveWnd");
					}
				}
				// if the ack number does not match what was expected we assume
				// that it was for the previously received chunkFrame
				else
				{
					int machedSequenceNumber = sequenceNumber - 1; // TODO This
																	// will need
																	// to change
																	// if window
																	// size is
																	// not fixed
																	// at 1

					// if the matched sequence number is negitive we throw an
					// exception
					if (machedSequenceNumber < 0)
					{
						throw new Exception("ack mismatch corrilates to negitive sequence number");
					}

					// we print our output for a duplicate ack
					println("AckRcvd " + machedSequenceNumber + " " + "DuplAck");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private String sendErr(Frame f)
	{
		String s;

		switch (f.getError())
		{
		case CORRUPT:
			s = "ERRR";
			break;

		case DROP:
			s = "DROP";
			break;

		default:
			s = "SENT";
			break;
		}

		return s;
	}

	private Long time()
	{
		return new Date().getTime() - startTime;
	}
	public void timeout(ChunkFrame f)
	{
		if (printerIsOn)
		{
			println("TimeOut " + f.getSequenceNumber());
		}
	}
}