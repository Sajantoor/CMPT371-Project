import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private Socket getSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            // You can use InputStream/OutputStream to handle communication with the client.
            OutputStream os = clientSocket.getOutputStream();
            InputStream is = clientSocket.getInputStream();
            out = new PrintWriter(os, true);
            in = new BufferedReader(new InputStreamReader(is));

            // Send playerID to the client when connecting
            String sendPlayerID = "playerID " + (Server.getPlayerCount() - 1);
            sendMessage(sendPlayerID);

            String message;

            while (Server.isPlayersLeft()) {
                message = in.readLine();
                handleMessage(message);
            }

            // Remove the client socket from the list of active client sockets upon
            // disconnection
            Server.removeClientSocket(this);
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private void handleMessage(String message) {
        if (message == null) {
            return;
        }

        String[] tokens = message.split(" ");

        if (tokens.length == 0) {
            return;
        }

        System.out.println("Received message: " + message);

        String commandToken = tokens[0];

        switch (commandToken) {
            case (Constants.captureCommand):
                handleCapture(tokens);
                break;
            case (Constants.startDrawCommand):
                handleStartDraw(tokens);
                break;
            case (Constants.endDrawCommand):
                handleEndDraw(tokens);
                break;
            case (Constants.cursorCommand):
                broadcastMessage(message);
                break;
            case (Constants.startCommand):
                startGame();
                break;
            default:
                System.out.println("Unrecognized command: " + commandToken);
                break;
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler socket : Server.getClientSockets()) {
            if (socket != this && socket.getSocket().isConnected()) {
                socket.sendMessage(message);
            }
        }
    }

    private void broadcastMessageToAll(String message) {
        for (ClientHandler socket : Server.getClientSockets()) {
            if (socket != this && socket.getSocket().isConnected()) {
                socket.sendMessage(message);
            }
            out.println(message);
        }
    }

    /**
     * Handles the start draw event, checks if the tile is already being drawn by
     * another user or captured by another user
     * 
     * If the tile is already being drawn or captured by another user, then don't
     * draw and don't broadcast the message and send an error message to the client
     * 
     * Else mark the tile as being drawn by the player and broadcast the message to
     * all other clients
     * 
     * @param tokens The tokens in the form: <command> <tile x> <tile y> <player id>
     */
    private void handleStartDraw(String[] tokens) {
        // Tokens are <tile x> <tile y> <player id>
        int playerID = Integer.parseInt(tokens[6]);
        int tileX = Integer.parseInt(tokens[1]);
        int tileY = Integer.parseInt(tokens[2]);

        // Check if the tile is already being drawn on or captured by another player
        // the tile is being drawn on or captured by another player, so don't draw and
        // don't broadcast the message
        if (ServerBoard.getInstance().attemptDrawTile(tileX, tileY, playerID)) {
            broadcastMessage(String.join(" ", tokens));
        } else {
            System.out.println("Tile is being drawn by another player");
            sendMessage(Constants.drawError);
        }
    }

    /**
     * Handles the capture event, checks if the tile is already being drawn by
     * another user or captured by another user
     * 
     * If the tile is already being drawn or captured by another user, then don't
     * capture and don't broadcast the message and send an error message to the
     * client
     * 
     * Else mark the tile as being captured by the player and broadcast the message
     * to all other clients
     * 
     * @param tokens The tokens in the form: <command> <tile x> <tile y> <player id>
     */
    private void handleCapture(String[] tokens) {
        // Tokens are <tile x> <tile y> <player id>
        int playerID = Integer.parseInt(tokens[3]);
        int tileX = Integer.parseInt(tokens[1]);
        int tileY = Integer.parseInt(tokens[2]);

        // Check if the tile has been captured or is being drawn on by another player
        if (ServerBoard.getInstance().attemptCaptureTile(tileX, tileY, playerID)) {
            // Take the tile and mark it as captured by the player
            broadcastMessage(String.join(" ", tokens));

            if (ServerBoard.getInstance().allTilesCaptured()) {
                endGame();
            }
            return;
        } else {
            // the tile is being captured or captured by another player, so don't capture
            // and don't broadcast the message
            sendMessage(Constants.captureError);
        }
    }

    /**
     * Handles the end draw event, unmarks the tile as being drawn by the player and
     * broadcasts the message to all other clients
     * 
     * @param tokens The tokens in the form: <command> <tile x> <tile y> <player id>
     */
    private void handleEndDraw(String[] tokens) {
        // Tokens are <tile x> <tile y> <player id>
        int playerID = Integer.parseInt(tokens[3]);
        int tileX = Integer.parseInt(tokens[1]);
        int tileY = Integer.parseInt(tokens[2]);

        ServerBoard serverBoard = ServerBoard.getInstance();

        // Unmark the tile as being drawn by the player
        serverBoard.releaseTile(tileX, tileY, playerID);
        broadcastMessage(String.join(" ", tokens));
    }

    /**
     * Tells server to not accept any more clients and lets
     * clients know to start the game
     */
    private void startGame() {
        Server.stopAcceptingClients();

        // message containing the start command and the current player count
        String message = String.format("%s %d", Constants.startCommand, Server.getPlayerCount());

        // Send that the game is starting all players
        broadcastMessageToAll(message);
    }

    public void endGame() {
        int[] playerScores = ServerBoard.getInstance().getPlayerScores();

        String message = Constants.endCommand + " ";

        // Send each players score back
        for (int i = 0; i < playerScores.length; i++) {
            message += playerScores[i] + " ";
        }

        broadcastMessageToAll(message);
    }

    /**
     * Sends a message to the client
     * 
     * @param message
     */
    private void sendMessage(String message) {
        out.println(message);
    }
}
