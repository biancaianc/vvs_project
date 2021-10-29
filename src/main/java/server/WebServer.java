package server;

import javax.imageio.ImageIO;
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
            File myFile = null;
            //get endpoint
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                if (inputLine.startsWith("GET")) {
                    String path = takePathFromRequestGet(inputLine);
                    myFile = findPathFile("src/main/resources/TestSite", path);
                    myReader = new Scanner(myFile);

                }

                if (inputLine.trim().equals(""))
                    break;

            }

            out.println("HTTP/1.0 200 OK\n\n");

            if (myReader != null)
                if (myFile.getAbsolutePath().endsWith(".jpg")) {


                } else
                    while (myReader.hasNextLine()) {

                        String data = myReader.nextLine();
                        out.println(data);
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

    private File findPathFile(String s, String path) throws IOException {

        String p = s;
        p = p.concat(path);
        p = p.trim();
        p = p.replaceAll("%20", " ");
        p = p.trim();

        File fi = new File(p);

        String finalEndpoint = path.replace("/", "").replaceAll("%20", " ");
        if (!fi.exists()) {
            return findRequestedFileInTestSiteLocation(s, finalEndpoint);
        } else {
            return fi;
        }
    }

    private File findRequestedFileInTestSiteLocation(String s, String finalEndpoint) throws IOException {
        List<Path> mylist = Files.walk(Paths.get("src/main/resources/TestSite"))
                .filter(Files::isRegularFile)
                .filter(path1 -> path1.endsWith(finalEndpoint))
                .collect(Collectors.toList());
        if (mylist.size() == 0) {
            return new File("src/main/resources/TestSite/error.html");
        } else {
            return new File(String.valueOf(mylist.get(0)));
        }
    }

    private String takePathFromRequestGet(String inputLine) {
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
