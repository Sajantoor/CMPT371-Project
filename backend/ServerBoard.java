class ServerBoard {
    private static ServerBoard instance;
    private int[][] board;
    private int captureCount;
    private int[] playerScores;

    ServerBoard() {
        board = new int[Constants.boardRows][Constants.boardCols];
        // set all tiles to -1 to indicate that they are free
        for (int i = 0; i < Constants.boardRows; i++) {
            for (int j = 0; j < Constants.boardCols; j++) {
                board[i][j] = -1;
            }
        }

        captureCount = 0;
        playerScores = new int[Server.getPlayerCount()];
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
        return board[row][col] == -1;
    }

    private boolean isTileBeingDrawnBy(int row, int col, int playerID) {
        return board[row][col] == playerID || isTileFree(row, col);
    }

    public synchronized boolean releaseTile(int row, int col, int playerID) {
        // Only release the tile if it is being drawn by the player
        if (isTileBeingDrawnBy(row, col, playerID)) {
            board[row][col] = -1;
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
            captureCount++;
            playerScores[playerID]++;
            return true;
        }

        return false;
    }

    public synchronized boolean allTilesCaptured() {
        if (captureCount == Constants.boardCols * Constants.boardRows) {
            return true;
        }
        return false;
    }

    public int[] getPlayerScores() {
        return playerScores;
    }
}
