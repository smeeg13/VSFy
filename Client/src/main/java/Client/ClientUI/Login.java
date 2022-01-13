package Client.ClientUI;

import Client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Login {
    private static final int port = 45007;
    private static InetAddress ipAddress;
    private final String ip = Client.getIp();

    private final JFrame jFrame;
    private final JTextField jtfUsername;
    private final JLabel jlIP;
    private JTextField jtfIP = null;
    private final JCheckBox jcbLocalIP;
    private ImageIcon imageIcon = new ImageIcon("Client/src/main/resources/Logo.jpg");


    public Login() {
        jFrame = new JFrame();

        JPanel jpTitle = new JPanel();
        jpTitle.setLayout(new BoxLayout(jpTitle, BoxLayout.Y_AXIS));
        jpTitle.setPreferredSize(new Dimension(500,80));

        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel jlPic = new JLabel(imageIcon);
        jlPic.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpTitle.add(jlPic);

        JLabel jTitle = new JLabel("Welcome to VSfy !");
        jTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jpTitle.add(jTitle);

        JPanel jPanelLogin = new JPanel();
        jPanelLogin.setPreferredSize(new Dimension(500,155));

        JLabel jlUserName = new JLabel("Enter Your Username : ");
        jlUserName.setFont(new Font("Arial", Font.BOLD, 18));
        jlUserName.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        jtfUsername = new JTextField();
        jtfUsername.setColumns(30);
        jPanelLogin.add(jlUserName);
        jPanelLogin.add(jtfUsername);

        //To know if the server run on the same ip of the user or not
        jcbLocalIP = new JCheckBox("the server use the local IP Address !");
        jcbLocalIP.setSelected(true);
        jcbLocalIP.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    jlIP.setVisible(true);
                    jtfIP.setVisible(true);
                }else {
                    jlIP.setVisible(false);
                    jtfIP.setVisible(false);
                }
            }
        });
        jPanelLogin.add(jcbLocalIP);

        jlIP = new JLabel("Enter the IP of the server you want to connect to : ");
        jlIP.setFont(new Font("Arial", Font.BOLD, 14));
        jlIP.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlIP.setAlignmentX(Component.CENTER_ALIGNMENT);

        jtfIP = new JTextField();
        jtfIP.setColumns(30);
        jlIP.setVisible(false);
        jtfIP.setVisible(false);

        jPanelLogin.add(jlIP);
        jPanelLogin.add(jtfIP);

        JLabel jlPort = new JLabel("Listening port of the server is always : "+ port);
        jlPort.setFont(new Font("Arial", Font.BOLD, 14));
        jlPort.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlPort.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelLogin.add(jlPort);

        JPanel jPanelBut = new JPanel();
        jPanelBut.setPreferredSize(new Dimension(500,60));
        jPanelBut.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jbLogin = new JButton("Login");
        jbLogin.setPreferredSize(new Dimension(150, 55));
        jbLogin.setFont(new Font("Arial", Font.BOLD, 20));
        jPanelBut.add(jbLogin);


        jbLogin.addActionListener(e -> {
            //Do things with the server

            String usrname = jtfUsername.getText();
            if (!jcbLocalIP.isSelected()){
                try {
                    ipAddress = InetAddress.getByName(jtfIP.getText()) ;
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                try {
                        ipAddress = InetAddress.getLocalHost();
                    } catch (UnknownHostException ex) {
                        ex.printStackTrace();
                    }
            }
            System.out.println("@@@@ IP used : "+ipAddress+" @@@");
            //Creation of the clientsocket and the client using the username enter above
            assert ipAddress != null;
            Socket clientsocket = null;
            try {
                clientsocket = new Socket(ipAddress, port);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            assert clientsocket != null;
            Client client = null;
            try {
                client = new Client(clientsocket, usrname, ipAddress);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            System.out.println("@@ You are connected as " + client.getClientUsername()+" with the ip "+ip);
            System.out.println("List of files and directories in the FilesToSend directory of the client :");
            for (String clientSong : client.getSongOnTheClient()) {
                System.out.println(clientSong);
            }
            //Send them into the server
            client.sendToServer("Login");
            //client.checkVarEnv();
            client.sendFileToServer();
            //Read confirmation from server
            client.listenToServer();
            //Go to next frame and close the current
            jFrame.dispose();
            Menu menu = new Menu(client);
            System.out.println("@@ Login made -> Go to Menu");
        });

        //Setup  UI
        jFrame.add(jpTitle);
        jFrame.add(jPanelLogin);
        jFrame.add(jPanelBut);
        jFrame.setTitle("Client - Login");
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Pour que le prog se termine en fermant la frame
        jFrame.setSize(500, 500); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        //Layout Par defaut = CardLayout
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Pop up to be sure
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == JOptionPane.YES_OPTION) { //if YES
                    // end program
                    jFrame.dispose();
                    System.exit(0);
                } else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
}