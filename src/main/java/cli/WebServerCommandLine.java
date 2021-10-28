package cli;

import server.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.SQLOutput;

public class WebServerCommandLine {
    public static void main(String[] args) throws IOException {
        final int PORT_SERVER_SOCKET = 10008;
        ServerSocket serverSocket = null;
        int currentState;
        System.out.println("0-running");
        System.out.println("1-stopped");
        System.out.println("2-maintenance");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String option = reader.readLine();
        currentState = Integer.parseInt(option);
        switch (currentState) {
            case 0: {
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
            break;

            case 1:
                System.out.println("Server is stopped.");
                break;
            case 2:
                System.out.println("Server in maintenance.");


        }

    }

}
