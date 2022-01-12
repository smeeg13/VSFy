package Client.ClientUI;

import Client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Playlist {
    //WHEN A USR SELECT ONE FILE HE CAN LISTEN TO IT
    //SO THE SERVER WILL SEND THE ENTIRE FILE ACCORDING TO THE FILENAME SELECTED
    //IT WILL BE LISTENED AND THE CLIENT CAN PAUSE IT

    private static JFrame jFrame;
    private String[] Files;
    private static DefaultListModel<String> model = new DefaultListModel<>();

    public Playlist(Client client) {
       //Creating and implementing components of the frame
        jFrame = new JFrame();

        JPanel jPanelTitle = new JPanel();
        jPanelTitle.setPreferredSize(new Dimension(500,100));
        jPanelTitle.setLayout(new BoxLayout(jPanelTitle,BoxLayout.Y_AXIS));

        String str = client.getClientUsername();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);

        JLabel jlTitle = new JLabel(cap +"'s Playlist");
        jlTitle.setFont(new Font("Arial",Font.BOLD,20));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jlistTitle = new JLabel("My files : ");
        jlistTitle.setFont(new Font("Arial",Font.BOLD,18));
        jlistTitle.setBorder(new EmptyBorder(10,0,0,0));
        jlistTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        jPanelTitle.add(jlTitle);
        jPanelTitle.add(jlistTitle);

        JPanel jPanelListSongs = new JPanel();
        jPanelListSongs.setLayout(new BoxLayout(jPanelListSongs, BoxLayout.Y_AXIS));
        jPanelListSongs.setPreferredSize(new Dimension(500, 100));


        JList jListSongs = new JList();
        jListSongs.setModel(model); // the model is set before displaying this playlist frame

        jListSongs.setVisibleRowCount(4);
        jListSongs.setPreferredSize(new Dimension(400, 100));

        //Selection Listener to access Song Details
        jListSongs.getSelectionModel().addListSelectionListener(e -> {
            //Pop up to be sure
            int answer = JOptionPane.showConfirmDialog(jFrame, "Are you sure you want to View this song in details ? ", "Confirm", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) { //if YES
                //Take the song name choosed
                String songselected = (String) jListSongs.getSelectedValue();
                //Open it's frame
                System.out.println("List selection of : "+songselected);
                //Tell the server we want to listen to something
                SongDetails SongFrame = new SongDetails(client,songselected);
            }
        });
        JScrollPane jScrollPane = new JScrollPane(jListSongs);
        jScrollPane.setPreferredSize(new Dimension(500,100));
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jPanelListSongs.add(jScrollPane);


        JPanel jpBottom = new JPanel();
        jpBottom.setLayout(new BoxLayout(jpBottom,BoxLayout.Y_AXIS));
        jpBottom.setPreferredSize(new Dimension(500,80));



        JButton jbAccesOtherUsr = new JButton("Access Other User's Playlist");
        jbAccesOtherUsr.setPreferredSize(new Dimension(150,45));
        jbAccesOtherUsr.setFont(new Font("Arial",Font.BOLD,16));
        jbAccesOtherUsr.addActionListener(e -> {
            jFrame.dispose();
            Allusr allusr = new Allusr(client);

        });
        jpBottom.add(jbAccesOtherUsr);
        JButton jbBackMenu = new JButton("Back to Menu");
        jbBackMenu.setPreferredSize(new Dimension(150,45));
        jbBackMenu.setFont(new Font("Arial",Font.BOLD,16));
        jbBackMenu.addActionListener(e -> {
            jFrame.dispose();
            Menu menu = new Menu(client);
        });
        jpBottom.add(jbBackMenu);
        //Setup  UI
        jFrame.add(jPanelTitle);
        jFrame.add(jPanelListSongs);
        jFrame.add(jpBottom);
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Pour que le prog se termine en fermant la frame
        jFrame.setSize(500,500); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        //Layout Par defaut = CardLayout
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Pop up to be sure
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == 0) { //if YES
                    client.sendToServer("Logout");

                    //Close connection with the server
                    Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                    //Delete this client from the client handler list
                    // Close frame
                    jFrame.dispose();
                    System.exit(0);
                }
                else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }

        });
    }

    public static JFrame getjFrame() {
        return jFrame;
    }

    public static void setModel(DefaultListModel<String> model) {
        Playlist.model = model;
    }
}