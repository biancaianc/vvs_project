
import org.junit.Before;
import org.junit.Test;
import client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;


public class ClientTest {


    @Before
    public void cleanOutputFile() throws FileNotFoundException {
        PrintWriter writer= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        writer.print("");
        writer.close();
    }
    @Test
    public void sendToOutputClient_test_normal() throws FileNotFoundException {
        Client webServer=new Client(new Socket());
        File file=new File("src/test/resources/test_input.txt");
        PrintWriter printWriter= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        webServer.sendToOutputClient(printWriter,file);
        printWriter.close();
        assertEquals(new File("src/test/resources/test_output.txt").length(),34);
    }

    @Before
    public void cleanOutputFile_jpg() throws FileNotFoundException {
        PrintWriter writer= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        writer.print("");
        writer.close();
    }

    @Test
    public void sendToOutputClient_test_jpg() throws FileNotFoundException {
        Client client =new Client(new Socket());
        File file=new File("src/test/resources/test_input.jpg");
        PrintWriter printWriter= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        //PrintWriter printWriter=new PrintWriter(new FileOutputStream("src/test/resources/test_output.txt"));
        client.sendToOutputClient(printWriter,file);
        printWriter.close();
        assertEquals(new File("src/test/resources/test_output.txt").length(),21);
    }

     @Test
    public void createServerSocketTest() throws IOException {
         ServerSocket result =new ServerSocket(10020);
         assertEquals(result.getLocalPort(),10020);
     }

    @Test
    public void createInput() throws IOException {
        Path path=new File("src/test/resources/input.txt").toPath();
        BufferedReader reader= Files.newBufferedReader(path,StandardCharsets.UTF_8);
        assertEquals(reader.readLine(),"aa");
         reader.close();
    }

    @Test
    public void createOutput() throws IOException {
        File file=new File("src/test/resources/output.txt");
        PrintWriter printWriter= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/output.txt"), StandardCharsets.UTF_8), true);
        printWriter.print("out");
        printWriter.close();
        assertEquals(file.length(),3);
    }

    @Test
    public void takePathFromRequestGet_normal() {
        Client client =new Client(new Socket());
        String expected = "/a.html";
        String result = client.takePathFromRequestGet("GET /a.html HTTP/1.1");
        assertEquals(expected, result);
    }
    @Test
    public void takePathFromRequestGet_index() {
        Client client =new Client(new Socket());
        String expected = "/index.html";
        String result = client.takePathFromRequestGet("GET / HTTP/1.1");
        assertEquals(expected, result);
    }





}
