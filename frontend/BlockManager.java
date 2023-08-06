import javax.swing.JPanel;

public class BlockManager {
    private static BlockManager instance = null;
    private Block[][] blocks;

    private BlockManager() {
        blocks = new Block[Constants.boardCols][Constants.boardRows]; // Initialize the 2D array
    }

    public static BlockManager getInstance() {
        if (instance == null) {
            instance = new BlockManager();
        }
        return instance;
    }

    public void addBlocksToPanel(JPanel boardPanel) {
        for (int i = 0; i < Constants.boardRows; i++) {
            for (int j = 0; j < Constants.boardCols; j++) {
                Block block = new Block(i, j);
                blocks[i][j] = block; // Store the reference in the array
                boardPanel.add(block);
            }
        }
    }

    public void setBlockAsCaptured(int x, int y, int playerID) {
        blocks[x][y].setCaptured(playerID);
    }

    public void setBlockAsDrawing(int x, int y, int xRelative, int yRelative, int playerID) {
        blocks[x][y].drawPixel(xRelative, yRelative, playerID);
    }

    public void clearBlock(int x, int y) {
        blocks[x][y].clearLines();
    }
}
