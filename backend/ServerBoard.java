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

    private void captureTile(int row, int col, int playerID) {
        // 4 is added to differentiate between drawing and captured tiles
        board[row][col] = playerID + 4;
    }

    private void drawTile(int row, int col, int playerID) {
        board[row][col] = playerID;
    }

    private boolean isTileFree(int row, int col) {
        return board[row][col] == 0;
    }

    private boolean isTileBeingDrawnBy(int row, int col, int playerID) {
        return board[row][col] == playerID || isTileFree(row, col);
    }

    public synchronized boolean releaseTile(int row, int col, int playerID) {
        // Only release the tile if it is being drawn by the player
        if (isTileBeingDrawnBy(row, col, playerID)) {
            board[row][col] = 0;
            return true;
        }

        return false;
    }

    public synchronized boolean attemptDrawTile(int row, int col, int playerID) {
        if (isTileBeingDrawnBy(row, col, playerID)) {
            drawTile(row, col, playerID);
            return true;
        }

        return false;
    }

    public synchronized boolean attemptCaptureTile(int row, int col, int playerID) {
        if (isTileBeingDrawnBy(row, col, playerID)) {
            captureTile(row, col, playerID);
            return true;
        }

        return false;
    }
}
