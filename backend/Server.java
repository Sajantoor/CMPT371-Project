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
    private static boolean gameStarted = false;
    private static List<ClientHandler> clientSockets = new ArrayList<>();
    public static int[] players = {0, 0, 0, 0};


    public static void main(String[] args) throws IOException {
        serverSocket = null;
        try {
            // Create the server socket
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            // Accept connections from clients and handle them
            startFaultTolerance();

            // In Server.java, inside the main method
            while (playerCount < MAX_PLAYERS) {
                if (getAvailablePlayerID() != -1) { // Check if there is an available slot
                    Socket newSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(newSocket);
                    Server.addClientSocket(clientHandler);

                    // Threads for the server to handle multiple clients simultaneously.
                    new Thread(clientHandler).start();
                } else {
                    // Optionally, add a short sleep here to prevent the loop from consuming too much CPU
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            if (playerCount > MAX_PLAYERS) {
                throw new RuntimeException("The number of target connections cannot exceed  " + MAX_PLAYERS);
            }
        } finally {
            // Close the server socket
            if (serverSocket != null) {
                try {
                    clear();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing sockets", e);
                }
            }
        }
    }

    public static void clear() throws IOException {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }

        for (ClientHandler clientHandler : clientSockets) {
            removeClientSocket(clientHandler);
        }
        clientSockets.clear();
        System.out.println("Server cleared. All existing connections terminated.");
    }

    private static void startFaultTolerance() {
        // Start the heartbeat timer
        Timer heartbeatTimer = new Timer();
        // check the status of each client every 5 seconds to see if they're still
        // running
        heartbeatTimer.schedule(new HeartbeatTask(), 0, 5000); // Every 5 seconds

        // Run a shutdown hook to stop the fault tolerance on program exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { // allows program to shut down gracefully after
                                                                // termination
            heartbeatTimer.cancel();
        }));
    }

    private static class HeartbeatTask extends TimerTask {
        @Override
        public void run() {
            // run whenever a client is not responsive in its 5 second ping
            // synchronized block to prevent concurrent modification of the list of client
            // sockets

            // Add list to avoid concurrent modification exception
            List<ClientHandler> clientHandlersToRemove = new ArrayList<>();

            for (ClientHandler clientHandler : clientSockets) {
                if (!clientHandler.getClientAlive()) {// added client status to client handler
                    System.out.println("Client " + clientHandler + " is not responding");
                    clientHandlersToRemove.add(clientHandler);
                }
            }

            for (ClientHandler clientHandler : clientHandlersToRemove) {
                removeClientSocket(clientHandler);
            }
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
    private static int getAvailablePlayerID() {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == 0) return i;
        }
        return -1; // No available slots
    }
    
}
