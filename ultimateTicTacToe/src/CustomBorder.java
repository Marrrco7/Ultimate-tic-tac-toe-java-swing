package ultimateTicTacToe;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class CustomBorder extends AbstractBorder {
    private final int top;
    private final int left;
    private final int bottom;
    private final int right;

    public CustomBorder(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, top);
        g.fillRect(x, y, left, height);
        g.fillRect(x, y + height - bottom, width, bottom);
        g.fillRect(x + width - right, y, right, height);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(top, left, bottom, right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = left;
        insets.top = top;
        insets.right = right;
        insets.bottom = bottom;
        return insets;
    }
}
