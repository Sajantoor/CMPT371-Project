import javax.swing.*;
import java.awt.*;

public class Screens {
    private static Screens instance = null;
    private JFrame frame;

    private Screens() {
    }

    public static Screens getInstance() {
        if (instance == null) {
            instance = new Screens();
        }
        return instance;
    }

    public void start() {
        frame = new JFrame("Deny and Conquer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));

        StartScreenPanel startScreenPanel = new StartScreenPanel();

        frame.add(startScreenPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void createAndShowGUI() {
        frame.getContentPane().removeAll();

        JPanel boardPanel = new JPanel(new GridLayout(4, 4, 5, 5)) {
            @Override
            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }
        };

        BlockManager.getInstance().addBlocksToPanel(boardPanel);

        Cursor cursor = new Cursor(frame);

        frame.add(cursor);
        frame.add(boardPanel);
        frame.revalidate(); // Update the frame layout
        frame.repaint(); // Repaint the frame to reflect the changes
        frame.pack();
        frame.setVisible(true);
    }

    public void endGameScreen(String[] playerScores) {
        frame.getContentPane().removeAll();

        GameOverPanel gameOverPanel = new GameOverPanel(playerScores);

        frame.add(gameOverPanel);
        frame.revalidate(); // Update the frame layout
        frame.repaint(); // Repaint the frame to reflect the changes
        frame.pack();
        frame.setVisible(true);
    }

}
