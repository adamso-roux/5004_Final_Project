package linkage_model;
import java.util.Random;
import java.util.function.Function;

public class quad {
    private double l0, l1, l2, l3;
    public quad(double l0, double l1, double l2, double l3){
        this.l0 = l0;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    //balh
    public double get_l0(){return this.l0;}
    public double get_l1(){return this.l1;}
    public double get_l2(){return this.l2;}
    public double get_l3(){return this.l3;}

    public quad constrained_subtract(quad q0){
        return new quad(Math.abs(this.l0 - q0.get_l0()),
                        Math.abs(this.l1 - q0.get_l1()),
                        Math.abs(this.l2 - q0.get_l2()),
                        Math.abs(this.l3 - q0.get_l3()));
    }

    public static quad random_quad(double dl){
        Random r = new Random();
        return new quad(r.nextDouble()*dl,
                r.nextDouble()*dl,
                r.nextDouble()*dl,
                r.nextDouble()*dl);
    }
}


