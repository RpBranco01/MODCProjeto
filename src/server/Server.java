package server	;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import calculator.Formula;
import file_manager.FileManager;

public class Server
{
	//initialize socket and input stream
	private Socket		 socket = null;
	private ServerSocket server = null;
	private ObjectInputStream in	 = null;
	private ObjectOutputStream out = null;
	FileManager fileManager = null;


	// constructor with port
	public Server(int port, String filePath)
	{
		// starts server and waits for a connection
		try
		{
			fileManager = new FileManager(filePath, "caguei");
			fileManager.deCipherCounts();
			
			
			server = new ServerSocket(port);	
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted");

			// takes input from the client socket
			in = new ObjectInputStream(
				socket.getInputStream());

			// sends output to the socket
			out = new ObjectOutputStream(
				socket.getOutputStream());

			String line = "";
			Formula formula;

			// reads message from client until "Over" is sent
			while (true)
			{
				try
				{
					line = (String) in.readObject();
					if (line.equals("Over")) {
						break;
					}
					formula = new Formula(line);
					String result = formula.makeOperation();
					
					fileManager.appendCount(result);
					fileManager.getListCounts();

					out.writeObject(result);
				}
				catch(IOException i)
				{
					System.out.println(i);
				}
			}
			fileManager.cipherCounts();
			System.out.println("Closing connection");

			// close connection
			socket.close();
			in.close();
		}
		catch(IOException | ClassNotFoundException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		new Server(5000, args[0]);
	}
}
