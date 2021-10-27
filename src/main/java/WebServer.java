import Utils.FileUtil;

import javax.management.StringValueExp;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


			Scanner myReader = null;
			String inputLine;

			//get endpoint
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Server: " + inputLine);
				if(inputLine.startsWith("GET")){
					char path[] = new char[100];
					int start_value=4;
					inputLine.getChars(start_value,inputLine.length()-9,path,0);
					String endpoint=String.valueOf(path);
					String p="src/main/resources/TestSite";
					endpoint=endpoint.trim();
					if(endpoint.length()==1){
						endpoint+="a.html";
					}
					p=p.concat(endpoint);
					p=p.trim();
					p=p.replaceAll("%20"," ");
					p=p.trim();

					File fi=new File(p);
					if(p.isEmpty()) System.out.println("emptyyyyy");
					System.out.println(endpoint);

//					File f = new File("src/main/resources/TestSite");
				String finalEndpoint = endpoint.replace("/","").replaceAll("%20"," ");
//					System.out.println("My end:"+endpoint);
//					File[] matchingFiles = f.listFiles(new FilenameFilter() {
//						public boolean accept(File dir, String name) {
//							return name.endsWith(finalEndpoint);
//						}
//					});
//					System.out.println("Matching:"+matchingFiles.length);

					if(!fi.exists()){

						List<Path> mylist=Files.walk(Paths.get("src/main/resources/TestSite"))
								.filter(Files::isRegularFile)
								.filter(path1 -> path1.endsWith(finalEndpoint))
								.collect(Collectors.toList());
						if(mylist.size()==0){

							File myErrorFile=new File("src/main/resources/TestSite/error.html");
							myReader=new Scanner(myErrorFile);
						}
						else {
							File myObj = new File(String.valueOf(mylist.get(0)));
							myReader = new Scanner(myObj);
						}

					}
					else {
						//File myObj=new File(p);
						//System.out.println("kjiuhy" + matchingFiles[0].getAbsolutePath());
						myReader = new Scanner(fi);
					}

				}

				if (inputLine.trim().equals(""))
					break;

			}

			out.println("HTTP/1.0 200 OK\n\n");
			if(myReader!=null)
			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				out.println(data);
			}
            while(clientSocket.getKeepAlive()){

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
