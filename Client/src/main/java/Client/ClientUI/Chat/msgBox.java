package Client.ClientUI.Chat;


import javax.swing.*;
import java.awt.*;

public class msgBox  extends JTextPane {

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public msgBox() {
        setBackground(new Color(0, 0, 0, 0));
    }

    private Color borderColor = Color.BLUE;
    private Color bgColor = Color.GREEN;

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
        super.paintComponent(grphcs);
    }
}
