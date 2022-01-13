package Client.ClientUI;

import Client.Client;
import Client.Player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class SongDetails implements ActionListener {

    private final JFrame jFrame;
    private JButton jbBackMenu;
    private JButton jbBackToPlaylist;
    private final Client client;
    private JButton jbPause;
    private JButton jButton;
    private JLabel jlsongNameChoose;
    private boolean playing = true;
    private JSlider jslidVolume;
    public static boolean stopListening;

    private final ImageIcon iconPlay = new ImageIcon(new ImageIcon("Client/src/main/resources/But_Play.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
    private final ImageIcon iconPause = new ImageIcon(new ImageIcon("Client/src/main/resources/But_Pause.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
    private final ImageIcon iconHighVol = new ImageIcon(new ImageIcon("Client/src/main/resources/high-volume.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
    private final ImageIcon iconLowVol = new ImageIcon(new ImageIcon("Client/src/main/resources/low-volume.png").getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));

    private  InputStream inputStream;
    private Player player ;

    public SongDetails( Client client, String songNameChoosed, InputStream is) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        //Creating and implementing components of the frame
        inputStream = is;
        player = new Player(songNameChoosed,is);

        jFrame = new JFrame();
        this.client = client;

        JPanel jPanelTitle = new JPanel();
        jPanelTitle.setLayout(new BoxLayout(jPanelTitle, BoxLayout.Y_AXIS));
        jPanelTitle.setPreferredSize(new Dimension(400, 150));

        JLabel jlFilename = new JLabel("Song choosed : ");
        jlFilename.setFont(new Font("Arial", Font.BOLD, 16));
        jlFilename.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlFilename.setAlignmentX(Component.CENTER_ALIGNMENT);

        jlsongNameChoose = new JLabel(songNameChoosed);
        jlsongNameChoose.setFont(new Font("Arial", Font.ITALIC, 14));
        jlsongNameChoose.setBorder(new EmptyBorder(50, 0, 0, 0));
        jlsongNameChoose.setAlignmentX(Component.CENTER_ALIGNMENT);

        jPanelTitle.add(jlFilename);
        jPanelTitle.add(jlsongNameChoose);

        JPanel jpButCommandSong = new JPanel();
        jpButCommandSong.setLayout(new BoxLayout(jpButCommandSong, BoxLayout.Y_AXIS));
        jpButCommandSong.setPreferredSize(new Dimension(400, 120));

        jButton = new JButton();
        jButton.setIcon(iconPause);
        jButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jButton.setBackground(new Color(211, 211, 211));
        jButton.setPreferredSize(new Dimension(5, 5));
        jButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButton.addActionListener(this);
        jpButCommandSong.add(jButton);

        JPanel jPanelVol = new JPanel();
        jPanelVol.setLayout(new GridLayout(1, 3));
        jPanelVol.setPreferredSize(new Dimension(400, 80));

        JLabel jllow = new JLabel();
        jllow.setIcon(iconLowVol);
        jllow.setPreferredSize(new Dimension(5, 5));
        jllow.setBackground(new Color(211, 211, 211));
        jllow.setAlignmentX(Component.LEFT_ALIGNMENT);

        jslidVolume = new JSlider(JSlider.HORIZONTAL, 0, 100, 40);
        jslidVolume.setPreferredSize(new Dimension(100, 80));
        jslidVolume.setMajorTickSpacing(20);
        jslidVolume.setPaintLabels(true);
        jslidVolume.setFont(new Font("MV Boli", Font.PLAIN, 10));
        jslidVolume.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            player.setVolume((float) (source.getValue() / 100.0));
        });
        jslidVolume.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel jlhigh = new JLabel();
        jlhigh.setIcon(iconHighVol);
        jlhigh.setPreferredSize(new Dimension(5, 5));
        jlhigh.setBackground(new Color(211, 211, 211));
        jlhigh.setAlignmentX(Component.RIGHT_ALIGNMENT);

        jPanelVol.add(jllow);
        jPanelVol.add(jslidVolume);
        jPanelVol.add(jlhigh);

        JPanel jpBottom = new JPanel();
        jpBottom.setLayout(new BoxLayout(jpBottom, BoxLayout.X_AXIS));

        jbBackToPlaylist = new JButton("Back to Playlist");
        jbBackToPlaylist.setPreferredSize(new Dimension(100, 45));
        jbBackToPlaylist.setFont(new Font("Arial", Font.BOLD, 16));
        jbBackToPlaylist.setAlignmentX(Component.LEFT_ALIGNMENT);
        jbBackToPlaylist.addActionListener(this);
        jpBottom.add(jbBackToPlaylist);

        jbBackMenu = new JButton("Back to Menu");
        jbBackMenu.setPreferredSize(new Dimension(100, 45));
        jbBackMenu.setFont(new Font("Arial", Font.BOLD, 16));
        jbBackToPlaylist.setAlignmentX(Component.RIGHT_ALIGNMENT);
        jbBackMenu.addActionListener(this);
        jpBottom.add(jbBackMenu);

        //Setup  UI
        jFrame.setTitle("VSFY Song player");
        jFrame.add(jPanelTitle);
        jFrame.add(jpButCommandSong);
        jFrame.add(jPanelVol);
        jFrame.add(jpBottom);
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Pour que le prog se termine en fermant la frame
        jFrame.setSize(400, 400); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        //Layout Par defaut = CardLayout
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
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
                } else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        //Launching the song

        player.play();
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jButton) {
            player.changeStatus();

            System.out.println("=== Clic on button play / pause");
            if (playing) {
                jButton.setIcon(iconPlay);
                playing = false;
            } else {
                jButton.setIcon(iconPause);
                playing = true;
            }
        }

        if (e.getSource() == jbBackMenu) {
            player.pause();
            stopListening = true;
            jFrame.dispose();
            //Close Playlist frame too
            Playlist.getjFrame().dispose();
            Menu menu = new Menu(client);
        }

        if (e.getSource() == jbBackToPlaylist) {
            player.pause();
            stopListening = true;
            jFrame.dispose();
            Playlist.getjFrame().setVisible(true);
        }
    }
}
