package server;

import client.Client;
import common.Common;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class WebServer extends Thread{

    private Client client;
    private ArrayList<Client> clientsConnected=new ArrayList<>();
    public void run() {
        ServerSocket serverSocket= null;
        try {
            serverSocket = new ServerSocket(Common.port);
            Common.serverSocket=serverSocket;
            System.out.println("NEW SERVER SOCKET" +serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Connection Socket Created");
            try {
                System.out.println("Waiting for Connections");
                while(true) {
                        client=new Client(serverSocket.accept());
                        System.out.println("Connection accepted");
                        client.start();
                        clientsConnected.add(client);
               }

            } catch (IOException e) {
                System.out.println("Accept failed.");
            }
        } finally {
            try {
                serverSocket.close();
                System.out.println("Socket closed");
            } catch (IOException e) {
                System.err.println("Could not close port: "+Common.port);
            }
        }

            System.out.println("SERVER IS DOWN");
            stopClientsThreads(clientsConnected);

    }

    public  void stopClientsThreads(ArrayList<Client> clientsCon) {
        ArrayList<Client> clients=clientsCon;
        for(Client client: clients){
            client.stop();
        }

    }

}
