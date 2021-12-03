package linkage_model;
import org.jetbrains.annotations.NotNull;

import java.lang.Math;
import java.util.ArrayList;

public class linkage {
    private tuple A0;
    private tuple A1;
    private tuple base_interval;

    private quad[] lengths;
    private ArrayList<tuple> tower_points;
    private ArrayList<tuple> trace;

    public linkage(quad @NotNull [] lengths) throws IllegalArgumentException{
        if(lengths.length == 0)throw new IllegalArgumentException("Intractible Linkage");
        this.lengths = lengths;
        this.base_interval = linkage_operations.get_t(this.lengths);

        if(this.base_interval == null)throw new IllegalArgumentException("Intractible Linkage");


        this.A0 = new tuple(0.0, 0.0);
        this.A1 = new tuple(1, 0.0);
        this.tower_points = new ArrayList<tuple>();

    }

    public void update_tower_points(tuple A0, tuple A1){
        this.A0 = A0;
        this.A1 = A1;

        ArrayList<tuple> new_verts = compute_tower_vertices();
        if(new_verts!=null){
            this.tower_points = new_verts;
        }
    }

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

    public ArrayList<tuple> compute_trace(int num_samples, tuple new_A0){
        ArrayList<tuple> trace = new ArrayList<tuple>();
        tuple temp_A1;
        double dx = (this.base_interval.get_y() - this.base_interval.get_x())/num_samples;
        for(int i = 0; i < num_samples; i++){
            temp_A1 = new tuple(new_A0.get_x() - this.base_interval.get_x() - dx*i,
                                   new_A0.get_y());
            update_tower_points(new_A0, temp_A1);
            compute_tower_vertices();
            trace.add(getTerminalPoint());
        }
        this.trace = trace;
        return trace;
    }

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
