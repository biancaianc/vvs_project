//import cli.WebServerCommandLine;
//import factory.ServerSocketFactory;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnit;
//import server.WebServer;
//
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.SocketException;
//import java.nio.charset.StandardCharsets;
//
//import static org.junit.Assert.*;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//public class WebServerCommandLineTest {
//    // WebServerCommandLine webServerCommandLine=new WebServerCommandLine();
//    ServerSocketFactory serverSocketFactory = Mockito.mock(ServerSocketFactory.class);
//    WebServer webServer = Mockito.mock(WebServer.class);
//    ServerSocket serverSocket=Mockito.mock(ServerSocket.class);
//
//    @Ignore
//    @Test
//    public void connectToServerTest_normal() throws IOException {
//        String result=null;
//        String expected="Server is working";
//        ServerSocket test = new ServerSocket(10012);
//        Mockito.when(serverSocketFactory.createSocketFor(10009)).thenReturn(test);
//        Mockito.when(webServer.stopWaiting()).thenReturn(true);
//        //doNothing().doThrow(new ArrayIndexOutOfBoundsException()).when(webServer).start_server();
//        //Mockito.when(webServer.start_server()).thenThrow(new ArrayIndexOutOfBoundsException());
//        try {
//            WebServerCommandLine.connectToServer(10009, "src/main/resources/TestSite");
//        } catch (ArrayIndexOutOfBoundsException e) {
//            result = "Server is working";
//        }
//        assertEquals(expected,result);
//        test.close();
//    }
//
//@Ignore
//    @Test
//    public void connectToServerTest_accept_fail() throws IOException {
//        String result=null;
//        String expected="Accept failed";
//        ServerSocket test = new ServerSocket(10011);
//        Mockito.when(serverSocketFactory.createSocketFor(10013)).thenReturn(test);
//        Mockito.when(webServer.start_server()).thenReturn("bla");
//        Mockito.when(webServer.stopWaiting()).thenReturn(true);
//        try {
//            Mockito.doThrow(new IOException()).when(serverSocket).accept();
//            WebServerCommandLine.connectToServer(10013, "src/main/resources/TestSite");
//        } catch (IOException e) {
//            result="Accept failed";
//        }
//
//
//        assertEquals(expected,result);
//        test.close();
//    }
//
//
//}
//
//
