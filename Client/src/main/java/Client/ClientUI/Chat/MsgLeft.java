package Client.ClientUI.Chat;

import javax.swing.*;
import java.awt.*;

public class MsgLeft extends JLayeredPane {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel1;
    private msgBox txt;

    public MsgLeft(String from, String msg) {
        initComponents();
        txt.setText(msg);
    }

    private void initComponents() {

        txt = new msgBox();
        jLabel1 = new JLabel();

        txt.setEditable(false);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txt.setBgColor(new Color(221, 246, 255));
        txt.setBorderColor(new Color(6, 126, 183));

        ImageIcon imageIcon = new ImageIcon("Client/src/main/resources/User.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        jLabel1.setIcon(imageIcon);
        jLabel1.setPreferredSize(new Dimension(10,10));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 46,GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, 0))
        );
    }
}