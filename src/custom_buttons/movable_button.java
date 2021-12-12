package custom_buttons;

import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A draggable button, used for moving the linkage structures and drawing a target trace
 */
public class movable_button implements button {

    //The location of the button
    private tuple pos;
    //The change in location while dragged
    private tuple dpos;
    //The size of the button
    private int width;

    //A boolean representing if the mouse is over the button
    private boolean mouseOver;
    //A boolean representing if the button is selected
    private boolean selected;

    /**
     * A no argument constructor for this button object, sets default values
     */
    public movable_button(){
        selected = false;
        this.width = 30;
        pos = new tuple(100, 100);
        dpos = new tuple(0, 0);
    }

    //Simple getters and setters :)
    public void set_position(tuple pos){this.pos = pos;}
    public tuple get_position(){return this.pos;}
    public boolean get_mouse_over(){return this.mouseOver;}
    public boolean get_selected(){return this.selected;}
    public void select(){this.selected = !this.selected;}


    /**
     * Draws the toggle button and updates the mouseOver instance variable
     * @param g, the graphics object used to draw the button
     * @param mouse, the current location of the mouse
     */
    public void draw(Graphics g, tuple mouse){
        Graphics2D g2d = (Graphics2D)g;

        if((mouse.get_x() > pos.get_x() - width/2) && (mouse.get_x() < pos.get_x() + width/2) &&
           (mouse.get_y() > pos.get_y() - width/2) && (mouse.get_y() < pos.get_y() + width/2)){

            mouseOver = true;
            g2d.setPaint(Color.red);
        }else{
            mouseOver = false;
            selected = false;
            g2d.setPaint(Color.black);

        }

        g2d.drawOval((int)pos.get_x()-this.width/2, (int)pos.get_y()-this.width/2,
                this.width, this.width);
    }


    /**
     * If the mouse is over the button and it's been pressed, we update the dpos instance variable
     * so that the location of the button can be changed
     * @param e the mouse event
     * @param mouse the location of the mouse
     */
    public void pressed(MouseEvent e, tuple mouse){
        if(mouseOver) {
            if(selected) dpos = new tuple(mouse.get_x() - pos.get_x(), mouse.get_y() - pos.get_y());
        }
    }

    /**
     * If the button is selected and the mouse is over it, we then update the position:
     * @param e the mouse event
     * @param mouse the location of the mouse
     */
    public void dragged(MouseEvent e, tuple mouse){
        if(selected && mouseOver) {
            pos = new tuple(mouse.get_x() - dpos.get_x(), mouse.get_y() - dpos.get_y());
        }
    }


}
