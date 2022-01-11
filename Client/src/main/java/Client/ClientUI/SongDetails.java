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
        private Client client;
        private JButton jbPause;
        private JButton jbPlay;
        private JLabel jlsongNameChoose;

        public SongDetails(Client client, String songNameChoosed) {
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
            jpButCommandSong.setLayout(new BoxLayout(jpButCommandSong, BoxLayout.Y_AXIS));

            jpButCommandSong.setPreferredSize(new Dimension(500, 220));

            jbPlay = new JButton("Play");
            jbPlay.setPreferredSize(new Dimension(100,45));
            jbPlay.setFont(new Font("Arial",Font.BOLD,16));
            jbPlay.setAlignmentX(Component.LEFT_ALIGNMENT);
            jbPlay.addActionListener(this);

            jbPause = new JButton("Pause");
            jbPause.setPreferredSize(new Dimension(100,45));
            jbPause.setFont(new Font("Arial",Font.BOLD,16));
            jbPause.setAlignmentX(Component.LEFT_ALIGNMENT);
            jbPause.addActionListener(this);

            jpButCommandSong.add(jbPlay);
            jpButCommandSong.add(jbPause);


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
                        //Close connection with the server
                        Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                        //Delete this client from the client handler list
                        client.sendToServer("Logout");
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


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()== jbPlay){
            //Send the command
            client.sendToServer("Listen to");
            //Send the Song name we want to listen to
            client.sendToServer(jlsongNameChoose.getText());

            //Receive files infos  from server
            //Receive command from server
        }

        if (e.getSource() == jbPause){

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
