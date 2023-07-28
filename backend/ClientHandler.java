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

    @Override
    public void run() {
        try {
            // Handle client communication and game actions here.

            // You can use InputStream/OutputStream to handle communication with the client.

            OutputStream os = clientSocket.getOutputStream();
            InputStream is = clientSocket.getInputStream();
            out = new PrintWriter(os, true);
            in = new BufferedReader(new InputStreamReader(is));

            // Send playerID to the client when connecting
            String sendPlayerID = "playerID " + Server.getPlayerCount();
            out.println(sendPlayerID);

            String message;

            while (Server.isPlayersLeft()) {
                message = in.readLine();
                handleMessage(message);
            }

            // Remove the client socket from the list of active client sockets upon
            // disconnection
            Server.removeClientSocket(clientSocket);
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    public void handleMessage(String message) {
        if (message == null) {
            return;
        }

        String[] tokens = message.split(" ");

        if (tokens.length == 0) {
            return;
        }

        String commandToken = tokens[0];

        System.out.println(commandToken);
        broadcastMessage(message);

        switch (commandToken) {
            case ("cursor"):

                break;
            case ("draw"):

                break;
            case ("end"):

                break;
            case ("capture"):

            case ("playerID"):

            default:
                System.out.println("Unrecognized command: " + commandToken);
                break;
        }

    }

    private void broadcastMessage(String message) {
        for (Socket socket : Server.getClientSockets()) {
            if (socket != clientSocket && socket.isConnected()) {
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.println(message);
                } catch (IOException e) {
                    System.err.println("Error broadcasting message to client: " + e.getMessage());
                }
            }
        }
    }
}