package Server;

import Client.Player;
import Server.ServerUI.MainPage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;

public class ClientHandler implements Runnable {
    private static final MyLogger logr;
    static {
        logr = Server.getLogr();
    }

    //What every client have
    private String clientUsername;
    private int clientId;
    private String clientIPAddress;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String[] songsOnSrv;
    private final File MainFolder = new File("C:\\VSFY\\FilesToSend");
    private final File StoredFolder = new File("C:\\VSFY\\FilesOnServer");


    //List of all user currently connected
    private static ArrayList<ClientHandler> handlerArrayList = new ArrayList<>(); //GET LIST OF CLIENT FROM MAIN PAGE
    private int fileid;

    /**
     * Constructor
     *
     * @param clientSocketOnServer
     * @param id
     */
    public ClientHandler(Socket clientSocketOnServer, int id) {
        try {
            this.socket = clientSocketOnServer;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Read the username and the ip that will be entered by the client
            this.clientUsername = in.readLine();
            this.clientIPAddress = in.readLine();
            this.clientId = id;//Id is set by the mainpage of the server

            //Adding the new client to the list of users
            handlerArrayList.add(this);

//Just to be sure which client is in the list
            System.out.println("Connected Client list : ");
            for (ClientHandler cli : handlerArrayList) {
                System.out.println(cli.clientUsername);
            }
        } catch (IOException e) {
            logr.getLogger().log(Level.SEVERE, "exception thrown", e);
            closeClientHand(socket, in, out);
        }
    }

    /**
     * What is running on a different thread (What the server do for every client)
     */
    public void run() {
        try {
            //Always Listen to which command the client is sending
            // (What the client wants to do)
            String actionfromclient;
            do {
                try {
                    actionfromclient = in.readLine();

                    //If server receive something
                    if (actionfromclient != null) {
                        logr.getLogger().log(Level.INFO, clientUsername + " want to : " + actionfromclient);

                        //Server do something according to what the client action send is
                        switch (actionfromclient) {
                            case "Login":
                                // Reading all files store in the client's pc
                                songsOnSrv = readFilesFromClient();
                                //Tell client all files ar downloaded
                                MsgToClient("INFO : All files downloaded into the server");
                                break;

                            case "Playlist":
                                //Command send to user for him to listen
                                MsgToClient("## Filenames are going to be send");
                                sendFilesToClient(this);
                                //Infos send to user
                                MsgToClient("INFO : All files send to the client");
                                break;


                            case "Listen to":
                                //read from client which song he want to play
                                String SongToListen = in.readLine();
                                System.out.println("song name received : "+SongToListen);

                                //Command send to user for him to listen
                                MsgToClient("## Song is going to be send");

                                //Then do what's needed
                                SendSongToClient(SongToListen, MainFolder);

                                break;
                            case "Listen fromsrv":
                                //read from client which song he want to play
                                String SongToListenfromsrv = in.readLine();
                                System.out.println("song name received : "+SongToListenfromsrv);

                                //Command send to user for him to listen
                                MsgToClient("## Song from srv is going to be send");

                                //Then do what's needed
                                SendSongToClient(SongToListenfromsrv,StoredFolder);

                                break;

                            case "GetUsers":
                                //Command send to user for him to listen
                                MsgToClient("## Users are going to be send");
                                sendUserlist();
                                MsgToClient("## users sent");
                                break;

//                            case "Playlist Of":
//                                //Read which user has been choosed on the list given above
//                                String usrname = in.readLine();
//                                //Take back the selected client
//                                ClientHandler cliChoosed = null;
//                                for (ClientHandler ch : handlerArrayList){
//                                    if (ch.getClientUsername().equals(usrname)){
//                                        cliChoosed = ch;
//                                    }
//                                }
//
//                                if (cliChoosed != null) {
//                                    MsgToClient("## files of other user will be sent");
//                                    //Send to the user the song name [] of the selected user
//                                    sendFilesToClient(cliChoosed);
//                                }
//
//                                break;

                            case "Playlist Of":
                                //Read which user has been choosed on the list given above
                                String usrname = in.readLine();

                                //Take back the selected client
                                ClientHandler cliChoosed = null;
                                for (ClientHandler ch : handlerArrayList){
                                    if (ch.getClientUsername().equals(usrname)){
                                        cliChoosed = ch;
                                    }
                                }
                                //dire au client selectionné qu'il doit downloader ses files sur le serveur
                                MsgToSpecificClient(cliChoosed.clientUsername,"## Should download file to serv");

                                //Read how many files will be sent
                                String nbfiles =  in.readLine();

                                //For each file server saves it
                                for (int i = 0; i == Integer.parseInt(nbfiles); i++){
                                    receiveSongFromClient();
                                }

                                String confirm = in.readLine();

                                if (confirm.equals("All files completely sent")){
                                    //Si ok envoyer list des noms d'abord
                                    //ensuite si clic, server enverra file au client
                                    MsgToClient("## files of other user will be sent");
                                    //Send to the user the song name [] of the selected user
                                    sendFilesToClient(cliChoosed);
                                }
                                break;

                            case "JoinChat":
                                MsgToClient("## In the chat");
                                JoinChat();
                                break;

                            case "Logout":
                                MainPage.getModel().remove(clientId);
                                MainPage.getjFrame().validate();
                                closeClientHand(socket, in, out);
                                break;
                        }
                    }
                } catch (IOException e) {
                    logr.getLogger().log(Level.SEVERE, "exception thrown", e);
                    closeClientHand(socket, in, out);
                }
            } while (socket.isConnected());
        } catch (Exception e) {
            logr.getLogger().log(Level.WARNING,"Client socket not connected");
            closeClientHand(socket, in, out);
        }
    }

    /**
     * Method to give a file list to a user
     *
     *  @param clientHandler
     */
    public void sendFilesToClient(ClientHandler clientHandler) {
        boolean exit = false;
        int filesentid = 0;
        String[] filestosend = clientHandler.getSongsOnSrv();

        // send number of files in total
        MsgToClient(String.valueOf(filestosend.length));
        System.out.println("nb file sent : " + filestosend.length);

        do {
            if (filesentid >= filestosend.length) {
                // When all files are sent, send to server - FINISH - so he can stop listening for a filename
                MsgToClient("INFO : finish");
                exit = true;
                break;
            }
            // for each file in the folder send the path to the server
            MsgToClient(filestosend[filesentid]);
            System.out.println("name file sent : " + filestosend[filesentid]);

            filesentid++;
            System.out.println("State of file id :" + filesentid);

        } while (!exit);
    }

    /**
     * Method to Read for the filename list send by the user
     * as long as the client is not sending FINISH
     *
     *  @return
     */
    public String[] readFilesFromClient() {

        String filenameSent;
        boolean exit = false;
        fileid = 0;

        // read number of file that will be sent
        String nbFiles;
        try {
            nbFiles = in.readLine();
            System.out.println("nb file read from client : "+ nbFiles);

            //If nb files has been received then continue
            if (nbFiles != null) {
                //Creation of the song list
                this.songsOnSrv = new String[Integer.parseInt(nbFiles)];

                do {
                    //Read the filename sent
                    filenameSent = in.readLine();
                    System.out.println("name read from client : "+ filenameSent);

                    //If the user sent Finish it get out of the loop
                    if (filenameSent.equals("FINISH")) {
                        exit = true;
                        break;
                    }

                    //Adding the filename to the Sting []
                    songsOnSrv[fileid] = filenameSent;
                    fileid++;
                } while (!exit);
            }
            //To be sure filename are stored in the server
            System.out.println("List of file given by the client");
            for (String s : songsOnSrv) {
                System.out.println(s);
            }

        } catch (IOException e) {
        }
        return songsOnSrv;
    }

    /**
     * Method to send to the client a Message ( simple string)
     * or an Action to Do ( start by ##)
     *
     * @param msgToSend
     */
    public void MsgToClient(String msgToSend) {
        try {
            out.write(msgToSend);
            out.newLine();
            out.flush();

        } catch (IOException e) {
            logr.getLogger().log(Level.WARNING,"Client socket not connected");
            closeClientHand(socket, in, out);
        }

        System.out.println("Server has sent TO " + clientUsername + " : " + msgToSend);
        logr.getLogger().log(Level.INFO, "SERVER SEND TO " + clientUsername + " : " + msgToSend);
    }
    /**
     * Method to send to the client a Message ( simple string)
     * or an Action to Do ( start by ##)
     *
     * @param msgToSend
     */
    public void MsgToSpecificClient(String usrname,String msgToSend) {
        for (ClientHandler clientHandler : handlerArrayList) {
            try {
                if (clientHandler.clientUsername.equals(usrname)) {
                    clientHandler.out.write(msgToSend);
                    clientHandler.out.newLine();
                    clientHandler.out.flush();
                }
            } catch (IOException e) {
                logr.getLogger().log(Level.WARNING,"Client socket not connected");
                closeClientHand(socket, in, out);
            }
        }
        logr.getLogger().log(Level.INFO, "SERVER SEND ONLY TO  : "+usrname+" - " + msgToSend);
    }

    /**
     * Method to send to the client a list of all users connected
     *
     * @throws IOException
     */
  public void sendUserlist() throws IOException {
        boolean exit = false;
        int usersentid = 0;

        // send number of users in total
        MsgToClient(String.valueOf(handlerArrayList.size()));
        System.out.println("nb user sent : " +(handlerArrayList.size()));


        do {
            if (usersentid >= handlerArrayList.size()) {
                // When all files are sent, send to server - FINISH - so he can stop listening for a filename
                MsgToClient("INFO : finish");
                exit = true;
                break;
            }
            // for each file in the folder send the path to the server
            MsgToClient(handlerArrayList.get(usersentid).getClientUsername()+" : "+handlerArrayList.get(usersentid).getClientIPAddress());
            System.out.println("usr sent : " + handlerArrayList.get(usersentid).getClientUsername()+" : "+handlerArrayList.get(usersentid).getClientIPAddress());

            usersentid++;
            System.out.println("State of usr id :" + usersentid);

        } while (!exit);
    }


    /**
     * Method to Send a message to all users connected except the one that the msg came from
     *
     * @param msgToSend
     */
    public void broadcastMessage(String msgToSend) {
        for (ClientHandler clientHandler : handlerArrayList) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.out.write(msgToSend);
                    clientHandler.out.newLine();
                    clientHandler.out.flush();
                }
            } catch (IOException e) {
                logr.getLogger().log(Level.WARNING,"Client socket not connected");
                closeClientHand(socket, in, out);
            }
        }
        logr.getLogger().log(Level.INFO, "SERVER SEND TO EVERYONE : " + msgToSend);
    }

    /**
     * Method when the client want to join the chat
     *
     * @throws IOException
     */
    public void JoinChat() throws IOException {

        String msg;
        boolean exit= false;
        try {
            do {
                msg = in.readLine();
                System.out.println("msg from user : "+msg);

                if (msg.equals("Left Chat")){
                    exit = true;
                    break;
                }else {
                    broadcastMessage(msg);
                    System.out.println("msg transmit");
                }
            }while (!exit);
            MsgToClient("## Chat Closed");

        } catch (IOException e) {
            closeClientHand(socket, in, out);
        }
    }

    /**
     * Method to send 1 song to the client
     *
     * @param songName
     * @throws IOException
     */
    public void SendSongToClient(String songName, File Folder) throws IOException {

        String pathname = Folder.getAbsolutePath() +"\\"+ songName; //To have the absolute path of the song
        File myFile = new File(pathname);
        long myFileSize = Files.size(Paths.get(pathname));

        PrintWriter Pout2 = new PrintWriter(socket.getOutputStream(), true);
        //Send file size
        Pout2.println(myFileSize);
        System.out.println("File size sent : "+myFileSize);
        //Send File name
        Pout2.println(songName);
        System.out.println("File name sent : "+songName);

        byte[] mybytearray = new byte[(int) myFileSize];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

        bis.read(mybytearray, 0, mybytearray.length);
        //open outputStream on Socket
        OutputStream os = socket.getOutputStream();
        //Send byteArray
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();

    }

    public void receiveSongFromClient() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //Get file size
        int totalsize = Integer.parseInt(in.readLine());
        System.out.println("File size receive : " + totalsize);
        byte[] mybytearray = new byte[totalsize];
        //Get file name
        String filename = in.readLine();
        System.out.println("File name receive : " + filename);


        //To get all that is sending
        InputStream is = new BufferedInputStream(socket.getInputStream());
        System.out.println("Input received");

        Player player= new Player(filename,is);

        System.out.println("Player created");

        // Thread.sleep(300000);

        //Create the file with the filename we get from the Buffin
        FileOutputStream fos = new FileOutputStream(StoredFolder.getAbsolutePath() + "\\" + filename);
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
    }
    public void SendSongofOtherToCli(String songName) throws IOException {

        String pathname = StoredFolder.getAbsolutePath() +"\\"+ songName; //To have the absolute path of the song
        File myFile = new File(pathname);
        long myFileSize = Files.size(Paths.get(pathname));

        PrintWriter Pout2 = new PrintWriter(socket.getOutputStream(), true);
        //Send file size
        Pout2.println(myFileSize);
        System.out.println("File size sent : "+myFileSize);
        //Send File name
        Pout2.println(songName);
        System.out.println("File name sent : "+songName);

        byte[] mybytearray = new byte[(int) myFileSize];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

        bis.read(mybytearray, 0, mybytearray.length);
        //open outputStream on Socket
        OutputStream os = socket.getOutputStream();
        //Send byteArray
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();

    }

    /**
     * Method to remove a user from the list after they disconnected
     */
    public void CloseClientHandler() {
        //Delete this client from the clients arraylist
        if (handlerArrayList != null) {
            for (int i = 0; i < handlerArrayList.size(); i++) {
                if (handlerArrayList.get(i).getClientUsername().equals(clientUsername)) {
                    handlerArrayList.remove(i);
                    MainPage.getModel().remove(clientId);
                    MainPage.setIdClient(MainPage.getIdClient()-1);
                    MainPage.getjFrame().validate();
                }
            }
            broadcastMessage("SERVER :: " + clientUsername + " is disconnected");
        }
    }

    /**
     * Method to close all items created
     *
     * @param socket
     * @param bufReader
     * @param bufWriter
     */
    public void closeClientHand(Socket socket, BufferedReader bufReader, BufferedWriter bufWriter) {
        CloseClientHandler();
        System.out.println("----" + clientUsername + " Connection has been closed ----");

        // end program
        logr.getLogger().setLevel(Level.INFO);
        logr.getLogger().info("CLOSING THE ClientHandler for client " + clientUsername);

        try {
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
            //e.printStackTrace();
            logr.getLogger().log(Level.SEVERE, "exception thrown", e);
        }
    }

//------------------------------------------------------------------------------------------------
//All Getters & Setters
//------------------------------------------------------------------------------------------------

    public Socket getSocket() {
        return socket;
    }

    public int getFileid() {
        return fileid;
    }

    public String[] getSongsOnSrv() {
        return songsOnSrv;
    }

    public BufferedReader getBufReader() {
        return in;
    }

    public BufferedWriter getBufWriter() {
        return out;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientIPAddress() {
        return clientIPAddress;
    }

    public static ArrayList<ClientHandler> getHandlerArrayList() {
        return handlerArrayList;
    }

    @Override
    public String toString() {
        return clientUsername + '\t' +
                " IP : " + clientIPAddress;
    }
}