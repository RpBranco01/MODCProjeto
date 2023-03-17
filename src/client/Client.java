package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Scanner scanner = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected\n Write operation like 1 + 2:");

			// takes scanner from terminal
			scanner = new Scanner(System.in);
			

			// sends output to the socket
			out = new ObjectOutputStream(
				socket.getOutputStream());

			// takes input from the socket
			in = new ObjectInputStream(
				socket.getInputStream());

		}
		catch (IOException e) {
			System.out.println(e);
			return;
		}

		// string to read message from input
		String line = "";

		// string to receive the message
		String response = "";

		// keep reading until "Over" is input
		while (!line.equals("Over")) {
			try {
				line = (String) scanner.nextLine();
				out.writeObject(line);
				if (line.equals("Over")) {
					break;
				}

				response = (String)in.readObject();
				System.out.println(response);
			}
			catch (IOException | ClassNotFoundException i) {
				System.out.println(i);
			}
		}

		// close the connection
		try {
			socket.close();
		}
		catch (IOException i) {
			System.out.println("Erro aqui");
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		new Client("127.0.0.1", 5000);
	}
}
