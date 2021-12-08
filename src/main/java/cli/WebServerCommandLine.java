package cli;

import server.WebServer;
import server.WebServerConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;



public class WebServerCommandLine {



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
         WebServerConnection.currentState = Integer.parseInt(option);


        switch (WebServerConnection.currentState) {
            case 0: {
                WebServerConnection.connectToServer(PORT_SERVER_SOCKET, PATH_SITE);
            }
            break;

            case 1:
                System.out.println("Server is stopped.");
                break;
            case 2:
                System.out.println("Server in maintenance.");
                WebServerConnection.connectToServer(PORT_SERVER_SOCKET, PATH_SITE);
                break;
            default:
                System.out.println("Not a valid state");


        }

    }



}
