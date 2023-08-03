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
        JFrame frame = new JFrame("Deny and Conquer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));
    
        JPanel boardPanel = new JPanel(new GridLayout(4, 4, 5, 5)) {
            @Override
            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }
        };
        for (int i = 0; i < 16; i++) {
            Block block = new Block(i / 4, i % 4);
            boardPanel.add(block);
        }
        Cursor cursor = new Cursor(frame);
        frame.add(cursor);

        frame.setContentPane(boardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
