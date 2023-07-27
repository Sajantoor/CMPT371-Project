import java.io.*;
import java.net.*;

/**
 * TCP Server used to accept connections when hosting a game. .
 */

public class Server {
    private static final int PORT = 8080;
    private static final int MAX_PLAYERS = 4;
    private static final int BUFFER_SIZE = 1024;
    private static ServerSocket serverSocket = null;
    private static int playerCount = 0;
    public static void main(String[] args) {
        serverSocket = null;
        try {
            // Initialize the game board and players (same as previous code)

            // Create the server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            // Accept connections from clients and handle them
            playerCount = 0;
            while (playerCount < MAX_PLAYERS) {
                Socket newSocket = serverSocket.accept();

                // threads for the server to handle multiple clients simultaneously.
                new Thread(new ClientHandler(newSocket)).start();

                playerCount++;
            }
        } catch (IOException e) {
            if (playerCount > MAX_PLAYERS) {
                throw new Exception("The number of target connections cannot exceed  " + MAX_PLAYERS);
        } finally {
            // Close the server socket
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                }
            }
        }
    }
    
    public void clear() throws IOException {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
        //terminate connections
    }
}