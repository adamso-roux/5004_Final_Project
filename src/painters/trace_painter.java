package painters;

import linkage_model.tuple;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;

/**
 * An object for painting traces!
 */
public class trace_painter extends JPanel {

    //The trace to be drawn
    private ArrayList<tuple> trace;
    //The color of the trace
    private Color c;
    //The stroke size of the trace
    private float stroke;


    /**
     * Instantiates this painter object with the trace to be drawn:
     * @param trace
     */
    public trace_painter(ArrayList<tuple> trace){
        this.trace = trace;
        this.c = Color.black;
        this.stroke = 1;

    }

    //Setters and getters!
    public void update_trace(ArrayList<tuple> trace){this.trace = trace;}
    public void set_stroke(float new_stroke){this.stroke = new_stroke;}private int display_length;
    public void set_color(Color new_color){this.c = new_color;}

    /**
     * Paints the trace with the provided graphics object:
     * @param g, the graphics object used to paint the trace
     */
    public void paint_trace(Graphics g){
        ((Graphics2D)g).setStroke(new BasicStroke(this.stroke));
        g.setColor(this.c);
        if(trace.size() > 0) {
            g.drawOval((int)(trace.get(0).get_x()-2.5), (int)(trace.get(0).get_y()-0.5), 5, 5);
        }
        for (int i = 0; i < trace.size() - 1; i++) {
            tuple t0 = trace.get(i);
            tuple t1 = trace.get(i + 1);
            g.drawLine((int) t0.get_x(), (int) t0.get_y(),
                    (int) t1.get_x(), (int) t1.get_y());
        }

    }
}
