package painters;

import linkage_model.tuple;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;

public class trace_painter extends JPanel {

    private ArrayList<tuple> trace;
    private Color c;
    private float stroke;
    private int display_length;

    public trace_painter(ArrayList<tuple> trace){
        this.trace = trace;
        this.c = Color.black;
        this.stroke = 1;

    }

    public void update_trace(ArrayList<tuple> trace){this.trace = trace;}
    public void set_stroke(float new_stroke){this.stroke = new_stroke;}
    public void set_color(Color new_color){this.c = new_color;}

    public void paint_trace(Graphics g){
        ((Graphics2D)g).setStroke(new BasicStroke(this.stroke));
        g.setColor(this.c);
        for (int i = 0; i < trace.size() - 1; i++) {
            tuple t0 = trace.get(i);
            tuple t1 = trace.get(i + 1);
            g.drawLine((int) t0.get_x(), (int) t0.get_y(),
                    (int) t1.get_x(), (int) t1.get_y());
        }

    }
}
