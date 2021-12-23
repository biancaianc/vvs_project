package gui;

import common.Common;
import util.WebServerUtil;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class WebServerControl extends JFrame {

    private JPanel mainPanel;
    private JButton startServerButton;
    private JTextField port;
    private JTextField rootDirectory;
    private JTextField maintenanceDirectory;
    private JButton stopServerButton;
    private JLabel serverStatus_label;
    private JLabel serverAdress_label;
    private JLabel serverListeningPort_label;
    private JCheckBox maintenanceModeCheckBox;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    StartServerSwingWorker mySwingWorker;

    public WebServerControl(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.serverStatus_label.setText("stopped");
        this.serverListeningPort_label.setText("stopped");
        this.serverAdress_label.setText("stopped");
        this.port.setText(String.valueOf(Common.port));
        this.rootDirectory.setText(String.valueOf(Common.rootDirectory));
        this.maintenanceDirectory.setText("nu stiu");
        this.stopServerButton.setEnabled(false);
        this.maintenanceModeCheckBox.setEnabled(false);
        this.checkBox1.setEnabled(false);
        this.checkBox2.setEnabled(false);
        this.checkBox1.setSelected(true);
        this.checkBox2.setSelected(true);
        this.maintenanceDirectory.setText(Common.maintenanceDirectory);


        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("running");
                serverListeningPort_label.setText(String.valueOf(Common.port));
                serverAdress_label.setText("127.0.0.1");
                Common.currentState = 0;
                System.out.println("PORT:" + Common.port);

                mySwingWorker = new StartServerSwingWorker();
                try {
                    mySwingWorker.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (checkBox2.isSelected()) {
                    maintenanceModeCheckBox.setEnabled(true);
                }
                startServerButton.setEnabled(false);
                stopServerButton.setEnabled(true);
                port.setEnabled(false);
                rootDirectory.setEnabled(false);
                maintenanceDirectory.setEnabled(true);
            }
        });

        stopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                serverStatus_label.setText("not running");
                serverListeningPort_label.setText("not running");
                serverAdress_label.setText("not running");
                Common.currentState = 1;
                mySwingWorker.webServer.stop();
                try {
                    Common.serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("stopped");
                stopServerButton.setEnabled(false);
                startServerButton.setEnabled(true);
                port.setEnabled(true);
                rootDirectory.setEnabled(true);
                maintenanceDirectory.setEnabled(false);
                maintenanceModeCheckBox.setSelected(false);
                maintenanceModeCheckBox.setEnabled(false);
                System.out.println("PORT:" + Common.port);

            }
        });


        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = port.getText();
                System.out.println(text);
                try {
                    Common.port = Integer.parseInt(text);
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
                    serverListeningPort_label.setText(String.valueOf(Common.port));
                    serverAdress_label.setText("127.0.0.1");
                    Common.currentState = 2;
                    //ADD here
                    port.setEnabled(false);
                    maintenanceDirectory.setEnabled(false);
                } else if (!maintenanceModeCheckBox.getModel().isSelected()) {
                    port.setEnabled(false);
                    rootDirectory.setEnabled(false);
                    maintenanceDirectory.setEnabled(true);
                    serverStatus_label.setText("running");
                    Common.currentState = 0;
                }
            }
        });
        rootDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = rootDirectory.getText();
                System.out.println(text);
                Common.rootDirectory = text;
                try {
                    if (validateRootDirectory(text)) {
                        checkBox1.setSelected(true);
                        startServerButton.setEnabled(true);
                        Common.lastRoot = text;
                    } else {
                        Common.rootDirectory = Common.lastRoot;
                        checkBox1.setSelected(false);
                        startServerButton.setEnabled(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        maintenanceDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = maintenanceDirectory.getText();
                System.out.println(text);
                try {
                    if (validateMaintenanceDirectory(text)) {
                        checkBox2.setSelected(true);
                        maintenanceModeCheckBox.setEnabled(true);
                        Common.maintenanceDirectory = text;
                    } else {
                        checkBox2.setSelected(false);
                        maintenanceModeCheckBox.setEnabled(false);
                    }
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


    private boolean validateRootDirectory(String text) throws IOException {
        Common.rootDirectory = text;
        File file = new File(text);
        if (file.exists() && file.isDirectory()) {
            System.out.println("e director");
            Boolean isMaintenance = Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("maintenance.html"));
            boolean isError = true;
            File errorFile = WebServerUtil.takeRequestedFile("/error.html");
            if (!errorFile.getPath().equals(new File(text + "/error.html").getPath())) {
                isError = false;
            }

            Boolean isIndex = Files.walk(Paths.get(text))
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> path.toString().endsWith("index.html"));

            if (isMaintenance && isError && isIndex)
                return true;

        }
        return false;
    }


    public static void main(String[] args) {
        JFrame jFrame = new WebServerControl("Configure the web server");
        jFrame.setVisible(true);

    }

}
