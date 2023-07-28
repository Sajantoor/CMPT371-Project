import java.net.*;
import java.io.*;

public class ClientSocket {
    private static ClientSocket instance = null;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isClosed = false;
    private String playerID = null;

    private final int PORT = 3000;
    private final String HOST = "localhost";

    private ClientSocket() {
    }

    public static ClientSocket getInstance() {
        if (instance == null) {
            instance = new ClientSocket();
        }
        return instance;
    }

    public void connect() throws IOException {
        socket = new Socket(HOST, PORT);
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

        System.out.println("Message from server: " + message);

        String[] tokens = message.split(" ");
        String commandToken = tokens[0];

        switch (commandToken) {
            case (Constants.cursorCommand):
                // TODO: Call the appropriate cursor's move method here
                // Tokens are <x position> <y position> <player id>
                break;
            case (Constants.drawCommand):
                // TODO: Call the appropriate draw method here for this user
                // Not sure if this is the best way to do this since we already get cursor
                // updates from the server for all users. Maybe we can have a startDraw and
                // endDraw command instead and track if the user is drawing or not
                // Not sure what's the best way, please explore this...

                // Tokens are <tile x> <tile y> <x position> <y position> <player id>
                break;
            case (Constants.endCommand):
                // TOOD: Call the appropriate game over method here
                // Game over
                break;
            case (Constants.captureCommand):
                // TODO: Change the tile's color to a player's color
                // A player captures a tile
                // Tokens are <tile x> <tile y> <player id>
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
}
