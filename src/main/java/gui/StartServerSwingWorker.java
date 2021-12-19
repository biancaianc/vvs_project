package gui;


import server.WebServer;

import javax.swing.*;


public class StartServerSwingWorker extends SwingWorker<WebServer,Integer> {
    public WebServer getWebServerConnection() {
        return webServer;
    }

    public WebServer webServer =new WebServer();
    @Override
    protected WebServer doInBackground() throws Exception {
        webServer.start();
        return null;
    }

    @Override
    public void done() {

    }
}

