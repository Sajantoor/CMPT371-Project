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

        JFrame frame = Frame.getInstance().getFrame();
        CursorManager.getInstance().createCursors();

        frame.add(boardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
