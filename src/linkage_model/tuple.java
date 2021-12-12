package linkage_model;

import java.lang.Math;

/**
 * A simple class representing a two-dimensional point. Used to also
 * represent a real-valued interval
 */
public class tuple {
    private double x;
    private double y;

    /**
     * A two argument constructor for a tuple
     * @param x the x value of the interval/point
     * @param y the y value of the interval/point
     */
    public tuple(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * A no argument constructor for a tuple, defaults to [0, 0]
     */
    public tuple(){this.x = 0; this.y = 0;}

    /**
     * A simple toString override for the purposes of debugging, not used in the view
     * @return a string representing the point/interval
     */
    @Override
    public String toString(){return String.format("[%.2f, %.2f]", this.x, this.y);}

    /**
     * The arithmetic difference between this tuple object and another:
     * @param a, the tuple to subtract from this object
     * @return a new tuple object representing the difference
     */
    public tuple subtract(tuple a){return new tuple(this.x - a.get_x(),this.y - a.get_y());}

    /**
     * Computes the l2 norm of this tuple object
     * @return The l2 norm of this tuple object
     */
    public double norm(){
        return Math.sqrt((this.x*this.x) + (this.y*this.y));
    }

    //Simple getters for the instance variables of this object
    public double get_x(){return this.x;}
    public double get_y(){return this.y;}
}
