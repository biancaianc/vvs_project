//package cli;
//
//import common.Common;
//import server.WebServer;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.nio.charset.StandardCharsets;
//
//
//public class WebServerCommandLine {
//
//    public static void main(String[] args) throws IOException {
//        System.out.println("0-running");
//        System.out.println("1-stopped");
//        System.out.println("2-maintenance");
//        InputStreamReader in = new InputStreamReader(System.in, StandardCharsets.UTF_8);
//        BufferedReader reader = new BufferedReader(in);
//        String option=reader.readLine();
//        if (option != null)
//            Common.currentState = Integer.parseInt(option);
//
//        while (true) {
//         option = reader.readLine();
//            if (!option.contains("SERVER:") && !option.isEmpty()) {
//                switch (Common.currentState) {
//                    case 0: {
//                        Common.currentState = 0;
//                        WebServer webServer = new WebServer();
//                        webServer.start();
//                        break;
//                    }
//
//
//                    case 1: {
//                        Common.currentState = 1;
//                        System.out.println("Server is stopped.");
//                        break;
//                    }
//                    case 2: {
//                        Common.currentState = 2;
//                        break;
//                    }
//                    default:
//                        System.out.println("Not a valid state");
//                }
//            }
//
//        }
//
//    }
//
//
//}
