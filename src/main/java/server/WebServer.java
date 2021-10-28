package server;

import Utils.FileUtil;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WebServer extends Thread {

    protected Socket clientSocket;
    public WebServer(Socket clientSoc) {
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
                    File myFile=FileUtil.findPathFile("src/main/resources/TestSite",inputLine);
                    myReader= new Scanner(myFile);

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

}
