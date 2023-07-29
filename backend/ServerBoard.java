class ServerBoard {
    private static ServerBoard instance;
    private int[][] board;

    ServerBoard() {
        board = new int[Constants.boardRows][Constants.boardCols];
    }

    public static ServerBoard getInstance() {
        if (instance == null) {
            instance = new ServerBoard();
        }

        return instance;
    }

    public void captureTile(int row, int col, int playerID) {
        // 4 is added to differentiate between drawing and captured tiles
        board[row][col] = playerID + 4;
    }

    public void drawTile(int row, int col, int playerID) {
        board[row][col] = playerID;
    }

    public boolean isTileFree(int row, int col) {
        return board[row][col] == 0;
    }

    public boolean isTileBeingDrawnBy(int row, int col, int playerID) {
        return board[row][col] == playerID || isTileFree(row, col);
    }

    public int[][] getBoard() {
        return board;
    }

    public int getTile(int row, int col) {
        return board[row][col];
    }

    public void releaseTile(int row, int col, int playerID) {
        // Only release the tile if it is being drawn by the player
        if (isTileBeingDrawnBy(row, col, playerID)) {
            board[row][col] = 0;
        }
    }
}
