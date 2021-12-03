import linkage_model.tuple;

import java.awt.*;
import java.awt.event.MouseEvent;

public class anchored_moveable_button extends moveable_button{

    private moveable_button b0;
    private moveable_button b1;
    private double current_dist;
    private tuple op_range;

    public anchored_moveable_button(moveable_button b0,
                                    moveable_button b1,
                                    tuple op_range){

        this.b0 = b0;
        this.b1 = b1;
        this.op_range = op_range;
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                                       b0.get_position().get_y()-b1.get_position().get_y())).norm();
    }

    private static int x_in_interval(tuple A, double x){return (x>=A.get_x()) && (x<=A.get_y()) ? 1:0;}

    @Override
    public void pressed(MouseEvent e, int mouseX, int mouseY) {
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                b0.get_position().get_y()-b1.get_position().get_y())).norm();
        if (x_in_interval(this.op_range, this.current_dist)==1){
            b1.pressed(e, mouseX, mouseY);
            b0.pressed(e, mouseX, mouseY);

        }

    }

    @Override
    public void dragged(MouseEvent e, int mouseX, int mouseY){
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                b0.get_position().get_y()-b1.get_position().get_y())).norm();
        if (x_in_interval(this.op_range, this.current_dist)==1){
            b1.dragged(e, mouseX, mouseY);
            b0.dragged(e, mouseX, mouseY);
        }
    }

    @Override
    public void draw(Graphics g, int mouseX, int mouseY) {
        if (x_in_interval(this.op_range, this.current_dist)==1) {
            b0.draw(g, mouseX, mouseY);
            b1.draw(g, mouseX, mouseY);
        }

        if(b0.get_mouse_over() && b0.get_selected()){
            g.setColor(new Color(152, 255, 152, 100 ));
            g.fillOval((int)b1.get_position().get_x()-(int)this.op_range.get_y(),
                    (int)b1.get_position().get_y()-(int)this.op_range.get_y(),
                    2*(int)this.op_range.get_y(), 2*(int)this.op_range.get_y());
            g.setColor(Color.white);
            g.fillOval((int)b1.get_position().get_x()-(int)this.op_range.get_x(),
                    (int)b1.get_position().get_y()-(int)this.op_range.get_x(),
                    2*(int)this.op_range.get_x(), 2*(int)this.op_range.get_x());

            g.setColor(Color.red);
        }

        if(b1.get_mouse_over() && b1.get_selected()){
            g.setColor(new Color(152, 255, 152, 100 ));
            g.fillOval((int)b0.get_position().get_x()-(int)this.op_range.get_y(),
                    (int)b0.get_position().get_y()-(int)this.op_range.get_y(),
                    2*(int)this.op_range.get_y(), 2*(int)this.op_range.get_y());
            g.setColor(Color.white);
            g.fillOval((int)b0.get_position().get_x()-(int)this.op_range.get_x(),
                    (int)b0.get_position().get_y()-(int)this.op_range.get_x(),
                    2*(int)this.op_range.get_x(), 2*(int)this.op_range.get_x());
            g.setColor(Color.red);
        }
    }
}
