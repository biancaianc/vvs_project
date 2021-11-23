package cli;

import server.WebServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;



public class WebServerCommandLine {

    private static int currentState=1;

    public static int getCurrentState() {
        return currentState;
    }

    public static void main(String[] args) throws IOException {
        final int PORT_SERVER_SOCKET = 10050;
        final String PATH_SITE="src/main/resources/TestSite";
        ServerSocket serverSocket = null;
        System.out.println("0-running");
        System.out.println("1-stopped");
        System.out.println("2-maintenance");
        InputStreamReader in=new InputStreamReader(System.in,StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(in);
        String option = reader.readLine();
        if(option!=null)
         currentState = Integer.parseInt(option);


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
            default:
                System.out.println("Not a valid state");


        }

    }

    public static void connectToServer(int PORT_SERVER_SOCKET, String PATH_SITE) throws IOException {
        ServerSocket serverSocket=new ServerSocket(PORT_SERVER_SOCKET);
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
