#include <iostream>
#include <vector>
#include <map>
#include <cstring>
#include <unistd.h>
#include <winsock2.h>
#include <ws2tcpip.h>

// #include <sys/socket.h>
// #include <netinet/in.h>
// #include <arpa/inet.h>
const int PORT = 8080;
const int MAX_PLAYERS = 4;
const int BUFFER_SIZE = 1024;

int main() {
    int serverSocket, newSocket;
    struct sockaddr_in serverAddr;
    struct sockaddr_storage clientAddr;
    socklen_t addrSize;
    char buffer[BUFFER_SIZE];

    // Initialize the game board and players (same as previous code)

    // Create the server socket
    serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (serverSocket == -1) {
        std::cerr << "Error creating socket\n";
        return 1;
    }

    // Bind the socket to a specific address and port
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(PORT);
    serverAddr.sin_addr.s_addr = INADDR_ANY;

    if (bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1) {
        std::cerr << "Error binding socket\n";
        close(serverSocket);
        return 1;
    }

    // Listen for incoming connections
    if (listen(serverSocket, MAX_PLAYERS) == -1) {
        std::cerr << "Error listening\n";
        close(serverSocket);
        return 1;
    }

    std::cout << "Server listening on port " << PORT << std::endl;

    // Accept connections from clients and handle them
    int playerCount = 0;
    while (playerCount < MAX_PLAYERS) {
        addrSize = sizeof(clientAddr);
        newSocket = accept(serverSocket, (struct sockaddr*)&clientAddr, &addrSize);
        if (newSocket == -1) {
            std::cerr << "Error accepting connection\n";
            continue;
        }

        // Add the new player to the game (handle player information, store in players vector, etc.)

        // Send initial game information to the new player (game board, their player info, etc.)

        // Start a new thread or process to handle client communication and game actions.
        // This allows the server to handle multiple clients simultaneously.

        playerCount++;
    }

    // Close the server socket

    close(serverSocket);
    return 0;
}