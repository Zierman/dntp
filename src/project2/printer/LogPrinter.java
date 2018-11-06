package project2.printer;

import java.io.PrintStream;
import java.util.Date;

import project2.frame.AckFrame;
import project2.frame.ChunkFrame;
import project2.frame.EndFrame;
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

	
	/** Logs that a ChunkFrame was received
	 * @param f the ChunkFrame received
	 * @param expectedSequenceNumber the int expected sequence number
	 */
	public void chunkReceived(ChunkFrame f, int expectedSequenceNumber)
	{
		if (printerIsOn)
		{
			// if there is an error we output differently
			if (f.failedCheckSum())
			{
				println("RECV " + time() + " " + f.getSequenceNumber() + " " + "CRPT");
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

	
	/** Logs that an AckFrame was recieved
	 * @param ackFrame the AckFrame recieved
	 * @param expectedAckNumber the int expected acknowledgement number
	 * @param sequenceNumber the sequence number of the ack
	 */
	public void ackReceived(AckFrame ackFrame, int expectedAckNumber, int sequenceNumber, int numberOfAckNumbers)
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
				// that it was for the most recently received chunkFrame that works with the ackNumber
				else
				{
					int machedSequenceNumber = sequenceNumber - 1; 
					while(machedSequenceNumber % numberOfAckNumbers != ackFrame.getAckNumber())
					{
						machedSequenceNumber--;
					}

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

	
	/** gets the error string for the frame's error
	 * @param f Frame that we get the error string for
	 * @return "ERRR" if frame was corrupt, "DROP" if frame was dropped, or "SENT" if no error
	 */
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

	
	/** logs the number of elapsed ms since the synced start time
	 * @return the Long number of elapsed ms since the synced start time
	 */
	private Long time()
	{
		return new Date().getTime() - startTime;
	}
	
	
	/** Logs timeout
	 * @param f the ChunkFrame that we timed out on the ack for
	 */
	public void timeout(ChunkFrame f)
	{
		if (printerIsOn)
		{
			println("TimeOut " + f.getSequenceNumber());
		}
	}
}