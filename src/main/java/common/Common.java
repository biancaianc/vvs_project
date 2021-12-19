package common;

import java.net.ServerSocket;

public class Common {
    public static int currentState=1;
    public static String rootDirectory="src/main/resources/TestSite";
    public static String maintenanceDirectory="src/main/resources/TestSite/maintenance.html";
    public static ServerSocket serverSocket;
    public static int port=10051;
    public static String lastRoot=rootDirectory;
}
