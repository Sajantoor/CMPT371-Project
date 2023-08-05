import javax.swing.JPanel;

public class BlockManager {
    private static BlockManager instance = null;
    private int BOARD_SIZE;
    private Block[][] blocks;
    private int captureCount;

    private BlockManager() {
        BOARD_SIZE = 1;
        blocks = new Block[BOARD_SIZE][BOARD_SIZE]; // Initialize the 2D array
        captureCount = 0;
    }

    public static BlockManager getInstance() {
        if (instance == null) {
            instance = new BlockManager();
        }
        return instance;
    }

    public void addBlocksToPanel(JPanel boardPanel) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Block block = new Block(i, j);
                blocks[i][j] = block; // Store the reference in the array
                boardPanel.add(block);
            }
        }
    }

    public void setBlockAsCaptured(int x, int y, int playerID) {
        blocks[x][y].setCaptured(playerID);
        captureCount++;

        if (captureCount == BOARD_SIZE * BOARD_SIZE) {
            Screens.getInstance().endGameScreen();
        }
    }

}
