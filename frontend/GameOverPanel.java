import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(String[] playerScores) {
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        add(gameOverLabel, BorderLayout.NORTH);

        JTextArea scoresTextArea = new JTextArea();
        scoresTextArea.setFont(new Font("Arial", Font.PLAIN, 24));
        scoresTextArea.setEditable(false);
        scoresTextArea.setOpaque(false);
        scoresTextArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        displayPlayerScores(playerScores, scoresTextArea);
        add(scoresTextArea, BorderLayout.CENTER);
    }

    private void displayPlayerScores(String[] playerScores, JTextArea scoresTextArea) {
        for (int i = 0; i < playerScores.length; i++) {
            String playerColor = Constants.playerColorsStrings[i];
            int score = Integer.parseInt(playerScores[i]);
            scoresTextArea.append(playerColor + ": " + score + "\n");
        }
    }
}
