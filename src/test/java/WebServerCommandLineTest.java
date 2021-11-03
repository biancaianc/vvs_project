import cli.WebServerCommandLine;
import factory.ServerSocketFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;


import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;

public class WebServerCommandLineTest {
   // WebServerCommandLine webServerCommandLine=new WebServerCommandLine();
    ServerSocketFactory serverSocketFactory=Mockito.mock(ServerSocketFactory.class);
   @Ignore
    @Test
    public void connectToServerTest() throws IOException {
        ServerSocket test=new ServerSocket(10011);
        Mockito.when(serverSocketFactory.createSocketFor(10010)).thenReturn(test);
        //Mockito.when(test.accept()).then();

        WebServerCommandLine.connectToServer(10012,"src/main/resources/TestSite");
        //assertNotNull();
        test.close();
        Mockito.verify(serverSocketFactory,Mockito.times(0)).createSocketFor(10010);
        Mockito.verify(test,Mockito.times(0)).accept();

    }



    }


