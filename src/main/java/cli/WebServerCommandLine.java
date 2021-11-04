package cli;

import factory.ServerSocketFactory;
import server.WebServer;
import util.WebServerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.sql.SQLOutput;

public class WebServerCommandLine {
    public static int currentState=1;

    public static int getCurrentState() {
        return currentState;
    }
    public static ServerSocketFactory serverSocketFactory=new ServerSocketFactory();

    public static void main(String[] args) throws IOException {
        final int PORT_SERVER_SOCKET = 10050;
        final String PATH_SITE="src/main/resources/TestSite";
        ServerSocket serverSocket = null;
        System.out.println("0-running");
        System.out.println("1-stopped");
        System.out.println("2-maintenance");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String option = reader.readLine();
        currentState = Integer.parseInt(option);
        //serverSocket = new ServerSocket(PORT_SERVER_SOCKET);

        switch (currentState) {
            case 0: {
                connectToServer(PORT_SERVER_SOCKET, PATH_SITE);
            }
            break;

            case 1:
                System.out.println("Server is stopped.");
                break;
            case 2:
                System.out.println("Server in maintenance.");
                connectToServer(PORT_SERVER_SOCKET, PATH_SITE);
                break;


        }

    }

    public static void connectToServer(int PORT_SERVER_SOCKET, String PATH_SITE) throws IOException {
        ServerSocket serverSocket=serverSocketFactory.createSocketFor(PORT_SERVER_SOCKET);
        try {
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    WebServer webserver = new WebServer(serverSocket.accept(), PATH_SITE);
                    webserver.start_server();
                    if(webserver.stopWaiting())
                        break;
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: "+PORT_SERVER_SOCKET);
                System.exit(1);
            }
        }
    }

}
