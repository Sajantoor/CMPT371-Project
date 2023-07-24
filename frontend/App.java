import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Deny and Conquer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Deny and Conquer");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        containerPanel.add(title);

        JPanel boardPanel = new JPanel(new GridLayout(4, 4, 5, 5)) {
            @Override
            public Insets getInsets() {
                return new Insets(0, 0, 0, 0);
            }
        };

        for (int i = 0; i < 16; i++) {
            Block block = new Block();
            boardPanel.add(block);
        }
        containerPanel.add(boardPanel);

        Cursor cursor = new Cursor();
        containerPanel.add(cursor);

        frame.add(containerPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class Block extends JPanel {
    private boolean colored = false;

    Block() {
        setPreferredSize(new Dimension(80, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colored = !colored;
                setBackground(colored ? Color.BLUE : Color.GRAY);
            }
        });
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}

class Cursor extends JPanel {
    private int x;
    private int y;

    Cursor() {
        setPreferredSize(new Dimension(30, 30));
        setOpaque(false); // Remove the background color
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                setLocation(x - 15, y - 15);
                repaint();
            }
        });
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
