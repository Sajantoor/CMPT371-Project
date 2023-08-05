import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    public GameOverPanel() {
        setLayout(new BorderLayout());
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        add(gameOverLabel, BorderLayout.CENTER);
    }
}
