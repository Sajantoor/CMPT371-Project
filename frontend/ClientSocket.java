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
        System.out.println("Message recieved");
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
