import cli.WebServerCommandLine;
import factory.InputFactory;
import factory.OutputFactory;
import factory.ServerSocketFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import server.WebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.Stream;

import org.mockito.Mockito;
import util.WebServerUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;

public class WebServerTest {

    private WebServer webServer=Mockito.mock(WebServer.class);
    private WebServerCommandLine webServerCommandLine=Mockito.mock(WebServerCommandLine.class);
    private WebServerUtil webServerUtil=Mockito.mock(WebServerUtil.class);
    ServerSocketFactory serverSocketFactory=Mockito.mock(ServerSocketFactory.class);
    private InputFactory inputFactory=Mockito.mock(InputFactory.class);
    private OutputFactory outputFactory=Mockito.mock(OutputFactory.class);


    @Test
    public void test_running() throws IOException {
        WebServerCommandLine.currentState=2;
        WebServer webServer=new WebServer(new Socket(),"src/main/resources/TestSite/");
        //Mockito.when(WebServerCommandLine.getCurrentState()).thenReturn(1);
        System.out.println("KKK");

        Mockito.when(inputFactory.getInputBufferReader(any())).thenReturn(new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/test_input.txt"))));
        Mockito.when(outputFactory.getOutputPrintWriter(any())).thenReturn(new PrintWriter(new FileOutputStream("src/test/resources/test_maintenance_output.txt"), true));
        webServer.run();
        System.out.println(new File("src/test/resources/test_maintenance_output.txt").length());


    }
    @Before
    public void cleanOutputFile() throws FileNotFoundException {
        File outputFileToClean= new File("src/test/resources/test_output.txt");
                PrintWriter writer = new PrintWriter(outputFileToClean.getAbsolutePath());
        writer.print("");
        writer.close();
    }
    @Test
    public void sendToOutputClient_test_normal() throws FileNotFoundException {
        WebServer webServer=new WebServer(new Socket(),"src/main/resources/TestSite/");
        File file=new File("src/test/resources/test_input.txt");
        PrintWriter printWriter=new PrintWriter(new FileOutputStream("src/test/resources/test_output.txt"));
        webServer.sendToOutputClient(printWriter,file);
        printWriter.close();
        assertEquals(new File("src/test/resources/test_output.txt").length(),34);
    }
    @Before
    public void cleanOutputFile_jpg() throws FileNotFoundException {
        File outputFileToClean= new File("src/test/resources/test_output.txt");
        PrintWriter writer = new PrintWriter(outputFileToClean.getAbsolutePath());
        writer.print("");
        writer.close();
    }

    @Test
    public void sendToOutputClient_test_jpg() throws FileNotFoundException {
        WebServer webServer=new WebServer(new Socket(),"src/main/resources/TestSite/");
        File file=new File("src/test/resources/test_input.jpg");
        PrintWriter printWriter=new PrintWriter(new FileOutputStream("src/test/resources/test_output.txt"));
        webServer.sendToOutputClient(printWriter,file);
        printWriter.close();
        assertEquals(new File("src/test/resources/test_output.txt").length(),21);
    }

     @Test
    public void createServerSocketTest() throws IOException {
         ServerSocketFactory serverSocketFactory= new ServerSocketFactory();
         ServerSocket result =serverSocketFactory.createSocketFor(10020);
         assertEquals(result.getLocalPort(),10020);
     }

    @Test
    public void createInput() throws IOException {
        InputFactory inputFactory=new InputFactory();
        BufferedReader reader=inputFactory.getInputBufferReader(new FileInputStream("src/test/resources/input.txt"));
        assertEquals(reader.readLine(),"aa");
    }

    @Test
    public void createOutput() throws IOException {
        File file=new File("src/test/resources/output.txt");
        OutputFactory outputFactory=new OutputFactory();
        PrintWriter printWriter=outputFactory.getOutputPrintWriter(new FileOutputStream("src/test/resources/output.txt"));
        printWriter.print("out");
        printWriter.close();
        assertEquals(file.length(),3);
    }






}
