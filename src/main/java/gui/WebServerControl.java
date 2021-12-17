package gui;

import server.WebServer;
import server.WebServerConnection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;


public class WebServerControl extends JFrame {
    //public static Semaphore semaphore = new Semaphore(0);

    private int PORT_SERVER_SOCKET = 10051;
    private String maintanance_directory = WebServerConnection.maintenanceDirectory;
    private String PATH_SITE = WebServerConnection.rootDirectory;
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
    private JCheckBox maintenanceModeCheckBox;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;

    public WebServerControl(String title) throws IOException {
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
        this.stopServerButton.setEnabled(false);
        this.maintenanceModeCheckBox.setEnabled(false);
        this.checkBox1.setEnabled(false);
        this.checkBox2.setEnabled(false);
        this.checkBox1.setSelected(true);
        this.checkBox2.setSelected(true);
        this.maintenanceDirectory.setText(WebServerConnection.maintenanceDirectory);

        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("running");
                serverListeningPort_label.setText(String.valueOf(PORT_SERVER_SOCKET));
                serverAdress_label.setText("127.0.0.1");
                WebServerConnection.currentState = 0;
                StartServerSwingWorker mySwingWorker = new StartServerSwingWorker(PORT_SERVER_SOCKET, PATH_SITE);
                try {
                    mySwingWorker.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startServerButton.setEnabled(false);
                stopServerButton.setEnabled(true);
                port.setEnabled(false);
                rootDirectory.setEnabled(false);
                maintenanceDirectory.setEnabled(true);
                maintenanceModeCheckBox.setEnabled(true);
            }
        });

        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("not running");
                serverListeningPort_label.setText("not running");
                serverAdress_label.setText("not running");
                WebServerConnection.currentState = 1;
                WebServerConnection.getWebserver().stop();
                System.out.println("stopped");
                stopServerButton.setEnabled(false);
                startServerButton.setEnabled(true);
                port.setEnabled(true);
                rootDirectory.setEnabled(true);
                maintenanceDirectory.setEnabled(false);
                maintenanceModeCheckBox.setSelected(false);
                maintenanceModeCheckBox.setEnabled(false);


            }
        });


        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String text = port.getText();
                System.out.println(text);
                try {
                    PORT_SERVER_SOCKET = Integer.parseInt(text);
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("This is not a number");
                }
            }
        });


        maintenanceModeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (maintenanceModeCheckBox.getModel().isSelected()) {
                    serverStatus_label.setText("maintenance");
                    serverListeningPort_label.setText(String.valueOf(PORT_SERVER_SOCKET));
                    serverAdress_label.setText("127.0.0.1");
                    WebServerConnection.currentState = 2;
                    //ADD here
                    port.setEnabled(false);
                    maintenanceDirectory.setEnabled(false);
                } else if (!maintenanceModeCheckBox.getModel().isSelected()) {
                    port.setEnabled(false);
                    rootDirectory.setEnabled(false);
                    maintenanceDirectory.setEnabled(true);
                    serverStatus_label.setText("running");
                    WebServerConnection.currentState = 0;
                }
            }
        });
        rootDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = rootDirectory.getText();
                System.out.println(text);
                try {
                    if (validateRootDirectory(text)) {
                        checkBox1.setSelected(true);
                        WebServerConnection.rootDirectory = text;
                    } else
                        checkBox1.setSelected(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        System.out.println(validateRootDirectory("src/main/resources/TestSite"));
        maintenanceDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = maintenanceDirectory.getText();
                System.out.println(text);
                try {
                    if (validateMaintenanceDirectory(text)) {
                        checkBox2.setSelected(true);
                        WebServerConnection.maintenanceDirectory = text;
                    } else
                        checkBox2.setSelected(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private boolean validateMaintenanceDirectory(String text) throws IOException {
        File file = new File(text);
        if (file.exists() && file.isFile()) {
            System.out.println("e valid");
            return Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("maintenance.html"));
        }
        return false;
    }

    //un root directory e valid daca calea data exista, e un director si contine cel putin un fisier .html
    private boolean validateRootDirectory(String text) throws IOException {
        File file = new File(text);
        if (file.exists() && file.isDirectory()) {
            System.out.println("e director");
            Boolean isMaintenance = Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("maintenance.html"));
            Boolean isError = Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("error.html"));
            Boolean isIndex = Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("index.html"));
            if (isMaintenance && isError && isIndex)
                return true;
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        JFrame jFrame = new WebServerControl("Configure the web server");
        jFrame.setVisible(true);

    }

    private void createUIComponents() {

    }


    public String getMaintanance_directory() {
        return maintanance_directory;
    }


    }
