import javax.swing.*;
import java.awt.*;

public class Block extends JPanel {
    private int row; 
    private int col; 

    Block(int row, int col) {
        this.row = row; 
        this.col = col;
        setPreferredSize(new Dimension(80, 80));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getHeight() {
        return super.getHeight();
    }

    public int getWidth() {
        return super.getWidth();
    }
}
