package Client;

import Client.ClientUI.Allusr;
import Client.ClientUI.Login;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static Client.ClientUI.Playlist.setModel;

public class Client {
    private Socket socket;
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;

    private String clientUsername;
    private static InetAddress ipAddress;

    static {
        try {
            ipAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static String ipString = String.valueOf(ipAddress);
    static String ip = ipString.substring(ipString.lastIndexOf("/") + 1);
    private String[] SongOnTheClient;
    private File sendFoler = new File("C:\\VSFY\\FilesToSend");
    private String[] filesFromServer;
    private File receiveFolder = new File("C:\\VSFY\\FilesToReceive");
    private String[] usersConnected;
    private DefaultListModel<String> Usersmodel = new DefaultListModel<>();


    private static final String VARIABLE_ENVIRONNEMENT = "VSFY"; // Nme of the Variable on the PC

    public static void main(String[] args) {
        //Launching the Client Frame
        Login login = new Login();
    }

    //Constructor
    public Client(Socket socket, String clientUsername, InetAddress ipAddress) throws UnknownHostException {
        try {
            //Initialization of variable according to what's given on parameters
            this.socket = socket;
            this.clientUsername = clientUsername;
            setIpAddress(ipAddress);
            this.bufWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //To send To server the username that is the first entry from the client
            bufWriter.write(clientUsername);
            bufWriter.newLine();
            bufWriter.flush();

            //To send the user IP
            bufWriter.write(ip);
            bufWriter.newLine();
            bufWriter.flush();

            //Get the files stored in the client's pc folder
            setSongOnTheClient();

        } catch (IOException e) {
            e.printStackTrace();
            ExitApplication(socket, bufReader, bufWriter);
        }
    }

    //Method to send the action the client want to do to the server
    public void sendToServer(String action) {
        try {
            this.getBufWriter().write(action);
            bufWriter.newLine();
            bufWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            ExitApplication(socket, bufReader, bufWriter);
        }
    }

    //Method to give the client's file list to the server
    public void sendFileToServer() {
        try {
            boolean exit = false;
            int filesentid = 0;

            // send number of files in total
            bufWriter.write(String.valueOf(getSongOnTheClient().length));
            bufWriter.newLine();
            bufWriter.flush();
            System.out.println("nb file sent : " + getSongOnTheClient().length);

            do {
                if (filesentid >= getSongOnTheClient().length) {
                    // When all files are sent, send to server - FINISH - so he can stop listening for a filename
                    sendToServer("FINISH");
                    exit = true;
                    break;
                }
                // for each file in the folder send the path to the server
                sendToServer(getSongOnTheClient()[filesentid]);
                System.out.println("name file sent : " + getSongOnTheClient()[filesentid]);

                filesentid++;
                System.out.println("State of file id :" + filesentid);

            } while (!exit);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Method that is running on a separate thread to be able to listen for every server responses
    public void listenToServer() {
        final String[] ConfirmDownload = new String[1];
        final String[] ConfirmSend = new String[1];

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean exit = false;
                int fileid = 0;
                int userid = 0;
                try {
                    //Confirmation that the client's file list has been given to srv
                    ConfirmDownload[0] = bufReader.readLine();
                    System.out.println(ConfirmDownload[0]);

                    while (socket.isConnected()) {

                        //Receive a SERVER COMMAND
                        String srvCommand = bufReader.readLine();
                        switch (srvCommand) {

                            case "## Filenames are going to be send":
                                //Client receive how many filename srv will send
                                String nbfile = bufReader.readLine();
                                System.out.println("nb files received : " + nbfile);

                                if (nbfile != null) {
                                    filesFromServer = new String[Integer.parseInt(nbfile)];
                                    do {
                                        //Receive the filename
                                        String filenameSent = bufReader.readLine();
                                        System.out.println("Filename received : " + filenameSent);

                                        //if the filename is finish then all files has been sent
                                        if (filenameSent.equals("INFO : finish")) {
                                            exit = true;
                                            break;
                                        } else {
                                            //Otherwise we need to add the filename to our String[]
                                            System.out.println("its file id : " + fileid);
                                            filesFromServer[fileid] = filenameSent;
                                            fileid++;
                                        }

                                    } while (exit == false);

                                    //Adding all new filename to the model of the JList that will be displayed
                                    DefaultListModel<String> Playlistmodel = new DefaultListModel();
                                    System.out.println("List of file given by the SERVER");
                                    for (String s : filesFromServer) {
                                        System.out.println(s);
                                        Playlistmodel.addElement(s);
                                    }
                                    //Update the model on the Class Playlist
                                    setModel(Playlistmodel);
                                }
                                break;
                            case "## Users are going to be send":
                                //Client receive how many Users srv will send
                                String nbusr = bufReader.readLine();
                                System.out.println("nb users received : " + nbusr);

                                if (nbusr!= null){

                                    usersConnected = new String[Integer.parseInt(nbusr)];

                                    do {
                                        //Receive the filename
                                        String usrreceive = bufReader.readLine();
                                        System.out.println("usr received : " + usrreceive);

                                        //if the filename is finish then all files has been sent
                                        if (usrreceive.equals("INFO : finish")) {
                                            exit = true;
                                            break;
                                        } else {
                                            //Otherwise we need to add the filename to our String[]
                                            System.out.println("its usr id : " + userid);
                                            usersConnected[userid] = usrreceive;
                                            userid++;
                                        }

                                    } while (exit == false);
                                }
//
                                //Adding all new filename to the model of the JList that will be displayed
                                System.out.println("List of users given by the SERVER");
                                for (int i = 0 ; i<= usersConnected.length-1; i++) {
                                    System.out.println(usersConnected[i]);
                                    Usersmodel.addElement(usersConnected[i]);
                                }
                                //Update the model on the Class Playlist
                                Allusr.setModel(Usersmodel);
                                break;

                            case "## Song is going to be send":
                                //Get file size
                                int totalsize = Integer.parseInt(bufReader.readLine());
                                byte[] mybytearray = new byte[totalsize];
                                //Get file name
                                String filename = bufReader.readLine();

                                //To get all that is sending
                                InputStream is = new BufferedInputStream(socket.getInputStream());

                                    Player player = null;
                                    try {
                                        player = new Player(is);
                                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                                        e.printStackTrace();
                                    }

                                    player.play();
                                   // Thread.sleep(300000);

                                    //Create the file with the filename we get from the Buffin
                                    FileOutputStream fos = new FileOutputStream(receiveFolder.getAbsolutePath()+"\\" + filename);
                                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                                    int byteReadTot = 0;
                                    while (byteReadTot < totalsize) {
                                        //To place all the byte i receive in my bytearray to store it in a file on my disk
                                        int byteRead = is.read(mybytearray, 0, mybytearray.length);
                                        byteReadTot += byteRead;
                                        System.out.println("byte read : " + byteReadTot);
                                        bos.write(mybytearray, 0, byteRead);

                                    }

                                    bos.close();



                                break;
                        }
                        break;
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
    }


//    public void playSong() throws IOException, InterruptedException {
//        int totalsize = Integer.parseInt(bufReader.readLine());
//        String filename = bufReader.readLine();
//        byte[] mybytearray = new byte[totalsize];
//
//        //To get all that is sending
//        InputStream is = new BufferedInputStream(socket.getInputStream());
//
//
//        if (filename.endsWith(".wav")) {
//            Player player = null;
//            try {
//                player = new Player(is);
//            } catch (UnsupportedAudioFileException | IOException e) {
//                e.printStackTrace();
//                logr.getLogr().log(Level.SEVERE, "exception thrown", e);
//            } catch (LineUnavailableException e) {
//                e.printStackTrace();
//                logr.getLogr().log(Level.SEVERE, "exception thrown", e);
//            }
//            System.out.println("Do you want to play the song ? yes or no :");
//           // if (scanner.nextLine() == "yes")
//                player.play();
//            Thread.sleep(300000);
//
//            //Create the file with the filename we get from the Buffin
//            FileOutputStream fos = new FileOutputStream("c://received//" + filename);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            int byteReadTot = 0;
//            while (byteReadTot < totalsize) {
//                //To place all the byte i receive in my bytearray to store it in a file on my disk
//                int byteRead = is.read(mybytearray, 0, mybytearray.length);
//                byteReadTot += byteRead;
//                System.out.println("byte read : " + byteReadTot);
//                bos.write(mybytearray, 0, byteRead);
//
//            }
//
//            bos.close();
//
//        }
//    }

    public static void ExitApplication(Socket socket, BufferedReader bufReader, BufferedWriter bufWriter) {
        closeAll(socket, bufReader, bufWriter);
        System.exit(0);
    }

    //Method to close all items created
    public static void closeAll(Socket socket, BufferedReader bufReader, BufferedWriter bufWriter) {
        try {
            System.out.println("----Connection has been Closed----");
            if (bufReader != null) {
                bufReader.close();
            }
            if (bufWriter != null) {
                bufWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            //To send a message in the chat
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String msgToSend = scanner.nextLine();
                if (msgToSend.equals("quit")) {
                    //
                } else {
                    bufWriter.write(clientUsername + " :: " + msgToSend);
                    bufWriter.newLine();
                    bufWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ExitApplication(socket, bufReader, bufWriter);
        }
    }

//------------------------------------------------------------------------------------------------
//All Getters & Setters

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufReader() {
        return bufReader;
    }

    public BufferedWriter getBufWriter() {
        return bufWriter;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public String[] getSongOnTheClient() {
        return SongOnTheClient;
    }

    public void setSongOnTheClient() {
        //get the list of all the existing files in the FileToSend directory
        File folderPath = new File("C:\\VSFY\\FilesToSend");

        //List of all files and directories And put them into the client
        this.SongOnTheClient = folderPath.list();
    }

    public static InetAddress getIpAddress() {
        return ipAddress;
    }

    public static void setIpAddress(InetAddress ipAddress) {
        Client.ipAddress = ipAddress;
    }

    public static String getIp() {
        return ip;
    }

    public String[] getFilesFromServer() {
        return this.filesFromServer;
    }

    public void setFilesFromServer(String[] filesFromServer) {
        this.filesFromServer = filesFromServer;
    }

    public int getArrayLength() {

        return filesFromServer.length;
    }

}

