import java.net.*;
import java.io.*;

public class ClientSocket {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isClosed = false;

    private final int PORT = 3000;
    private final String HOST = "localhost";

    ClientSocket() {
    }

    public void connect() throws IOException {
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.recieveMessages();
    }

    public void send(String message) {
        out.println(message);
    }

    private void handleMessage(String message) {
        String[] tokens = message.split(" ");
        String comamndToken = tokens[0];

        switch (comamndToken) {
            case ("cursor"):
                // TODO: Call the appropriate cursor's move method here
                // Tokens are <x position> <y position> <player id>
                break;
            case ("draw"):
                // TODO: Call the appropriate draw method here for this user
                // Not sure if this is the best way to do this since we already get cursor
                // updates from the server for all users. Maybe we can have a startDraw and
                // endDraw command instead and track if the user is drawing or not
                // Not sure what's the best way, please explore this...

                // Tokens are <tile x> <tile y> <x position> <y position> <player id>
                break;
            case ("end"):
                // TOOD: Call the appropriate game over method here
                // Game over
                break;
            case ("capture"):
                // TODO: Change the tile's color to a player's color
                // A player captures a tile
                // Tokens are <tile x> <tile y> <player id>
            default:
                System.out.println("Unrecognized command: " + comamndToken);
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
}
