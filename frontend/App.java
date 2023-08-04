import javax.swing.*;
import java.awt.*;

public class App {

    private static final int BOARD_SIZE = 4;
    private static Block[][] blocks;

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

        blocks = new Block[BOARD_SIZE][BOARD_SIZE]; // Initialize the 2D array

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Block block = new Block(i, j);
                blocks[i][j] = block; // Store the reference in the array
                boardPanel.add(block);
            }
        }

        Cursor cursor = new Cursor(frame);
        frame.add(cursor);

        frame.add(boardPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void setBlockAsCaptured(int x, int y, int playerID) {
        blocks[x][y].setCaptured(playerID);
    }

}
