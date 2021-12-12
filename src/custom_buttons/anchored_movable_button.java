package custom_buttons;

import linkage_model.tuple;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A class which extends a moveable button, allowing for unconstrained movement
 * within an interval. The interval is drawn as part of this objects draw() method.
 */
public class anchored_movable_button extends movable_button {
    //Class composition!
    private movable_button b0;
    private movable_button b1;
    private double current_dist;
    private tuple op_range;

    /**
     * Instantiates an anchored movable button by wrapping two other movable buttons, setting the op_range
     * @param b0, the first movable button
     * @param b1, the second movable button
     * @param op_range, the range in which motion will be allowed
     */
    public anchored_movable_button(movable_button b0,
                                   movable_button b1,
                                   tuple op_range){

        this.b0 = b0;
        this.b1 = b1;
        this.op_range = op_range;
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                                       b0.get_position().get_y()-b1.get_position().get_y())).norm();
    }

    //checks to see if the anchor points are within the op_range
    private static int x_in_interval(tuple A, double x){return (x>=A.get_x()) && (x<=A.get_y()) ? 1:0;}

    @Override
    public void pressed(MouseEvent e, tuple mouse) {
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                b0.get_position().get_y()-b1.get_position().get_y())).norm();
        if (x_in_interval(this.op_range, this.current_dist)==1){
            b1.pressed(e, mouse);
            b0.pressed(e, mouse);

        }

    }

    @Override
    public void dragged(MouseEvent e, tuple mouse){
        this.current_dist = (new tuple(b0.get_position().get_x()-b1.get_position().get_x(),
                b0.get_position().get_y()-b1.get_position().get_y())).norm();
        if (x_in_interval(this.op_range, this.current_dist)==1){
            b1.dragged(e, mouse);
            b0.dragged(e, mouse);
        }
    }

    @Override
    public void draw(Graphics g, tuple mouse) {
        if (x_in_interval(this.op_range, this.current_dist)==1) {
            b0.draw(g, mouse);
            b1.draw(g, mouse);
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
