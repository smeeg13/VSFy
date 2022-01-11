package Server;

import Server.ServerUI.FirstPage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.logging.Level;

public class Server {

    private static ServerSocket serverSocket;
    private static MyLogger serverLogger;

    static {
        try {
            serverLogger = setServerLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ;
    private static InetAddress localIP;
    static {
        try {
            localIP = InetAddress.getLocalHost();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            serverLogger.getLogger().log(Level.SEVERE, "exception thrown", e);
        }
    }
    private static final String ipString = String.valueOf(localIP);
    private static final String ip = ipString.substring(ipString.lastIndexOf("/") + 1);

    public static void main(String[] args) {
        try {
            //Create the log file of the month if it doesn't exist or complete the exiting one
            setServerLogger();
            //Launching of the first frame which will let us start the server
            FirstPage firstPage = new FirstPage();

        } catch (
                RuntimeException | IOException ex) {
            serverLogger.getLogger().log(Level.SEVERE, "exception thrown", ex);
            ex.printStackTrace();
        }
    }

    //Constructor
    public Server(ServerSocket serverSocket) {
        Server.serverSocket = serverSocket;
    }

    //Method to close the server
    public static void closeServerSocket(ServerSocket serverSocket) {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                // end program
                serverLogger.getLogger().setLevel(Level.INFO);
                serverLogger.getLogger().log(Level.INFO,"CLOSING SERVER SOCKET");
                serverLogger.getLogger().info("=========== program ends =============");
            }
        } catch (IOException e) {
            e.printStackTrace();
            serverLogger.getLogger().log(Level.SEVERE, "exception thrown", e);
        }
    }
//------------------------------------------------------------------------------------------------
//All Getters & Setters

    public static int getPort() {
        return 45007;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static InetAddress getLocalIP() {
        return localIP;
    }

    public static String getIp() {
        return ip;
    }

    public static MyLogger getLogr() {
        return serverLogger;
    }

    public static MyLogger setServerLogger() throws IOException {
        LocalDate currently = LocalDate.now();
        String currentsMonthYear = currently.getMonth() +"_"+ currently.getYear();
        String LogFileName = currentsMonthYear+".log";
        String LogFilePath = "Server/src/main/resources/Logs/";

        MyLogger log =new MyLogger( LogFilePath+LogFileName,currentsMonthYear+"_Server");
        return log;
    }
}
