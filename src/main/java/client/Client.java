package client;


import common.Common;
import util.WebServerUtil;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Client extends Thread {

    private Socket clientSocket;

    public Client(Socket clientSoc) {
        clientSocket = clientSoc;
    }

    public void run() {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
            PrintWriter out= new PrintWriter(new OutputStreamWriter(
                    (clientSocket.getOutputStream()), StandardCharsets.UTF_8), true);

            String inputLine;
            File myFile = null;

            if (Common.currentState == 2) {
                myFile = new File(Common.maintenanceDirectory);
            }

            if (Common.currentState == 0) {
                //System.out.println("STARE 0");
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println("Server: " + inputLine);
                    if (inputLine.startsWith("GET")) {
                        String requestedFilePath = takePathFromRequestGet(inputLine);
                        myFile = WebServerUtil.takeRequestedFile(requestedFilePath);
                        break;
                    }
                    if (inputLine.trim().equals(""))
                        break;
                }
            }


            if (Common.currentState != 1 && myFile.exists() && myFile.isFile() && !myFile.isDirectory()) {
                sendToOutputClient(out, myFile);
            } else if (Common.currentState != 1)
                sendToOutputClient(out,WebServerUtil.takeRequestedFile("/error.html"));


            System.out.println("GATA CLIENT");
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException  e) {
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
            //
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

}
