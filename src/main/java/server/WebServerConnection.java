package server;

import java.io.IOException;
import java.net.ServerSocket;

public class WebServerConnection {
    public static int currentState=1;
    public static int lastState=1;
    public static String rootDirectory="src/main/resources/TestSite";
    public static String maintenanceDirectory="src/main/resources/TestSite/maintenance.html";

    public static WebServer getWebserver() {
        return webserver;
    }

    public static WebServer webserver;
    public static int getCurrentState() {
        return currentState;
    }
    public static void connectToServer(int PORT_SERVER_SOCKET, String PATH_SITE) throws IOException {
        ServerSocket serverSocket=new ServerSocket(PORT_SERVER_SOCKET);
        try {
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    webserver = new WebServer(serverSocket.accept());
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
