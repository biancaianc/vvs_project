import javax.management.StringValueExp;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WebServer extends Thread {
	private static final int PORT_SERVER_SOCKET = 10009;
	protected Socket clientSocket;
	//0-off 1-run 3-maintenance
	public static int currentState=0;

	WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	public void run() {
		System.out.println("New Communication Thread Started");

		try {

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			File myObj=new File("src/main/resources/TestSite/a.html");
			Scanner myReader = new Scanner(myObj);
			
			String inputLine;

			//get endpoint
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Server: " + inputLine);
				if(inputLine.startsWith("GET")){
					char path[] = new char[100];
					int start_value=4;
					inputLine.getChars(start_value,inputLine.length()-9,path,0);
					String endpoint=String.valueOf(path);
					System.out.println("asta e"+endpoint);
					/*
					myObj = new File("src/main/resources/TestSite"+endpoint);
					System.out.println(myObj.toString());
					myReader=myReader1;
					System.out.println("kjhgf");
					*/

					break;
				}


				System.out.println("Aaaa" );
			}

			out.println("HTTP/1.0 400 OK\n\n");
			if(myReader!=null)
			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				out.println(data);
			}

			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT_SERVER_SOCKET);
			System.out.println("Connection Socket Created");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}
}
