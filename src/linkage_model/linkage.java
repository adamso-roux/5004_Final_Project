package linkage_model;
import org.jetbrains.annotations.NotNull;

import java.lang.Math;
import java.util.ArrayList;

/**
 * A class which instantiates linkage objects given an array of quads,
 * includes methods for computing the tower vertices, and traces.
 */
public class linkage {
    //the first anchor point
    private tuple A0;
    //the second anchor point
    private tuple A1;
    //the interval in which the tower is well-defined
    private tuple base_interval;

    //the array of quads which defines the linkage
    private quad[] lengths;
    //the array of tuples which represents a drawn linkage object
    private ArrayList<tuple> tower_points;
    //the array of tuples which stores the location of the
    //terminal point of the linkage over the base-interval
    private ArrayList<tuple> trace;

    public linkage(quad @NotNull [] lengths) throws IllegalArgumentException{
        if(lengths.length == 0)throw new IllegalArgumentException("Intractible Linkage");
        this.lengths = lengths;
        this.base_interval = linkage_operations.get_t(this.lengths);

        //if the base interval is null, then the linkage is intractible
        if(this.base_interval == null)throw new IllegalArgumentException("Intractible Linkage");


        //setting the default parameters for the linkage
        this.A0 = new tuple(0.0, 0.0);
        this.A1 = new tuple(1, 0.0);
        this.tower_points = new ArrayList<tuple>();

    }

    /**
     * updates the anchor points for a linkage, necessary for
     * defining the linkage structure in the geometry of the jframe:
     * @param A0 the new first anchor point
     * @param A1 the new second anchor point
     */
    public void update_anchors(tuple A0, tuple A1){
        this.A0 = A0;
        this.A1 = A1;

        ArrayList<tuple> new_verts = compute_tower_vertices();
        if(new_verts!=null){
            this.tower_points = new_verts;
        }
    }

    /**
     * Returns the left-hand solution for the intersection of two circles
     * with origins p0 and p1 and radii l0 l1 respectively.
     * @param p0 the origin of the first circle
     * @param p1 the origin of the second circle
     * @param l0 the radius of the first circle
     * @param l1 the radius of the second circle
     * @return the intersection point between the two circles
     */
    private tuple get_tri(tuple p0, tuple p1, double l0, double l1){
        if(p0 == null || p1 == null) return null;
        double x0 = p0.get_x();
        double y0 = p0.get_y();
        double x1 = p1.get_x();
        double y1 = p1.get_y();

        double d = Math.sqrt((x0-x1)*(x0-x1) + (y0-y1)*(y0-y1));

        if(d > l0 + l1) return null;

        double a = (l0*l0 - l1*l1 + d*d)/(2*d);
        double h = Math.sqrt(l0*l0 - a*a);

        double x2 = x0 + a*(x1-x0)/d;
        double y2 = y0 + a*(y1-y0)/d;

        //RH solution:
        double x3 = x2 - h*(y1 - y0)/d;
        double y3 = y2 + h*(x1 - x0)/d;

        return new tuple(x3, y3);
    }

    /**
     * Uses get_tri and some vector math to return the vertices of a quad given
     * two anchor points p0 and p1
     * @param p0 the first anchor point
     * @param p1 the second anchor point
     * @param L the quad defining the linkage
     * @return the vertices of the quad
     */
    private tuple[] get_quad(tuple p0, tuple p1, quad L){
        double l0, l1, l2, l3;
        l0 = L.get_l0(); l1 = L.get_l1(); l2 = L.get_l2(); l3 = L.get_l3();

        tuple pc = get_tri(p0, p1, l0, l1);

        if(pc == null){return null;}

        tuple p1_pc_diff = new tuple(pc.get_x() - p1.get_x(), pc.get_y() - p1.get_y());
        tuple p0_pc_diff = new tuple(pc.get_x() - p0.get_x(), pc.get_y() - p0.get_y());

        double norm_p1_pc_x = p1_pc_diff.get_x() / p1_pc_diff.norm();
        double norm_p1_pc_y = p1_pc_diff.get_y() / p1_pc_diff.norm();
        double norm_p0_pc_x = p0_pc_diff.get_x() / p0_pc_diff.norm();
        double norm_p0_pc_y = p0_pc_diff.get_y() / p0_pc_diff.norm();

        tuple p2 = new tuple(norm_p0_pc_x*(l0+l2)+p0.get_x(),norm_p0_pc_y*(l0+l2)+p0.get_y());
        tuple p3 = new tuple(norm_p1_pc_x*(l3+l1)+p1.get_x(),norm_p1_pc_y*(l3+l1)+p1.get_y());

        return new tuple[]{p2, p3};
    }

    /**
     * Loops through all the quads in the linkage structure and constructs the
     * tower of points defining the linkage via the anchor points
     * @return an array of tuples representing the drawn tower
     */
    private ArrayList<tuple> compute_tower_vertices(){

        ArrayList<tuple> temp_vertices = new ArrayList<>();

        tuple[] first_points = get_quad(this.A0, this.A1, this.lengths[0]);
        if(first_points == null){return null;}
        tuple p2, p3;
        p2 = first_points[0]; p3 = first_points[1];

        temp_vertices.add(this.A0);
        temp_vertices.add(this.A1);
        temp_vertices.add(p2);
        temp_vertices.add(p3);

        tuple[] temp_quad;

        for(int i = 1; i < this.lengths.length; i++){
            if((p2 == null) || (p3 == null)){return null;}

            //the LH solution needs to be swapped to RH when
            //the index of the quad is even/odd
            if(i%2 == 1){
                temp_quad = get_quad(p3, p2, this.lengths[i]);

                if(temp_quad == null) return null;

                p3 = temp_quad[0]; p2 = temp_quad[1];
            }else{
                temp_quad = get_quad(p2, p3, this.lengths[i]);

                if(temp_quad == null) return null;

                p2 = temp_quad[0]; p3 = temp_quad[1];
            }

            temp_vertices.add(p2);
            temp_vertices.add(p3);

        }

        tuple tri = get_tri(temp_vertices.get(temp_vertices.size()-1),
                            temp_vertices.get(temp_vertices.size()-2),
                            this.lengths[this.lengths.length-1].get_l2(),
                            this.lengths[this.lengths.length-1].get_l3());

        temp_vertices.add(tri);
        temp_vertices.add(tri);
        return temp_vertices;
    }

    /**
     * Passes the linkage structure through its base interval and samples the
     * position of the terminal point of the linkage.
     * @param num_samples the number of samples to take
     * @param new_A0 the location of the fixed anchor point
     * @return the trace of the linkage
     */
    public ArrayList<tuple> compute_trace(int num_samples, tuple new_A0){
        ArrayList<tuple> trace = new ArrayList<tuple>();
        tuple temp_A1;
        double dx = (this.base_interval.get_y() - this.base_interval.get_x())/num_samples;
        for(int i = 0; i < num_samples; i++){
            temp_A1 = new tuple(new_A0.get_x() - this.base_interval.get_x() - dx*i,
                                   new_A0.get_y());
            update_anchors(new_A0, temp_A1);
            compute_tower_vertices();
            trace.add(getTerminalPoint());
        }
        this.trace = trace;
        return trace;
    }

    //Some simple getters for the above class:
    public ArrayList<tuple> getTower_points(){return this.tower_points;}
    public tuple getBase_interval(){return this.base_interval;}
    public tuple getA0(){return this.A0;}
    public tuple getA1(){return this.A1;}
    public quad[] getLengths(){return this.lengths;}
    public ArrayList<tuple> get_trace(){return this.trace; }

    public tuple getTerminalPoint(){
        if(this.tower_points.size() > 0){ return this.tower_points.get(this.tower_points.size()-1);}
        else{return new tuple();}
    }

}
