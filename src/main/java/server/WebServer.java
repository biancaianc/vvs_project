package server;

import cli.WebServerCommandLine;
import factory.InputFactory;
import factory.OutputFactory;
import util.WebServerUtil;
//import util.WebServerUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WebServer extends Thread {

    private final String sitePath;
    protected Socket clientSocket;
    private WebServerUtil webServerUtil;
    private InputFactory inputFactory;
    private OutputFactory outputFactory;

    public WebServer(Socket clientSoc, String sitePath) {
        clientSocket = clientSoc;
        this.sitePath = sitePath;
        webServerUtil = new WebServerUtil(sitePath);
        inputFactory = new InputFactory();
        outputFactory = new OutputFactory();

    }

    public String start_server(){
        start();
        return "start";
    }
    public void run() {

        try {

            //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader in = inputFactory.getInputBufferReader(clientSocket.getInputStream());
            PrintWriter out = outputFactory.getOutputPrintWriter(clientSocket.getOutputStream());

            String inputLine;
            File myFile = null;

            if (WebServerCommandLine.getCurrentState() == 2) {
                myFile = new File(sitePath + "/maintenance.html");
            }
            if (WebServerCommandLine.getCurrentState() == 0) {
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Server: " + inputLine);
                    if (inputLine.startsWith("GET")) {
                        String requestedFilePath = takePathFromRequestGet(inputLine);
                        myFile = webServerUtil.takeRequestedFile(requestedFilePath);
                    }

                    if (inputLine.trim().equals(""))
                        break;
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
            System.exit(1);
        }
    }


    public void sendToOutputClient(PrintWriter out, File myFile) throws FileNotFoundException {
        System.out.println("blablabla");
        Scanner myReader = new Scanner(myFile);
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
