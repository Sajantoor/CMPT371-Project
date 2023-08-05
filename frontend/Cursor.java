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
                // convert mouse coordinates to relative x and y coordinates

                // get mouse location relative to the frame
                int x = e.getX();
                int y = e.getY();

                double resX = (double) x / frame.getWidth();
                double resY = (double) y / frame.getHeight();

                String message = String.format("%s %f %f", Constants.cursorCommand, resX, resY);
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
        this.cursorX = 0;
        this.cursorY = 0;
        cursorLabel.setBounds(cursorX, cursorY, cursorImage.getWidth(), cursorImage.getHeight());

        this.frame.add(cursorLabel);
    }

    private int calculateHotspotX() {
        return cursorImage.getWidth() / 2 - 15;
    }

    private int calculateHotspotY() {
        return cursorImage.getHeight() / 2 + 5;
    }

    public void move(double x, double y) {
        this.cursorX = (int) (x * frame.getWidth()) + calculateHotspotX();
        this.cursorY = (int) (y * frame.getHeight()) - calculateHotspotY();

        cursorLabel.setBounds(this.cursorX, this.cursorY, cursorLabel.getWidth(), cursorLabel.getHeight());
        this.repaint();
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
