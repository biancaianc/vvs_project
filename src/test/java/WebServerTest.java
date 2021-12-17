import cli.WebServerCommandLine;
import org.junit.Before;
import org.junit.Test;
import server.WebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.mockito.Mockito;
import util.WebServerUtil;

import static org.junit.Assert.assertEquals;


public class WebServerTest {


    @Before
    public void cleanOutputFile() throws FileNotFoundException {
        PrintWriter writer= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        writer.print("");
        writer.close();
    }
    @Test
    public void sendToOutputClient_test_normal() throws FileNotFoundException {
        WebServer webServer=new WebServer(new Socket());
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
        WebServer webServer=new WebServer(new Socket());
        File file=new File("src/test/resources/test_input.jpg");
        PrintWriter printWriter= new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("src/test/resources/test_output.txt"), StandardCharsets.UTF_8), true);
        //PrintWriter printWriter=new PrintWriter(new FileOutputStream("src/test/resources/test_output.txt"));
        webServer.sendToOutputClient(printWriter,file);
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
        WebServer webServer=new WebServer(new Socket());
        String expected = "/a.html";
        String result = webServer.takePathFromRequestGet("GET /a.html HTTP/1.1");
        assertEquals(expected, result);
    }
    @Test
    public void takePathFromRequestGet_index() {
        WebServer webServer=new WebServer(new Socket());
        String expected = "/index.html";
        String result = webServer.takePathFromRequestGet("GET / HTTP/1.1");
        assertEquals(expected, result);
    }





}
