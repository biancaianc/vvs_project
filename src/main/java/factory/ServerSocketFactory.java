package factory;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketFactory {
    public ServerSocket createSocketFor(int port) throws IOException {
        return new ServerSocket(port);
    }
}
