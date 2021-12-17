package gui;

import server.WebServerConnection;

import javax.swing.*;

//import static gui.WebServerControl.semaphore;

public class StartServerSwingWorker extends SwingWorker<WebServerConnection,Integer> {


    private int port_server_socket;
    private String path_site;

    public StartServerSwingWorker(int port_server_socket, String path_site) {
        this.port_server_socket = port_server_socket;
        this.path_site = path_site;
    }


    @Override
    protected WebServerConnection doInBackground() throws Exception {

        WebServerConnection.connectToServer(port_server_socket,path_site);
        return null;
    }

    @Override
    public void done() {

    }
}

