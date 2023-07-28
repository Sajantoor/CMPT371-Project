import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

class Block extends JPanel {
    private boolean isDrawing = false;
    private Color crayonColor = Color.BLUE;
    private static Color backgroundColor = Color.WHITE;
    private int totalBoxArea = 0;
    private int coloredArea = 0;
    private int lastXValue = -1;
    private int lastYValue = -1;
    private List<Point> drawnPointsInBox = new ArrayList<>();
    private JFrame frame;

    Block(JFrame frame) {
        setPreferredSize(new Dimension(80, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.frame = frame;

        // Load the cursor image
        BufferedImage cursorImage = loadCursorImage(crayonColor);
        int hotspotX = cursorImage.getWidth() / 2 - 15;
        int hotspotY = cursorImage.getHeight() / 2 + 5;
        java.awt.Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(hotspotX, hotspotY), "crayonCursor");
        // Set the custom cursor for the entire JFrame
        this.frame.setCursor(customCursor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDrawing = true;
                totalBoxArea = getWidth() * getHeight();
                draw(e);

                // Send the draw command to the server
                String message = String.format("%s %d %d", Constants.drawCommand, e.getX(), e.getY());
                ClientSocket.getInstance().send(message);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
                setBackground(coloredArea >= 0.25 * totalBoxArea ? crayonColor : backgroundColor);
                if (coloredArea < 0.25 * totalBoxArea) {
                    clearLines();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing) {
                    draw(e);
                }
            }
        });
    }

    private void draw(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Check if drawing is within the bounds
        if (x < 0)
            x = 0;
        if (x >= getWidth())
            x = getWidth() - 1;

        if (y < 0)
            y = 0;
        if (y >= getHeight())
            y = getHeight() - 1;

        // If this is the first pixel being drawn, store its position
        if (lastXValue == -1 || lastYValue == -1) {
            lastXValue = x;
            lastYValue = y;
            return;
        }

        // Update the colored area based on the distance between the two points
        int dx = x - lastXValue;
        int dy = y - lastYValue;
        coloredArea += Math.sqrt(dx * dx + dy * dy);

        // Make sure the colored area doesn't exceed the total area of the box
        coloredArea = Math.min(coloredArea, totalBoxArea);

        // Store the drawn points to be able to clear them later if area < 50%
        drawnPointsInBox.add(new Point(x, y));
        repaint();

        lastXValue = x;
        lastYValue = y;
    }

    private void clearLines() {
        // Reset the colored area and the stored drawn points
        coloredArea = 0;
        drawnPointsInBox.clear();
        lastXValue = -1;
        lastYValue = -1;

        // Repaint the panel
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all the lines based on the stored points
        g2d.setColor(crayonColor);
        g2d.setStroke(new BasicStroke(5));
        for (int i = 0; i < drawnPointsInBox.size() - 1; i++) {
            Point p1 = drawnPointsInBox.get(i);
            Point p2 = drawnPointsInBox.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
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


    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
