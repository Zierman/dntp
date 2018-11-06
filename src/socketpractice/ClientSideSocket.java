package socketpractice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSideSocket
{

	public static void main(String[] args)
	{
		String hostname = args.length > 0 ? args[0] : "http://www.time.nist.gov";

		// Note just completely changing the value of hostname here
		hostname = "127.0.0.1";
		Socket socket = null;

		System.out.println("Client main is running");

		try
		{
			System.out.println("Attempting try block");
			socket = new Socket(hostname, 1333);
			System.out.println("client has connected to " + socket.getRemoteSocketAddress());
			socket.setSoTimeout(15000);
			InputStream in = socket.getInputStream();
			StringBuilder time = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(in, "ASCII");
			for (int c = reader.read(); c != -1; c = reader.read())
			{
				time.append((char) c);
			}
			System.out.println(time);

		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}
		finally
		{
			if (socket != null)
			{
				try
				{
					socket.close();
				}
				catch (IOException ex)
				{
				}
			}
		}
	}

}
