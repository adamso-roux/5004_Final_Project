package custom_buttons;

import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * A class for a toggle button! :) A rectangle that can be selected and unselected
 */
public class toggle_button implements button {
    //the location of the button
    private tuple pos;
    //the size of the button
    private tuple size;
    //A boolean representing if the mouse is over the button
    private boolean mouseOver;
    //A boolean representing if the button is selected
    private boolean selected;
    //The text to be written inside the button
    private String text;

    /**
     * Creates a toggle button object with a name!
     * @param text the text to be written inside the button
     */
    public toggle_button(String text){
        this.pos = new tuple(0, 0);
        this.size = new tuple(20, 10);
        this.mouseOver = false;
        this.selected = false;
        this.text = text;
    }

    /**
     * If the mouse is over the button and it's been pressed, then we flip the
     * selected instance variable.
     * @param e the mouse event
     * @param mouse the location of the mouse
     */
    @Override
    public void pressed(MouseEvent e, tuple mouse) {
        if(mouseOver){
            select();
        }
    }


    /**
     * Flips the selected boolean variable
     */
    public void select(){this.selected = !this.selected;}

    /**
     * Draws the toggle button and updates the mouseOver instance variable
     * @param g, the graphics object used to draw the button
     * @param mouse, the current location of the mouse
     */
    @Override
    public void draw(Graphics g, tuple mouse) {
        Graphics2D g2d = (Graphics2D)g;

        if((mouse.get_x() > pos.get_x() - size.get_x()) && (mouse.get_x() < pos.get_x() + size.get_x()) &&
                (mouse.get_y() > pos.get_y() - size.get_y()) && (mouse.get_y() < pos.get_y() + size.get_y())){
            mouseOver = true;
        }else{
            mouseOver = false;
        }

        if (selected) {
            g2d.setPaint(Color.red);
        } else {
            g2d.setPaint(Color.black);
        }

        g2d.draw(new Rectangle2D.Double(this.pos.get_x(), this.pos.get_y()-this.size.get_y()/2,
                this.size.get_x(), this.size.get_y()));
        g2d.drawString(this.text,
                (int)(this.pos.get_x()+this.size.get_x()/8),
                (int)(this.pos.get_y()+this.size.get_y()/6));
    }

    //Simple getters and setters
    public void setPos(tuple pos) {this.pos = pos;}
    public void setSize(tuple size) {this.size = size;}
    public boolean getSelected(){return this.selected;}
    public tuple getPos(){return this.pos;}
}
