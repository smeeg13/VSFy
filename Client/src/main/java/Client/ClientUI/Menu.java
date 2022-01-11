package Client.ClientUI;

import Client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Menu implements ActionListener {

    private final JFrame jFrame;
    private final JPanel jPanel;
    private final JButton jbLogout;

    private final JButton jbShowPlaylist;
    private final JButton jbGetAllUsers;
    private JButton AskUsersPlaylist;
    private JButton GiveAccessToPlaylist;
    private JButton jbEnterChat;
    private JLabel jLabel;

    private final Client client;
    private String[] filesreceived;

    public Menu(Client client) {
        this.client = client;
        jFrame = new JFrame();

        jPanel = new JPanel();
        GridBagLayout gridLayout = new GridBagLayout();
        jPanel.setLayout(gridLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        jPanel.setBorder(new EmptyBorder(75, 0, 10, 0));

        jbShowPlaylist = new JButton("My Playlists");
        jbShowPlaylist.setPreferredSize(new Dimension(150, 75));
        jbShowPlaylist.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        gridLayout.setConstraints(jbShowPlaylist, c);
        jbShowPlaylist.addActionListener(this);
        jPanel.add(jbShowPlaylist);


        jbGetAllUsers = new JButton("Access Other Users");
        jbGetAllUsers.setPreferredSize(new Dimension(150, 75));
        jbGetAllUsers.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 2;
        c.gridy = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        gridLayout.setConstraints(jbGetAllUsers, c);
        jbGetAllUsers.addActionListener(this);
        jPanel.add(jbGetAllUsers);


        //AskUsersPlaylist= new JButton();
        //GiveAccessToPlaylist= new JButton();
//        jbEnterChat = new JButton("Enter Chat");
//        jbEnterChat.setPreferredSize(new Dimension(150,75));
//        jbEnterChat.setFont(new Font("Arial",Font.BOLD,20));
//        c.gridx = 0;
//        c.gridy = 2;
//        c.gridheight = 1;
//        c.gridwidth = 2;
//        gridLayout.setConstraints(jbEnterChat,c);
//        jbEnterChat.addActionListener(this);
//        jPanel.add(jbEnterChat);

        jbLogout = new JButton("Logout");
        jbLogout.setPreferredSize(new Dimension(150, 75));
        jbLogout.setFont(new Font("Arial", Font.BOLD, 20));
        c.gridx = 2;
        c.gridy = 3;
        c.gridheight = 1;
        c.gridwidth = 2;
        gridLayout.setConstraints(jbLogout, c);
        jbLogout.addActionListener(this);
        jPanel.add(jbLogout);


        //Setup  UI
        jFrame.add(jPanel);
        jFrame.setTitle("Client - " + client.getClientUsername() + "'s Dashboard");
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setSize(500, 500); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        //Layout Par defaut = CardLayout
        ImageIcon img = new ImageIcon("OtherClass/src/main/resources/OtherRessources/Logo.jpg");
        jFrame.setIconImage(img.getImage());
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Pop up to be sure
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == 0) { //if YES

                    //Close connection with the server
                    if (client != null) {
                        Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                        client.sendToServer("Logout");
                        //Close frame
                        jFrame.dispose();
                    }
                    System.exit(0);
                } else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    //Method linked to buttons
    @Override
    public void actionPerformed(ActionEvent e) {

        //Server should send filenames that will be displayed on the frame
        if (e.getSource() == jbShowPlaylist) {
            client.sendToServer("Playlist");
            System.out.println("@@ Sent playlist command to serv");

            //Display the new frame
            jFrame.dispose();
            //Then open the fram which will display user's song
            Playlist playlist = new Playlist(client);
            System.out.println("## Menu -> User's Playlist");
        }

        if (e.getSource() == jbGetAllUsers) {
            client.sendToServer("GetUsers");
            System.out.println("@@ Sent getusers command to serv");


            jFrame.dispose();
            Allusr allUsr = new Allusr(client);
            System.out.println("## Menu -> Users list");

        }

//        if (e.getSource()==jbEnterChat){
//            client.sendActionToServer("JoinChat");
//            client.listenToMessage();
//            client.sendMessage();
//        }

        if (e.getSource() == jbLogout) {
            //Pop up to be sure
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
            if (answer == JOptionPane.YES_OPTION) { //if YES
                //Close connection with the server
                Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                client.sendToServer("Logout");
                //Pour supprimer le clienthandler de la liste et fermer ses socket & buffer
//Close frame
                jFrame.dispose();
            }
        }
    }
}