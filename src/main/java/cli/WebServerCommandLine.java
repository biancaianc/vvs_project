package cli;

import server.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.SQLOutput;

public class WebServerCommandLine {
    public static int currentState=1;
    public static void main(String[] args) throws IOException {
        final int PORT_SERVER_SOCKET = 10008;
        final String PATH_SITE="src/main/resources/TestSite";
        ServerSocket serverSocket = null;
        System.out.println("0-running");
        System.out.println("1-stopped");
        System.out.println("2-maintenance");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String option = reader.readLine();
        currentState = Integer.parseInt(option);
        switch (currentState) {
            case 0: {
                connectToServer(PORT_SERVER_SOCKET, PATH_SITE, serverSocket);
            }
            break;

            case 1:
                System.out.println("Server is stopped.");
                break;
            case 2:
                System.out.println("Server in maintenance.");
                connectToServer(PORT_SERVER_SOCKET, PATH_SITE, serverSocket);
                break;


        }

    }

    private static void connectToServer(int PORT_SERVER_SOCKET, String PATH_SITE, ServerSocket serverSocket) {
        try {
            serverSocket = new ServerSocket(PORT_SERVER_SOCKET);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new WebServer(serverSocket.accept(),PATH_SITE);
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
