package linkage_model;
import java.util.Random;

/**
 * An object representing a quad, which is really just a 4d vector.
 * Could have been implemented using an array of four doubles,
 * I just wanted the implementation to be cleaner:
 */
public class quad {
    private double l0, l1, l2, l3;
    public quad(double l0, double l1, double l2, double l3){
        this.l0 = l0;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }


    /**
     * Substracts this quad object with another, if the result is
     * less than zero we reflect the sign of the result. This function is
     * used only in the genetic algorithm for the purposes of mutating another
     * linkage structure.
     * @param q0 the quad to be subtracted from this quad object
     * @return a new quad object representing the difference
     */
    public quad constrained_subtract(quad q0){
        return new quad(Math.abs(this.l0 - q0.get_l0()),
                        Math.abs(this.l1 - q0.get_l1()),
                        Math.abs(this.l2 - q0.get_l2()),
                        Math.abs(this.l3 - q0.get_l3()));
    }

    /**
     * Returns a quad object whose values are randomized. This function is used primarily
     * in the genetic algorithm for the purposes of mutating another linkage structure
     * @param dl, the scalar which controls the size of the new random quad
     * @return a new randomized quad object.
     */
    public static quad random_quad(double dl){
        Random r = new Random();
        return new quad(r.nextDouble()*dl,
                r.nextDouble()*dl,
                r.nextDouble()*dl,
                r.nextDouble()*dl);
    }

    //getters for the lengths of a quad:
    public double get_l0(){return this.l0;}
    public double get_l1(){return this.l1;}
    public double get_l2(){return this.l2;}
    public double get_l3(){return this.l3;}

}


