package Client.ClientUI.Chat;

import Client.Client;
import Client.ClientUI.Menu;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Frame  extends JFrame implements WindowListener {
    private JButton cmdBackMenu;
    private JButton cmdSendMsg;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private static JPanel panel;
    private JTextArea txt;

    private Client client;


    public  Frame(Client client){
        initComponents();
        panel.setLayout(new MigLayout("fillx"));
        this.client = client;
        setTitle(client.getClientUsername()+"'s Chat");
    }

    private void initComponents() {

        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        panel = new JPanel();
        jScrollPane2 = new JScrollPane();
        txt = new JTextArea();
        cmdBackMenu = new JButton();
        cmdSendMsg = new JButton();


        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.setBackground(new java.awt.Color(255, 255, 255));

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 1051, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 459, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane2.setViewportView(txt);

        cmdBackMenu.setText("Back Menu");
        cmdBackMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmdBackMenuActionPerformed(evt);
            }
        });

        cmdSendMsg.setText("Send");
        cmdSendMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmdSendMsgActionPerformed(evt);
            }
        });

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(cmdBackMenu, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdSendMsg, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))
                                .addGap(81, 81, 81))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(cmdBackMenu)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdSendMsg)))
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1,GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }
    private void cmdBackMenuActionPerformed(ActionEvent evt) {
        client.sendToServer("Left Chat");
        this.dispose();
        Menu menu = new Menu(client);
    }

    private void cmdSendMsgActionPerformed(ActionEvent evt) {
        String text = txt.getText().trim();
        String msgFrom =client.getClientUsername();
        //Send to server our msg so he can broadcast it to others
        client.sendMessage(client.getClientUsername()+":"+text);
        txt.setText("");

        //Put it on the frame
        MsgRight myMsg = new MsgRight(msgFrom,text);
        panel.add(myMsg, "wrap, w 80%, al right");
        panel.repaint();
        panel.revalidate();

    }

    public static void receiveMsgFromOther(String msg){

        String substr = ":";
        String before = msg.substring(0, msg.indexOf(substr));
        String after = msg.substring(msg.indexOf(substr) + substr.length());


        MsgLeft item = new MsgLeft(before,after);
        panel.add(item, "wrap, w 80%");
        panel.repaint();
        panel.revalidate();
        System.out.println("+++new panel of msg add");    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
//Pop up to be sure
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to disconnect ? ", "Loggin Out", JOptionPane.YES_NO_CANCEL_OPTION);
        if (answer == 0) { //if YES

            //Close connection with the server
            if (client != null) {
                client.sendToServer("Logout");

                Client.ExitApplication(client.getSocket(), client.getBufReader(), client.getBufWriter());
                //Close frame
                this.dispose();
            }
            System.exit(0);
        } else {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
}
