import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreenPanel extends JPanel {
    public StartScreenPanel() {
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Deny and Conquer");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        add(gameOverLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startGame() {
        String message = String.format("%s", Constants.startCommand);
        ClientSocket.getInstance().send(message);
        Screens.getInstance().createAndShowGUI();
    }
}
