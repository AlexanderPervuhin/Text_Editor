import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;


class BorderFactory {
    public static void setMargin(JComponent component, int top, int right, int bottom, int left) {
        Border border = component.getBorder();
        Border marginBorder = new EmptyBorder(new Insets(top, left, bottom, right));
        component.setBorder(border == null ? marginBorder
                : new CompoundBorder(marginBorder, border));
    }
}