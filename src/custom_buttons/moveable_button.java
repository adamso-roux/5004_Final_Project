package custom_buttons;

import custom_buttons.button;
import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;

public class moveable_button implements button {

    private tuple pos;
    private tuple dpos;
    private int width;

    private boolean mouseOver;
    private boolean selected;

    public moveable_button(){
        selected = false;
        this.width = 30;
        pos = new tuple(100, 100);
        dpos = new tuple(0, 0);
    }

    public void set_position(tuple pos){this.pos = pos;}
    public tuple get_position(){return this.pos;}
    public boolean get_mouse_over(){return this.mouseOver;}
    public boolean get_selected(){return this.selected;}
    public void select(){this.selected = !this.selected;}

    public void draw(Graphics g, int mouseX, int mouseY){
        Graphics2D g2d = (Graphics2D)g;


        if((mouseX > pos.get_x() - width/2) && (mouseX < pos.get_x() + width/2) &&
           (mouseY > pos.get_y() - width/2) && (mouseY < pos.get_y() + width/2)){

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


    public void pressed(MouseEvent e, int mouseX, int mouseY){
        if(mouseOver) {
            if(selected) dpos = new tuple(mouseX - pos.get_x(), mouseY - pos.get_y());
        }
    }

    public void dragged(MouseEvent e, int mouseX, int mouseY){
        if(selected && mouseOver) {
            pos = new tuple(mouseX - dpos.get_x(), mouseY - dpos.get_y());
        }
    }


}
