# CMPT 371 Final Project

<!-- Description of the game and your design, including your application-layer messaging scheme.
Please show the code snippets where you are:
i. Opening sockets
ii. Handling the shared object
b. A list of group members and their % contribution to the project.
c. Commented source code of the client and the server. Alternatively you can include a link to
Github or other repositories, though the code still has to be commented.
d. Video of a working demo. Upload the video somewhere and put its link in the final report.
The video must be 1 to 2 minutes and show at least 2 players playing the game, and must
include the shared object in action -->

# Description of Game

The game we've created is Deny and Conquer. Deny and Conquer is a strategic multiplayer board game played on an 8x8 square grid. Each player has a pen of a different color, and the objective is to take over as many boxes as possible and deny opponents from filling the most number of boxes.

## Description of Design

**include high level description of the design**

The game follows a client-server architecture, where multiple players (clients) connect to a central server to play the game. The server manages the game state, enforces rules, and facilitates communication between the clients.

The game starts when a player (the host) initiates the server and waits for other players to join. The server listens for incoming connections from clients. The game board is represented as an 8x8 grid, where each cell corresponds to a box. The server maintains the state of the board, including which boxes are claimed by which players.

When a player makes a move, the server verifies if the box they selected can be claimed. If the player colors at least 50% of the box, the server updates the game state to reflect the claim, and the box turns into the color of the player. If the player colors less than 50%, the box remains unclaimed, and another player can attempt to claim it on their turn. After each move, the server sends updates to all connected clients, informing them of the current state of the game board and whose turn it is.

<!--Description of the Front End Design -->

## Description of Messaging Scheme

**include opening sockets snippets**
**handling of shared object**

# Group Members

Arjun Singh - 20%

Gurinder Bhogal - 20%

Jessy Chahal - 20%

Sajan Toor - 20%

Yousef Haiba - 20%

# Video of Demo

**Include video here**
