package custom_buttons;

import custom_buttons.button;
import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class toggle_button implements button {
    private tuple pos;
    private tuple size;
    private boolean mouseOver;
    private boolean selected;
    private String text;

    public toggle_button(String text){
        this.pos = new tuple(0, 0);
        this.size = new tuple(20, 10);
        this.mouseOver = false;
        this.selected = false;
        this.text = text;
    }
    @Override
    public void pressed(MouseEvent e, int mouseX, int mouseY) {
        if(mouseOver){
            select();
        }
    }

    public void released(MouseEvent e, int mouseX, int mouseY){
        if(mouseOver && selected){selected = true;}
    }

    public void select(){this.selected = !this.selected;}

    @Override
    public void draw(Graphics g, int mouseX, int mouseY) {
        Graphics2D g2d = (Graphics2D)g;

        if((mouseX > pos.get_x() - size.get_x()) && (mouseX < pos.get_x() + size.get_x()) &&
                (mouseY > pos.get_y() - size.get_y()) && (mouseY < pos.get_y() + size.get_y())){
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

    public void setPos(tuple pos) {this.pos = pos;}
    public void setSize(tuple size) {this.size = size;}
    public boolean getSelected(){return this.selected;}
    public tuple getPos(){return this.pos;}
}
