package custom_buttons;

import linkage_model.tuple;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A simple button interface which allows me to store button objects in an array
 */
public interface button {
    void pressed(MouseEvent e, tuple mouse);
    void draw(Graphics g, tuple mouse);
    void select();
}
