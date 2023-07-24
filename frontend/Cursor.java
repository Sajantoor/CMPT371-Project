import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Cursor extends JPanel {
    private int x;
    private int y;

    Cursor() {
        setPreferredSize(new Dimension(30, 30));
        setOpaque(false); // Remove the background color
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                setLocation(x - 15, y - 15);
                repaint();
            }
        });
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
