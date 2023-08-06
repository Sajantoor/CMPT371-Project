import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP Server used to accept connections when hosting a game. .
 */

public class Server {
    private static final int PORT = 3000;
    private static final int MAX_PLAYERS = 4;
    private static ServerSocket serverSocket = null;
    private static int playerCount = 0;
    private static boolean gameStarted = false;
    private static List<ClientHandler> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        serverSocket = null;
        try {
            // Create the server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            // Accept connections from clients and handle them
            while (playerCount < MAX_PLAYERS && !gameStarted) {
                Socket newSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(newSocket);
                Server.addClientSocket(clientHandler);

                // threads for the server to handle multiple clients simultaneously.
                new Thread(clientHandler).start();
            }

        } catch (IOException e) {
            if (playerCount > MAX_PLAYERS) {
                throw new RuntimeException("The number of target connections cannot exceed  " + MAX_PLAYERS);
            }
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
    }

    public synchronized static void addClientSocket(ClientHandler socket) {
        clientSockets.add(socket);
        playerCount++;
    }

    public synchronized static void removeClientSocket(ClientHandler socket) {
        clientSockets.remove(socket);
        playerCount--;
    }

    public static List<ClientHandler> getClientSockets() {
        return clientSockets;
    }

    public static boolean isPlayersLeft() {
        return playerCount != 0;
    }

    public synchronized static int getPlayerCount() {
        return playerCount;
    }

    public static void stopAcceptingClients() {
        gameStarted = true;
    }
}
