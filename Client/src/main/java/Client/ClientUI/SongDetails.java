package Client.ClientUI;

import Client.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SongDetails implements ActionListener {

        private final JFrame jFrame;
        private JButton jbBackMenu;
        private JButton jbBackToPlaylist;
        private final Client client;
        private JButton jbPause;
        private JButton jButton;
        private JLabel jlsongNameChoose;

    private final ImageIcon iconPlay = new ImageIcon(new ImageIcon("Client/src/main/resources/But_Play.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
    private  final ImageIcon iconPause = new ImageIcon(new ImageIcon("Client/src/main/resources/But_Pause.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));


    public SongDetails(final Client client, String songNameChoosed) {
            //Creating and implementing components of the frame
            jFrame = new JFrame();
            this.client = client;

            JPanel jPanelTitle = new JPanel();
            jPanelTitle.setLayout(new BoxLayout(jPanelTitle,BoxLayout.Y_AXIS));
            JLabel jlTitle = new JLabel("VSfy Song Player");
            jlTitle.setFont(new Font("Arial",Font.BOLD,25));
            jlTitle.setBorder(new EmptyBorder(20,0,10,0));
            jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel jlFilename = new JLabel("Song choosed : ");
            jlFilename.setFont(new Font("Arial",Font.BOLD,20));
            jlFilename.setBorder(new EmptyBorder(50,0,0,0));
            jlFilename.setAlignmentX(Component.CENTER_ALIGNMENT);

            jlsongNameChoose = new JLabel(songNameChoosed);
            jlsongNameChoose.setFont(new Font("Arial",Font.ITALIC,20));
            jlsongNameChoose.setBorder(new EmptyBorder(50,0,0,0));
            jlsongNameChoose.setAlignmentX(Component.CENTER_ALIGNMENT);

            jPanelTitle.add(jlTitle);
            jPanelTitle.add(jlFilename);
            jPanelTitle.add(jlsongNameChoose);

            JPanel jpButCommandSong = new JPanel();
            jpButCommandSong.setLayout(new BoxLayout(jpButCommandSong, BoxLayout.X_AXIS));
            jpButCommandSong.setPreferredSize(new Dimension(500, 220));

            jButton = new JButton();


           // Icon iconPlay = new ImageIcon("Client/src/main/resources/But_Play.png");

            jButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            jButton.setBackground(new Color(211,211,211));
            jButton.setPreferredSize(new Dimension(5,5));
            jButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            jButton.addActionListener(this);

            jbPause = new JButton();

//            //Icon iconPause = new ImageIcon("Client/src/main/resources/But_Pause.png");
//            jbPause.setIcon(iconPause);
//            jbPause.setPreferredSize(new Dimension(5,5));
//
//            jbPause.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//            jbPause.setBackground(new Color(211,211,211));
//            jbPause.setAlignmentX(Component.RIGHT_ALIGNMENT);
//            jbPause.addActionListener(this);

            jpButCommandSong.add(jButton);
         //   jpButCommandSong.add(jbPause);


            JPanel jpBottom = new JPanel();
            jpBottom.setLayout(new BoxLayout(jpBottom,BoxLayout.X_AXIS));

            jbBackToPlaylist = new JButton("Back to Playlist");
            jbBackToPlaylist.setPreferredSize(new Dimension(100,45));
            jbBackToPlaylist.setFont(new Font("Arial",Font.BOLD,16));
            jbBackToPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
            jbBackToPlaylist.addActionListener(this);
            jpBottom.add(jbBackToPlaylist);

            jbBackMenu = new JButton("Back to Menu");
            jbBackMenu.setPreferredSize(new Dimension(100,45));
            jbBackMenu.setFont(new Font("Arial",Font.BOLD,16));
            jbBackToPlaylist.setAlignmentX(Component.RIGHT_ALIGNMENT);
            jbBackMenu.addActionListener(this);
            jpBottom.add(jbBackMenu);

            //Setup  UI
            jFrame.add(jPanelTitle);
            jFrame.add(jpButCommandSong);
            jFrame.add(jpBottom);
            jFrame.setVisible(true); //Pour que ce soit visible
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Pour que le prog se termine en fermant la frame
            jFrame.setSize(400,400); //pour ajuster la taille
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
                        Playlist.getjFrame().dispose();
                        jFrame.dispose();
                        System.exit(0);
                    }
                    else {
                        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
            });
        }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()== jButton){
            System.out.println("=== Clic on button");

            Client.player.changeStatus();


//            //Send the command
//            client.sendToServer("Listen to");
//            System.out.println("Cmd listen to sent--------------");
//            //Send the Song name we want to listen to
//            client.sendToServer(jlsongNameChoose.getText());
//            System.out.println("name song to sent--------------");

            //Receive files infos  from server
            //Receive command from server
        }

        if (e.getSource() == jbPause){
            System.out.println("=== Clic on pause");

            //Client.player.pause();
            Client.player.changeStatus();

        }

        if (e.getSource() == jbBackMenu){
            jFrame.dispose();
            //Close Playlist frame too
            Playlist.getjFrame().dispose();
            Menu menu = new Menu(client);
        }

        if (e.getSource() == jbBackToPlaylist){
            jFrame.dispose();
        }
    }
}
