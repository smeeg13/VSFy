package Client.ClientUI.Chat;

import javax.swing.*;
import java.awt.*;

public class MsgRight extends JLayeredPane {

    private JLabel jlFrom;
    private msgBox txt;

    public MsgRight(String from,String text) {
        initComponents();
        jlFrom.setText(from);
        txt.setText(text);
    }

    //method called from within the constructor to initialize the form.
    private void initComponents() {

        txt = new msgBox();
        jlFrom = new JLabel();

        txt.setEditable(false);
        txt.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt.setBgColor(new java.awt.Color(222, 221, 255));
        txt.setBorderColor(new java.awt.Color(6, 23, 183));

        ImageIcon imageIcon = new ImageIcon("Client/src/main/resources/User.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back

        jlFrom.setIcon(imageIcon); // NOI18N
        jlFrom.setPreferredSize(new Dimension(5,5));

        setLayer(txt, JLayeredPane.DEFAULT_LAYER);
        setLayer(jlFrom, JLayeredPane.DEFAULT_LAYER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(txt, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlFrom, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jlFrom, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, 0))
        );
    }
}
