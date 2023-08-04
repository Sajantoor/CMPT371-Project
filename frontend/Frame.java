import java.awt.Dimension;

import javax.swing.JFrame;

public class Frame {
    private static Frame frame;
    private static JFrame jFrame;

    private Frame() {
        jFrame = new JFrame("Deny and Conquer");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(400, 400));
    }

    public static Frame getInstance() {
        if (frame == null) {
            frame = new Frame();
        }
        return frame;
    }

    public void show() {
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

    public JFrame getFrame() {
        return jFrame;
    }
}
