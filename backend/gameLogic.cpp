#include <iostream>
#include <vector>
#include <map>
#include <cstring>
#include <unistd.h>




// Constants for the game board size
const int BOARD_SIZE = 8;
const int BOX_AREA = 64; // 8x8 boxes

// Player structure to store player information
struct Player {
    std::string name;
    std::string penColor;
    int numBoxesTaken;
};

// Function to check if a box is taken over by a player
bool isBoxTakenOver(const std::vector<std::vector<std::string>>& board, int row, int col) {
    return (board[row][col] != " ");
}

// Function to update the board after a player takes over a box
void updateBoard(std::vector<std::vector<std::string>>& board, int row, int col, const std::string& penColor) {
    board[row][col] = penColor;
}

// Function to calculate the percentage of a box that is colored by a player
float calculateBoxPercentage(const std::vector<std::vector<std::string>>& board, int row, int col, const std::string& penColor) {
    // Implement logic to calculate the percentage of the box area colored by the player
    // use this function to check if a player has taken over a box (>= 50% colored).
}

int main() {

   
  


    // Initialize the game board with empty boxes
    std::vector<std::vector<std::string>> board(BOARD_SIZE, std::vector<std::string>(BOARD_SIZE, " "));

    // List of players participating in the game
    std::vector<Player> players;

    // Example players (you can add more players if needed)
    players.push_back({"Player1", "Red", 0});
    players.push_back({"Player2", "Blue", 0});
    // Add more players...

    // Game loop
    int currentPlayerIndex = 0;
    int totalBoxesTaken = 0;

    while (totalBoxesTaken < BOX_AREA) {
        Player& currentPlayer = players[currentPlayerIndex];

        //  code to receive player input and take over a box on the board.
        // need to handle player actions, update the board, and check for box ownership based on the conditions 
        // .

        int row, col;
        std::cout << currentPlayer.name << "'s turn. Enter row and column (0 to 7): ";
        std::cin >> row >> col;

        if (!isBoxTakenOver(board, row, col)) {
            updateBoard(board, row, col, currentPlayer.penColor);
            float boxPercentage = calculateBoxPercentage(board, row, col, currentPlayer.penColor);

            if (boxPercentage >= 50.0) {
                currentPlayer.numBoxesTaken++;
                totalBoxesTaken++;
            }
        }

        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    // Game ended, display the results
    std::cout << "Game Over! Results:\n";
    for (const Player& player : players) {
        std::cout << player.name << ": " << player.numBoxesTaken << " boxes\n";
    }

    // Determine the winner or if it's a tie
  
    
}