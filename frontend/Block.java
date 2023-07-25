import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Block extends JPanel {
    private boolean isDrawing = false;      // Flag to track if the player is currently drawing in box
    private Color crayonColor = Color.BLUE; 
    private static Color backgroundColor = Color.WHITE; 
    private int totalBoxArea = 0; 
    private int coloredArea = 0;
    private int lastXValue = -1;
    private int lastYValue = -1;

    Block() {
        setPreferredSize(new Dimension(80, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDrawing = true;
                totalBoxArea = getWidth() * getHeight(); 
                draw(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
                setBackground(coloredArea >= 0.5 * totalBoxArea ? crayonColor : backgroundColor);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing == true) {
                    draw(e);
                }
            }
        });
    }

    private void draw(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
    
        // Check if drawing is within the bounds
        if (x < 0) x = 0;
        if (x >= getWidth()) x = getWidth() - 1;
        
        if (y < 0) y = 0;
        if (y >= getHeight()) y = getHeight() - 1;
        

    
        // If this is the first pixel being drawn, store its position
        if (lastXValue == -1 || lastYValue == -1) {
            lastXValue = x;
            lastYValue = y;
            return;
        }
    
        // Draw a line from the last known position to the current position
        Graphics2D graphics_line = (Graphics2D) getGraphics();
        graphics_line.setColor(crayonColor);
        graphics_line.setStroke(new BasicStroke(5));
        graphics_line.drawLine(lastXValue, lastYValue, x, y);
    
        // Update the colored area based on the distance between the two points
        int dx = x - lastXValue;
        int dy = y - lastYValue;
        coloredArea += Math.sqrt(dx * dx + dy * dy);
    
        // Make sure the colored area doesn't exceed the total area of the box
        coloredArea = Math.min(coloredArea, totalBoxArea);
    
        lastXValue = x;
        lastYValue = y;
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
}
