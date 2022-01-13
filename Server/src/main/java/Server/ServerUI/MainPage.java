package Server.ServerUI;

import Server.ClientHandler;
import Server.MyLogger;
import Server.Server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;


public class MainPage implements ActionListener {


    private static final InetAddress localIP = Server.getLocalIP();
    private static final String ip = Server.getIp(); //IP
    private static final int port = Server.getPort(); //Port

    private ServerSocket serverSocket;
    private static int idClient;
    private static ArrayList<ClientHandler> handlerArrayList = new ArrayList<>(); //All Users
    private static final ExecutorService pool = Executors.newFixedThreadPool(4); //FOR 4 USER ONLY
    private final MyLogger logr = Server.getLogr();

    private static  JFrame jFrame;
    private static DefaultListModel<ClientHandler> model = new DefaultListModel<>();
    private final JButton jbCloseServer;


    public MainPage() throws IOException {
        jFrame = new JFrame();
        BoxLayout boxLayout = new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS);
        jFrame.setLayout(boxLayout);

        JPanel jpTitle = new JPanel();
        jpTitle.setPreferredSize(new Dimension(500, 80));

        JLabel jlTitle = new JLabel("Server's Dashboard");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 22));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpTitle.add(jlTitle);

        JPanel jPanelListUser = new JPanel();
        jPanelListUser.setLayout(new BoxLayout(jPanelListUser, BoxLayout.Y_AXIS));

        jPanelListUser.setPreferredSize(new Dimension(500, 420));

        JLabel jlListTitle = new JLabel("All Connected Users : ");
        jlListTitle.setFont(new Font("Arial", Font.BOLD, 16));
        jlListTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jPanelListUser.add(jlListTitle);


        JList jListUsrs = new JList();
        jListUsrs.setModel(model);

        jListUsrs.setVisibleRowCount(4);
        jListUsrs.setPreferredSize(new Dimension(400, 100));
        JScrollPane jScrollPane = new JScrollPane(jListUsrs);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jPanelListUser.add(jScrollPane);


        JPanel jPanelCloseServer = new JPanel();
        jPanelCloseServer.setPreferredSize(new Dimension(500, 80));
        GridBagLayout gridLayout = new GridBagLayout();
        jPanelCloseServer.setLayout(gridLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel jlIPServer = new JLabel(" Ip of the server " + ip);
        jlIPServer.setFont(new Font("Arial", Font.BOLD, 10));
        jlIPServer.setBorder(new EmptyBorder(20, 0, 10, 0));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 3;
        gridLayout.setConstraints(jlIPServer, c);
        jPanelCloseServer.add(jlIPServer);

        JLabel jlPortServer = new JLabel("Listen on port : " + port);
        jlPortServer.setFont(new Font("Arial", Font.BOLD, 10));
        jlPortServer.setBorder(new EmptyBorder(20, 0, 10, 0));
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 3;
        gridLayout.setConstraints(jlPortServer, c);
        jPanelCloseServer.add(jlPortServer);

        jbCloseServer = new JButton("Close Server");
        jbCloseServer.setPreferredSize(new Dimension(100, 50));
        jbCloseServer.setFont(new Font("Arial", Font.BOLD, 10));
        c.gridx = 3;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 2;
        gridLayout.setConstraints(jlPortServer, c);
        jbCloseServer.addActionListener(this);
        jPanelCloseServer.add(jbCloseServer);

        //Setup  UI
        jFrame.add(jpTitle);
        jFrame.add(jPanelListUser);
        jFrame.add(jPanelCloseServer);
        jFrame.setTitle("Server - Dashboard");
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setSize(600, 600); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    closeFrame();
            }
        });
        ImageIcon img = new ImageIcon("Server/src/main/resources/Logo.jpg");
        jFrame.setIconImage(img.getImage());

        //Starting the server
        idClient = 0;
        startServer();
    }

    public void startServer() {
//Creation of the Server socket
        try {
            this.serverSocket = new ServerSocket(port, 10, localIP);
        } catch (IOException ex) {
          //  ex.printStackTrace();
            logr.getLogger().log(Level.SEVERE, "exception thrown", ex);
        }
        Server server = new Server(serverSocket);

        String lineSeparator = System.getProperty("line.separator");//To make a return to new line in a log
        logr.getLogger().log(Level.INFO, "STARTING THE SERVER with IP : " + ip + ", on Port : " + port + lineSeparator + "Waiting for client's connection");

//Thread to be able to accept a new client
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                Socket socketClient = null;
                try {
//Accepting the client
                    socketClient = serverSocket.accept();
                } catch (IOException ex) {
                    Server.closeServerSocket(Server.getServerSocket());
                }

                if (socketClient != null) {
//Creating the new clienthandler
                    ClientHandler newClientH = new ClientHandler(socketClient, idClient); //
                    logr.getLogger().log(Level.INFO, "The Client " + newClientH.getClientUsername() + " (N° " + idClient + " With the IP " + newClientH.getClientIPAddress() + ") has connected");
                    idClient++; //increment of 1 for the next client
                    //Add the new client into the JList Model
                    model.add(handlerArrayList.size()-1, newClientH);

//Launch the run method in clienthandler for this client
                    pool.execute(newClientH);
                }
            }
        }).start();

        handlerArrayList = ClientHandler.getHandlerArrayList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbCloseServer) {
            //Pop up to be sure
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
            if (answer == JOptionPane.YES_OPTION) { //if YES
                closeFrame();
            }
        }
    }

//------------------------------------------------------------------------------------------------
//All Getters

    public static DefaultListModel<ClientHandler> getModel() {
        return model;
    }

    public static int getIdClient() {
        return idClient;
    }

    public static void setIdClient(int idClient) {
        MainPage.idClient = idClient;
    }

    public static JFrame getjFrame() {
        return jFrame;
    }

    public void closeFrame(){
        //Pop up to be sure
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
        if (answer == 0) { //if YES
            //Close connections with the server
            if (handlerArrayList.size() != 0) {
                //Close socket of every client connected
                for (ClientHandler newClientH : handlerArrayList) {
                    newClientH.closeClientHand(newClientH.getSocket(), newClientH.getBufReader(), newClientH.getBufWriter());
                }
            }
            //Close server
            Server.closeServerSocket(serverSocket);
            //Close frame
            jFrame.dispose();
            System.exit(0);
        } else {
            jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }
}

