package painters;

import linkage_model.tuple;
import linkage_model.linkage;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * An object for painting linkages!
 */
public class linkage_painter extends JPanel {

    // The linkage to be drawn
    private linkage l;

    /**
     * Instantiates this object with the provided linkage
     * @param l, the linkage to be drawn
     */
    public linkage_painter(linkage l) {
        super(); this.l = l;
    }

    /**
     * Updates the linkage object to be drawn
     * @param l
     */
    public void update_linkage(linkage l){this.l = l;}

    /**
     * Paints the linkage to the screen with the provided graphics object:
     * @param g the graphics object used to paint the linkage
     */
    public void paint_linkage(Graphics g) {
        super.paintComponent(g);

        if(l.getA0() == null || l.getA1() == null) return;

        Graphics2D g2d = (Graphics2D)g;

        g2d.setPaint(Color.blue);

        l.update_anchors(l.getA0(), l.getA1());

        if(l.getTower_points() == null){return;}

        for(int i = 0; i < l.getTower_points().size()-2; i++){
            g2d.setStroke(new BasicStroke(3));

            if(i%2 == 0){
                g2d.setPaint(Color.PINK);
            }else{
                g2d.setPaint(Color.ORANGE);
            }
            tuple p0 = l.getTower_points().get(i);
            tuple p1 = l.getTower_points().get(i+2);

            g2d.draw(new Line2D.Double(p0.get_x(), p0.get_y(),
                    p1.get_x(), p1.get_y()));

            if(i >1) {
                g2d.setStroke(new BasicStroke(1));
                g2d.setPaint(Color.black);
                g2d.draw(new Ellipse2D.Double(p0.get_x() - 5, p0.get_y() - 5, 10, 10));
            }

        }
    }



}

