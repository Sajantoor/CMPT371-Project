import java.net.*;
import java.io.*;

public class ClientSocket {
    private static ClientSocket instance = null;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isClosed = false;
    private String playerID = null;

    private ClientSocket() {
    }

    public static ClientSocket getInstance() {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }

    public void connect() throws IOException {
        socket = new Socket(Constants.serverIP, Constants.serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.recieveMessages();
    }

    public void send(String message) {
        // Output message with the player id
        out.println(message + " " + playerID);
    }

    private void handleMessage(String message) {
        if (message == null) {
            return;
        }

        String[] tokens = message.split(" ");
        String commandToken = tokens[0];

        switch (commandToken) {
            case (Constants.cursorCommand):
                // Tokens are <x position> <y position> <player id>
                handleCursorCommand(tokens);
                break;
            case (Constants.startDrawCommand):
                // TODO: Call the appropriate draw method here for this user
                // Tokens are <tile x> <tile y> <player id>
                break;
            case (Constants.endDrawCommand):
                // TODO: Call the appropriate draw method here for this user
                // Tokens are <tile x> <tile y> <player id>
                break;
            case (Constants.endCommand):
                // Get each players score from the server
                String[] playerScores = new String[tokens.length - 1];
                for (int i = 0; i < playerScores.length; i++) {
                    playerScores[i] = tokens[i + 1];
                }

                Screens.getInstance().endGameScreen(playerScores);
                break;
            case (Constants.captureCommand):
                // TODO: Change the tile's color to a player's color
                // A player captures a tile
                // Tokens are <tile x> <tile y> <player id>
                int userPlayerID = Integer.parseInt(tokens[3]);
                int tileX = Integer.parseInt(tokens[1]);
                int tileY = Integer.parseInt(tokens[2]);

                BlockManager.getInstance().setBlockAsCaptured(tileX, tileY, userPlayerID);
                break;
            case (Constants.drawError):
                // TODO: handle the case where the player tries to draw on a tile that is
                // already being drawn on by another player (This is a likely case)
                break;
            case (Constants.captureError):
                // TODO: handle the case where the player tries to capture a tile that is
                // already captured by another player (This case really shouldn't happen)
                break;
            case (Constants.startCommand):
                Screens.getInstance().createAndShowGUI();
                break;
            case (Constants.playerIDCommand):
                setPlayerID(tokens[1]);
                break;
            default:
                System.out.println("Unrecognized command from frontend: " + commandToken);
                break;
        }
    }

    private void recieveMessages() {
        new Thread(() -> {
            while (!isClosed) {
                try {
                    String message = in.readLine();
                    handleMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close() throws IOException {
        isClosed = true; // This is to prevent the infinite loop in the thread from running
        in.close();
        out.close();
        socket.close();
    }

    private void setPlayerID(String id) {
        if (playerID != null) {
            throw new RuntimeException("Player ID already set");
        }

        playerID = id;
    }

    public void handleCursorCommand(String[] tokens) {
        double x = Double.parseDouble(tokens[1]);
        double y = Double.parseDouble(tokens[2]);
        int playerID = Integer.parseInt(tokens[3]);
        CursorManager cursorManager = CursorManager.getInstance();
        Cursor cursor = cursorManager.getCursor(playerID);

        if (cursor == null) {
            System.out.println("Creating new cursor for player: " + playerID);
            cursor = new Cursor(playerID);
            cursorManager.addCursor(cursor);
        }

        cursor.show();
        cursor.move(x, y);
    }

    public int getPlayerID() {
        return Integer.parseInt(playerID);
    }
}
