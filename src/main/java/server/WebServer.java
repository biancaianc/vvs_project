package server;

import cli.WebServerCommandLine;
import util.WebServerUtil;

import java.net.*;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class WebServer extends Thread {

    private String sitePath;
    private Socket clientSocket;
    private WebServerUtil webServerUtil;

    public WebServer(Socket clientSoc) {
        clientSocket = clientSoc;
        this.sitePath = WebServerConnection.rootDirectory;
        webServerUtil = new WebServerUtil(sitePath);
    }

    public String start_server(){
        start();
        return "start";
    }

    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
            PrintWriter out= new PrintWriter(new OutputStreamWriter(
                    (clientSocket.getOutputStream()), StandardCharsets.UTF_8), true);

            String inputLine;
            File myFile = null;

            if(WebServerConnection.getCurrentState()==2) {
                myFile = new File(WebServerConnection.maintenanceDirectory);
            }

            if (WebServerConnection.getCurrentState() == 0) {
                while ((inputLine = in.readLine()) != null) {
                    if(WebServerConnection.getCurrentState()==2)
                    {
                        myFile = new File(WebServerConnection.maintenanceDirectory);
                        break;
                    }else {
                        System.out.println("Server: " + inputLine);
                        if (inputLine.startsWith("GET")) {
                            String requestedFilePath = takePathFromRequestGet(inputLine);
                            myFile = webServerUtil.takeRequestedFile(requestedFilePath);
                        }

                        if (inputLine.trim().equals(""))
                            break;
                    }
                }
            }

            if (myFile != null) {
                sendToOutputClient(out, myFile);
            } else {
                out.println("HTTP/1.1 400 PAGE NOT FOUND");
            }


            while (clientSocket.getKeepAlive()) {




            }
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            throw new RuntimeException();
        }
    }


    public void sendToOutputClient(PrintWriter out, File myFile) throws FileNotFoundException {
        Scanner myReader = null;
        try {
            myReader = new Scanner(myFile,StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("HTTP/1.1 200 OK");
        out.println("\r\n");
        if (myFile.getAbsolutePath().endsWith(".jpg")) {
            //trebuie adaugat
        } else while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            out.println(data);
            System.out.println(data);
        }
    }

    public String takePathFromRequestGet(String inputLine) {
        char path[] = new char[100];
        int start_value = 4;
        inputLine.getChars(start_value, inputLine.length() - 9, path, 0);
        String endpoint = String.valueOf(path);
        endpoint = endpoint.trim();
        if (endpoint.length() == 1) {
            endpoint += "index.html";
        }
        return endpoint;
    }

    public boolean stopWaiting() {
        return false;
    }
}
