package gui;

import server.WebServerConnection;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;


public class WebServerControl extends JFrame {
    public static Semaphore semaphore=new Semaphore(0);

    private int PORT_SERVER_SOCKET = 10051;
    private String PATH_SITE = "src/main/resources/TestSite";
    private JPanel mainPanel;
    private JButton startServerButton;
    private JTextField port;
    private JTextField rootDirectory;
    private JTextField maintenanceDirectory;
    private JButton stopServerButton;
    private JButton maintenaceModeButton;
    private JLabel serverStatus_label;
    private JLabel serverAdress_label;
    private JLabel serverListeningPort_label;

    public WebServerControl(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.serverStatus_label.setText("stopped");
        this.serverListeningPort_label.setText("stopped");
        this.serverAdress_label.setText("stopped");
        this.port.setText(String.valueOf(this.PORT_SERVER_SOCKET));
        this.rootDirectory.setText(String.valueOf(this.PATH_SITE));
        this.maintenanceDirectory.setText("nu stiu");

        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("running");
                serverListeningPort_label.setText(String.valueOf(PORT_SERVER_SOCKET));
                serverAdress_label.setText("127.0.0.1");
                WebServerConnection.currentState = 0;

                StartServerSwingWorker mySwingWorker= new StartServerSwingWorker(PORT_SERVER_SOCKET, PATH_SITE);
                try {
                    mySwingWorker.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //mai e de lucru-se opreste dar ramane cumva in wait
        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("stopped");
                serverListeningPort_label.setText("stopped");
                serverAdress_label.setText("stopped");
                WebServerConnection.currentState = 1;
                StopServerSwingWorker otherSwing=new StopServerSwingWorker();
                System.out.println("stopped");
            }
        });
    }


    public static void main(String[] args) {
        JFrame jFrame = new WebServerControl("Configure the web server");
        jFrame.setVisible(true);

    }

    private void createUIComponents() {

    }
}
