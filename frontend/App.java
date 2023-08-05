import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        try {
            ClientSocket.getInstance().connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {

        JPanel boardPanel = new JPanel(new GridLayout(4, 4, 5, 5)) {
            @Override
            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }
        };

        BlockManager.getInstance().addBlocksToPanel(boardPanel);

        // TODO: Create new cursor object for the player
        JFrame frame = Frame.getInstance().getFrame();
        CursorManager cm = CursorManager.getInstance();
        new Cursor();
        for (int i = 0; i < 3; i++) {
            cm.addCursor(new Cursor(i + 1));
        }

        frame.add(boardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
