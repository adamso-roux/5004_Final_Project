package linkage_model;

import java.lang.Math;

public class tuple {
    private double x;
    private double y;

    public tuple(double x, double y){
        this.x = x;
        this.y = y;
    }

    public tuple(){this.x = 0; this.y = 0;}

    @Override
    public String toString(){return String.format("[%.2f, %.2f]", this.x, this.y);}
    public double norm(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public double get_x(){return this.x;}
    public double get_y(){return this.y;}
}
