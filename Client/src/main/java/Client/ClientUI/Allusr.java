package Client.ClientUI;

import Client.Client;

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

//        if (clients!=null){
//            //Adding every connected usr on the model of the jlist
//            for (int i = 0; i < clients.size(); i++) {
//                model.addElement(clients.get(i).getClientUsername()+" : "+clients.get(i).getClientIPAddress());
//            }
            jListUsrs.setVisibleRowCount(4);
            jListUsrs.setPreferredSize(new Dimension(400,80));
            jScrollPane = new JScrollPane(jListUsrs);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jPanelListUser.add(jScrollPane);
//        }
//        else {
//            //If no user connected, put a label to inform and take off the jlist
//            jlNoUsr = new JLabel("Any user connected for the moment");
//            jListUsrs.setVisible(false);
//            jPanelListUser.add(jlNoUsr);
//        }

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
                String SelectedUsr =  jListUsrs.getSelectedValue();

                jlUserChoosed.setText(SelectedUsr);
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
