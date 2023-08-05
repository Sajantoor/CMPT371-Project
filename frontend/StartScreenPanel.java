import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreenPanel extends JPanel {
    public StartScreenPanel() {
        setLayout(new BorderLayout());

    Border titledBorder = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 2), "Deny and Conquer",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 30),
                    Color.BLACK);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(20, 20, 20, 20),
                    titledBorder
            ));

        JLabel infoLabel = new JLabel("Become the ultimate champion!");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        add(infoLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setPreferredSize(new Dimension(100, 50));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(255, 192, 203));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Set the panel background to be transparent
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setBackground(new Color(230, 230, 230)); // Set background color
    }

    private void startGame() {
        String message = String.format("%s", Constants.startCommand);
        ClientSocket.getInstance().send(message);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw a gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(255, 235, 205), // Start color
                getWidth(), getHeight(), new Color(255, 192, 203) // End color
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}