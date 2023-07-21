#include <iostream>
#include <cstring>
#include <unistd.h>

#include <winsock2.h>
#include <ws2tcpip.h>

// #include <sys/socket.h>
// #include <netinet/in.h>
// #include <arpa/inet.h>

const int PORT = 8080;
const int BUFFER_SIZE = 1024;

int main() {
    int clientSocket;
    struct sockaddr_in serverAddr;
    char buffer[BUFFER_SIZE] = {0};

    // Create the client socket
    clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (clientSocket == -1) {
        std::cerr << "Error creating socket\n";
        return 1;
    }

    // Configure the server address
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Replace with the server IP address

    // Connect to the server
    if (connect(clientSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        std::cerr << "Connection failed\n";
        close(clientSocket);
        return 1;
    }

    // Send player information to the server (you can implement serialization here)

    // Receive initial game information from the server (game board, player info, etc.)

    // Implement game logic and communication with the server here
    // For example, read input from the player, send actions to the server, and receive game updates.

    // Close the client socket
    close(clientSocket);

    return 0;
}