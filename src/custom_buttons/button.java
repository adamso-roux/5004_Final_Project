package custom_buttons;

import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface button {
    void pressed(MouseEvent e, int mouseX, int mouseY);
    tuple pos = new tuple();
    boolean mouseOver = false;
    boolean selected = true;
    public void draw(Graphics g, int mouseX, int mouseY);
    public void select();
}
