package Client.ClientUI;

import Client.Client;
import Client.PlaylistOtherUsr;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Allusr {
    private JFrame jFrame;

    private JPanel jPanelTitle;
    private JLabel jlTitle ;
    private JLabel jlabel;
    private JLabel jlUserChoosed;

    private JPanel jPanelInfoUsr;
    private JLabel jlInfosusr;
    private JPanel jPanelListUser;
    private JLabel jlNoUsr;
    //private ArrayList<ClientHandler> clients = ClientHandler.getHandlerArrayList();
    private JList<String> jListUsrs ;
    private JScrollPane jScrollPane ;

    private JPanel jpBottom;
    private JButton jbBackMenu;

    private static DefaultListModel<String> model = new DefaultListModel<>();


    public Allusr(Client client) {
        jFrame = new JFrame();
//Title part
        jPanelTitle = new JPanel();
        jPanelTitle.setLayout(new BoxLayout(jPanelTitle,BoxLayout.Y_AXIS));
        jlTitle = new JLabel("VSfy File - Access other Users");
        jlTitle.setFont(new Font("Arial",Font.BOLD,25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jPanelTitle.add(jlTitle);

//Jlist of all users
        jPanelListUser = new JPanel();

        jListUsrs = new JList<String>();
        jListUsrs.setModel(model);

            jListUsrs.setVisibleRowCount(4);
            jListUsrs.setPreferredSize(new Dimension(400,80));
            jScrollPane = new JScrollPane(jListUsrs);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jPanelListUser.add(jScrollPane);

//Print out information of the selected usr
// IF HAVE ACCESS IF NOT ASK FOR ACCESS FIRST
        jPanelInfoUsr = new JPanel();
        jlabel = new JLabel("Select the user you want to connect to in the list above :  ");
        jlabel.setFont(new Font("Arial",Font.BOLD,20));
        jlabel.setBorder(new EmptyBorder(50,0,0,0));
        jlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelInfoUsr.add(jlabel);

        jlUserChoosed = new JLabel("No user selected");
        jlUserChoosed.setFont(new Font("Arial",Font.ITALIC,16));
        jlUserChoosed.setBorder(new EmptyBorder(50,0,0,0));
        jlUserChoosed.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanelInfoUsr.add(jlUserChoosed);

//Back to menu button
        jpBottom =new JPanel();
        jbBackMenu = new JButton("Back to Menu");
        jbBackMenu.setPreferredSize(new Dimension(150,55));
        jbBackMenu.setFont(new Font("Arial",Font.BOLD,18));
        jbBackMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                Menu menu = new Menu(client);
            }
        });
        jpBottom.add(jbBackMenu);

        //Jlist Selection Handler
        jListUsrs.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //Pop up to be sure
                int answer = JOptionPane.showConfirmDialog(jFrame, "Are you sure you want to View this user's playlist ? ", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) { //if YES
                    //Take the user name choosed
                    String SelectedUsr = jListUsrs.getSelectedValue();
                    jlUserChoosed.setText(SelectedUsr);
                    //Open it's frame

                    //Tell the server we want to Display the playlist of someone
                    client.sendToServer("Playlist Of");

                    //Sending the client username of the one we choosed
                    String separator =" : ";
                    int sepPos = SelectedUsr.lastIndexOf(separator);
                    String selectedUsrName = SelectedUsr.substring(0,sepPos);
                    System.out.println("The username of the client we want - "+selectedUsrName);
                    client.sendToServer(selectedUsrName);

                    //Display the new frame
                    jFrame.dispose();
                    //Then open the fram which will display user's song
                    PlaylistOtherUsr playlist = new PlaylistOtherUsr(client);
                }
            }
        });

        //Setup  UI
        jFrame.add(jPanelTitle);
        jFrame.add(jPanelListUser);
        jFrame.add(jPanelInfoUsr);
        jFrame.add(jpBottom);
        jFrame.setVisible(true); //Pour que ce soit visible
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
                   //Close connection with the server
                    client.sendToServer("Logout");

                    Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                    //Delete this client from the client handler list

                    //Close frame
                    jFrame.dispose();
                    System.exit(0);
                }
                else {
                    jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }

        });
    }

    public static void setModel(DefaultListModel<String> model) {
        Allusr.model = model;
    }
}
