package server;
import util.WebServerUtil;

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

    public WebServer(Socket clientSoc, String sitePath) {
        clientSocket = clientSoc;
        this.sitePath = sitePath;
        webServerUtil = new WebServerUtil(sitePath);
        start();
    }


    public void run() {


        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Scanner myReader = null;
            String inputLine;
            File myFile = null;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                if (inputLine.startsWith("GET")) {
                    String requestedFilePath = webServerUtil.takePathFromRequestGet(inputLine);
                    myFile = webServerUtil.takeRequestedFile(requestedFilePath);
                    myReader = new Scanner(myFile);
                }

                if (inputLine.trim().equals(""))
                    break;

            }

            if (myReader != null) {
                out.println("HTTP/1.1 200 OK");
                out.println("\r\n");
                if (myFile.getAbsolutePath().endsWith(".jpg")) {
                    //trebuie adaugat
                } else
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        out.println(data);
                    }
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
}

