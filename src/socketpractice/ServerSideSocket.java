package socketpractice;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerSideSocket
{

	public final static int PORT = 1333;

	public static void main(String[] args)
	{
		System.out.println("The server's main is running");
		try (ServerSocket server = new ServerSocket(PORT))
		{
			while (true)
			{
				try (Socket connection = server.accept())
				{ // accept will return a Socket representing the connection
					System.out.println("server try loop after accept running");
					Writer out = new OutputStreamWriter(connection.getOutputStream());
					Date now = new Date();
					out.write(now.toString() + " here is the string written out" + "/r/n");
					out.flush();
					connection.close();
				}
				catch (IOException ex)
				{
				}
			}
		}
		catch (IOException ex)
		{
			System.err.println((ex));
			System.out.println("server hit the 2nd error catch");
		}
	}
}
