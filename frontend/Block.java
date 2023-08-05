import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class Block extends JPanel {
    private int xCoord; // x coordinate of the block
    private int yCoord; // y coordinate of the block
    private boolean isDrawing = false;
    private boolean captured = false;
    private ClientSocket socket = ClientSocket.getInstance();
    private Color crayonColor = Constants.playerColors[socket.getPlayerID()];
    private static Color backgroundColor = Color.WHITE;
    private int totalBoxArea = 0;
    private int coloredArea = 0;
    private int lastXValue = -1;
    private int lastYValue = -1;
    private List<Point> drawnPointsInBox = new ArrayList<>();

    Block(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        setPreferredSize(new Dimension(80, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (captured) {
                    return;
                }
                isDrawing = true;
                totalBoxArea = getWidth() * getHeight();
                draw(e);

                // Send the draw command to the server
                String message = String.format("%s %d %d", Constants.startDrawCommand, xCoord, yCoord);
                socket.send(message);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (captured) {
                    return;
                }
                isDrawing = false;
                double threshold = 0.25 * totalBoxArea;

                setBackground(coloredArea >= threshold ? crayonColor : backgroundColor);

                if (coloredArea < threshold) {
                    String message = String.format("%s %d %d", Constants.clearPixels, xCoord, yCoord);
                    socket.send(message);
                    clearLines();
                } else {
                    // Send the end draw command to the server
                    String message = String.format("%s %d %d", Constants.captureCommand, xCoord, yCoord);
                    socket.send(message);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (captured) {
                    return;
                }
                if (isDrawing) {
                    draw(e);
                    String message = String.format("%s %d %d %d %d", Constants.drawingPixels, xCoord, yCoord, e.getX(), e.getY());
                    socket.send(message);
                }
            }
        });
    }

    private void draw(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Check if drawing is within the bounds
        if (x < 0 || x >= getWidth())
            return;

        if (y < 0 || y >= getHeight())
            return;

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

    public void clearLines() {
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

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    public void setCaptured(int playerID) {
        captured = true;
        setBackground(Constants.playerColors[playerID]);
    }

    public void draw_pixel(int x, int y, int playerID) {
        System.out.println("Drawing pixel at " + x + " " + y);
        if (captured) {
            return;
        }

        // clear lines if less then threashold
        if (coloredArea < 0.25 * totalBoxArea) {
            clearLines();
        }

        // Add the point to the list of drawn points and repaint the panel
        drawnPointsInBox.add(new Point(x, y));
        repaint();
    }

    public void clearPixels() {
        clearLines();
    }
}
