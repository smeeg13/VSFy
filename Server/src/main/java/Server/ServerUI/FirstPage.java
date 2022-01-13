package Server.ServerUI;

import Server.MyLogger;
import Server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


public class FirstPage {

    private final JFrame jFrame;
    private final MyLogger logr = Server.getLogr();

    /**
     * Constructor
     * @throws IOException
     */

    public FirstPage() throws IOException {

        logr.getLogger().info("=========== program starts=============");

        //UI Components
        jFrame = new JFrame();
        JPanel jPanelBut = new JPanel();
        jPanelBut.setPreferredSize(new Dimension(500, 50));
        jPanelBut.setBorder(new EmptyBorder(75, 0, 10, 0));

        JPanel jPanelLabels = new JPanel();
        jPanelLabels.setLayout(new BoxLayout(jPanelLabels, BoxLayout.Y_AXIS));
        jPanelLabels.setPreferredSize(new Dimension(600, 350));
        BufferedImage myPicture = ImageIO.read(new File("Server/src/main/resources/Logo.jpg"));
        JLabel jlPic = new JLabel(new ImageIcon(myPicture));
        jlPic.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelLabels.add(jlPic);

        JLabel jlStartServ = new JLabel("Click below to start your Server ! :)");
        jlStartServ.setFont(new Font("Arial", Font.BOLD, 22));
        jlStartServ.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlStartServ.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelLabels.add(jlStartServ);

        JPanel jpInfos = new JPanel();
        jpInfos.setLayout(new BoxLayout(jpInfos, BoxLayout.PAGE_AXIS));
        jpInfos.setPreferredSize(new Dimension(200, 100));
        jpInfos.setBorder(new EmptyBorder(75, 0, 10, 0));

        JLabel jlip = new JLabel("IP of the server : " + Server.getIp());
        jlip.setFont(new Font("Arial", Font.ITALIC, 12));
        jlip.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlip.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelLabels.add(jlip);

        JLabel jlport = new JLabel("Listening on port : " + Server.getPort());
        jlport.setFont(new Font("Arial", Font.ITALIC, 12));
        jlport.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlport.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelLabels.add(jlport);

        JButton jbSartServer = new JButton("Start");
        jbSartServer.setPreferredSize(new Dimension(150, 40));
        jbSartServer.setFont(new Font("Arial", Font.BOLD, 17));
        jPanelBut.add(jbSartServer);

        //Listener for the Start Button
        jbSartServer.addActionListener(e -> {
            //Go to MainPage frame
            jFrame.dispose();
            try {
                MainPage mainPage = new MainPage(); //This constructor will start the server
            } catch (IOException ex) {
                ex.printStackTrace();
                logr.getLogger().log(Level.SEVERE, "exception thrown", ex);
            }
        });

        //Setup  UI
        jFrame.add(jPanelLabels);
        jFrame.add(jPanelBut);
        jFrame.setTitle("Server - Starting Page");
        jFrame.setVisible(true); //Pour que ce soit visible
        jFrame.setSize(600, 600); //pour ajuster la taille
        jFrame.setLocationRelativeTo(null); //positionner Au milieu de l'ecran
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        ImageIcon img = new ImageIcon("Server/src/main/resources/Logo.jpg");
        jFrame.setIconImage(img.getImage());
        //Window Listener
        jFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                //Pop up to be sure
                int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
                if (answer == JOptionPane.YES_OPTION) { //if YES
                    //Close frame
                    jFrame.dispose();
                    // end program
                    logr.getLogger().setLevel(Level.INFO);
                    logr.getLogger().info("=========== program ends =============");
                    System.exit(0);
                } else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
}
