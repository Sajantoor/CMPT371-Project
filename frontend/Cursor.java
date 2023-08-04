import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

class Cursor extends JComponent {
    private JFrame frame;
    private Color color = Color.BLUE;
    private BufferedImage cursorImage;
    private JLabel cursorLabel;
    private int cursorX = 0;
    private int cursorY = 0;
    private int playerID = 0;

    public Cursor() {
        this.frame = Frame.getInstance().getFrame();

        cursorImage = loadCursorImage(color);

        java.awt.Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage,
                new Point(calculateHotspotX(), calculateHotspotY()),
                "crayonCursor");

        // Set the custom cursor for the entire JFrame
        this.frame.setCursor(customCursor);
        this.frame.add(this);

        // Add event listeners for mouse movement
        this.frame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                String message = String.format("%s %d %d", Constants.cursorCommand, e.getX(), e.getY());
                ClientSocket.getInstance().send(message);
            }
        });
    }

    public Cursor(int playerID) {
        this.frame = Frame.getInstance().getFrame();
        this.playerID = playerID;

        // Load the cursor image
        cursorImage = loadCursorImage(Constants.playerColors[playerID]);
        cursorLabel = new JLabel(new ImageIcon(cursorImage));

        // add the cursor image to the frame and set the cursor to the center of the
        // image
        this.cursorX = cursorImage.getWidth() / 2 - calculateHotspotX();
        this.cursorY = cursorImage.getHeight() / 2 - calculateHotspotY();
        cursorLabel.setBounds(cursorX, cursorY, cursorImage.getWidth(), cursorImage.getHeight());

        this.frame.add(cursorLabel);
    }

    private int calculateHotspotX() {
        return cursorImage.getWidth() / 2 - 15;
    }

    private int calculateHotspotY() {
        return cursorImage.getHeight() / 2 + 5;
    }

    public void move(int x, int y) {
        this.cursorX = x - calculateHotspotX();
        this.cursorY = y - calculateHotspotY();

        cursorLabel.setBounds(x, y, cursorLabel.getWidth(), cursorLabel.getHeight());
        frame.repaint();
    }

    public int getPlayerID() {
        return playerID;
    }

    // Get cursor image
    private static BufferedImage loadCursorImage(Color crayonColor) {
        try {
            // import the crayon image
            URL imageURL = App.class.getResource("assets/crayon_32px.png");
            BufferedImage image = ImageIO.read(imageURL);

            // Create a new BufferedImage with the same dimensions as the original image
            BufferedImage buffImage = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = buffImage.createGraphics();

            g.setColor(crayonColor);
            g.drawImage(image, 0, 0, null);

            // Dispose of the Graphics object
            g.dispose();

            return buffImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
