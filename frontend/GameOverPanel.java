import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(String[] playerScores) {
        setLayout(new BorderLayout());
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        add(gameOverLabel, BorderLayout.NORTH);

        JTextArea scoresTextArea = new JTextArea();
        scoresTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        scoresTextArea.setEditable(false);
        add(scoresTextArea, BorderLayout.CENTER);

        displayPlayerScores(playerScores, scoresTextArea);
    }

    private void displayPlayerScores(String[] playerScores, JTextArea scoresTextArea) {

        for (int i = 0; i < playerScores.length; i++) {
            String playerColor = Constants.playerColorsStrings[i];
            int score = Integer.parseInt(playerScores[i]);
            scoresTextArea.append(playerColor + ": " + score + "\n");
        }
    }
}
