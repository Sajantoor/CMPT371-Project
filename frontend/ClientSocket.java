import java.net.*;
import java.io.*;

public class ClientSocket {
    private static ClientSocket instance = null;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isClosed = false;
    private String playerID = null;
    private int tilePositionX = 0;
    private int tilePositionY = 0;

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
                handleDrawing(tokens);
                break;
            case (Constants.endDrawCommand):
                handleEndDraw(tokens);
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
                handleCapture(tokens);
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

    private void handleDrawing(String[] tokens) {
        tilePositionX = Integer.parseInt(tokens[1]);
        tilePositionY = Integer.parseInt(tokens[2]);
        int x = Integer.parseInt(tokens[3]);
        int y = Integer.parseInt(tokens[4]);
        int playerID = Integer.parseInt(tokens[5]);

        BlockManager.getInstance().setBlockAsDrawing(this.tilePositionX, tilePositionY, x, y, playerID);
        // hide the cursor when drawing
        CursorManager.getInstance().getCursor(playerID).hide();
    }

    private void handleEndDraw(String[] tokens) {
        tilePositionX = Integer.parseInt(tokens[1]);
        tilePositionY = Integer.parseInt(tokens[2]);
        int playerID = Integer.parseInt(tokens[3]);

        BlockManager.getInstance().clearBlock(tilePositionX, tilePositionY);
        CursorManager.getInstance().getCursor(playerID).show();

    }

    private void handleCapture(String[] tokens) {
        int userPlayerID = Integer.parseInt(tokens[3]);
        int tileX = Integer.parseInt(tokens[1]);
        int tileY = Integer.parseInt(tokens[2]);
        int playerID = Integer.parseInt(tokens[3]);

        BlockManager.getInstance().setBlockAsCaptured(tileX, tileY, userPlayerID);
        CursorManager.getInstance().getCursor(playerID).show();
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

        int intPlayerID = Integer.parseInt(id);

        if (intPlayerID == -1) {
            System.exit(1);
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
