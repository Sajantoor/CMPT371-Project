import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

class Cursor extends JComponent {
    private JFrame frame;

    Cursor(JFrame frame) {
        this.frame = frame;

        // Load the cursor image
        BufferedImage cursorImage = loadCursorImage(Color.BLUE);
        int hotspotX = cursorImage.getWidth() / 2 - 15;
        int hotspotY = cursorImage.getHeight() / 2 + 5;
        java.awt.Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(hotspotX, hotspotY), "crayonCursor");
        // Set the custom cursor for the entire JFrame
        this.frame.setCursor(customCursor);
    }
    
    // Get cursor image
    private static BufferedImage loadCursorImage(Color crayonColor) {
        try {
            // import the crayon image
            URL imageURL = App.class.getResource("assets/crayon_32px.png");
            BufferedImage image = ImageIO.read(imageURL);

            // Create a new BufferedImage with the same dimensions as the original image
            BufferedImage buffImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = buffImage.createGraphics();

            g.setColor(crayonColor);
            g.drawImage(image, 0, 0, null);

            // Dispose of the Graphics object
            g.dispose();

            return buffImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
