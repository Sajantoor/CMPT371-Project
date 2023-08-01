import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TCP Server used to accept connections when hosting a game. .
 */

public class Server {
    private static final int PORT = 3000;
    private static final int MAX_PLAYERS = 4;
    private static ServerSocket serverSocket = null;
    private static int playerCount = 0;
    private static List<ClientHandler> connectedClients = new ArrayList<>();
    private static List<Socket> clientSockets = new ArrayList<>();
    private static boolean isRunning;
    public static void main(String[] args) throws Exception  {
        startServer();
        startFaultTolerance();
    }

    public static void startServer() throws Exception  {
        serverSocket = null;
        try {
            // Initialize the game board and players (same as previous code)

            // Create the server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            // Accept connections from clients and handle them
            while (playerCount < MAX_PLAYERS) {
                Socket newSocket = serverSocket.accept();
                Server.addClientSocket(newSocket);

                // threads for the server to handle multiple clients simultaneously.
                new Thread(new ClientHandler(newSocket)).start();
            }
            isRunning = true;
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

    public static void stopServer() throws IOException {
        isRunning = false;
        clear();
    }

    public static void clear() throws IOException {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
        // terminate connections
    }

    private static void startFaultTolerance() {
        // Start the heartbeat timer
        Timer clientTimer = new Timer();
        clientTimer.schedule(new TimeClients(), 0, 5000); // Every 5 seconds
    }

    private static class TimeClients extends TimerTask {
        @Override
        public void run() {
            for (ClientHandler clientHandler : connectedClients) {
                // wasn't sure how we're gonna go about this
            }
        }
    }

    public synchronized static void addClientSocket(Socket socket) {
        clientSockets.add(socket);
        playerCount++;
    }

    public synchronized static void removeClientSocket(Socket socket) {
        clientSockets.remove(socket);
        playerCount--;
    }

    public static List<Socket> getClientSockets() {
        return clientSockets;
    }

    public static boolean isPlayersLeft() {
        return playerCount != 0;
    }

    public synchronized static int getPlayerCount() {
        return playerCount;
    }
}