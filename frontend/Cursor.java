import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Cursor extends JPanel {
    private int x;
    private int y;
    private int playerId;

    Cursor(int playerId) {
        this.playerId = playerId;
        setPreferredSize(new Dimension(30, 30));
        setOpaque(false); // Remove the background color
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                setLocation(x - 15, y - 15);
                repaint();

                // Send the cursor's position to the server
                String message = String.format("%s %d %d %d", Constants.cursorCommand, x, y, playerId);
                ClientSocket.getInstance().send(message);
            }
        });
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
