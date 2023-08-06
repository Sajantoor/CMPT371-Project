import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel(String[] playerScores) {
        setLayout(new BorderLayout());
        setOpaque(false); // Make the panel transparent

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        gameOverLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw a gradient background (similar to the home screen)
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 235, 205), // Start color
                getWidth(), getHeight(), new Color(255, 192, 203) // End color
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw a border (similar to the home screen)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
