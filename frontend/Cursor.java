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
        BufferedImage cursorImage = loadCursorImage();
        int hotspotX = cursorImage.getWidth() / 2 - 15;
        int hotspotY = cursorImage.getHeight() / 2 + 5;
        java.awt.Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point(hotspotX, hotspotY), "crayonCursor");
        // Set the custom cursor for the entire JFrame
        this.frame.setCursor(customCursor);
    }

    // Get cursor image
    private static BufferedImage loadCursorImage() {
        try {
            String crayonColorName = Constants.ColorNames[ClientSocket.getInstance().getPlayerID()];
            String crayonImageName = String.format("crayon_%s.png", crayonColorName);
            URL imageURL = App.class.getResource("assets/" + crayonImageName);
    
            // Import the crayon image
            BufferedImage image = ImageIO.read(imageURL);
    
            // Create a new BufferedImage with the same dimensions as the original image
            BufferedImage buffImage = new BufferedImage(image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = buffImage.createGraphics();
    
            g.drawImage(image, 0, 0, null);
    
            // Dispose of the Graphics object
            g.dispose();
    
            return buffImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
