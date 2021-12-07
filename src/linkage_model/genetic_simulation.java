package linkage_model;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import painters.trace_painter;

public class genetic_simulation {
    private ArrayList<tuple> target_trace;
    private ArrayList<linkage> population;
    private tuple A0;
    private int num_generations;
    private int gen_number;
    private int disp_every;
    private int init_pool_size;
    private int num_carry_over;

    public genetic_simulation(ArrayList<tuple> target_trace,
                              tuple A0) throws IllegalArgumentException{
        if(target_trace.size() < 10) throw new IllegalArgumentException("Target Trace Is Too Small");
        this.target_trace = target_trace;
        this.A0 = A0;
        this.num_generations = 10;
        this.init_pool_size = 1000;
        this.num_carry_over = 80;

        this.population = generate_random_pop(init_pool_size);
    }

    public static double l2_norm(tuple A, tuple B){
        return (new tuple(A.get_x() - B.get_x(), A.get_y() - B.get_y())).norm();
    }

    private static int argmin(ArrayList<Double> fitness){
        int min_loc = 0;
        double min = 1000000;
        for(int i = 0; i < fitness.size(); i++){
            double a = fitness.get(i);
            if(a < min){min = a; min_loc = i;}
        }
        return min_loc;
    }

    //only used for computing the 'distance' between parametric curves
    //Quick and dirty sub-sampling so that the simulation can be run
    //as quickly as possible, not super accurate.
    private static ArrayList<tuple> sub_sample_trace(ArrayList<tuple> trace, double step_len){
        ArrayList<tuple> sub_trace = new ArrayList<tuple>();
        tuple start = trace.get(0);
        sub_trace.add(start);
        tuple temp;

        int sample_step = 2;
        //almost O(n^2), but not really!
        int pos = 0;
        while(pos < trace.size()){
            temp = trace.get(pos);
            pos+=sample_step;

            while((l2_norm(temp, start) < step_len)){
                if(pos >= trace.size()){break;}
                temp = trace.get(pos);
                pos+=sample_step;
            }
            start = temp;
            sub_trace.add(start);
        }
        return sub_trace;
    }

    public static double compute_fitness(ArrayList<tuple> target_trace, ArrayList<tuple> temp_trace){
        double step = 2;
        ArrayList<tuple> target_sub_sample = sub_sample_trace(target_trace, step);
        ArrayList<tuple> temp_sub_sample = sub_sample_trace(temp_trace, step);
        int min_size = Math.min(target_sub_sample.size(), temp_sub_sample.size());

        if(min_size < 10){return 1000;}

        double sum_of_loss = 0;
        for(int i = 0; i < min_size; i++){
            sum_of_loss += l2_norm(target_sub_sample.get(i), temp_sub_sample.get(i));
        }
        double l2_loss = sum_of_loss/min_size;
        double endpoint0_l2 = (target_trace.get(0).subtract(temp_trace.get(0))).norm();
        double endpoint1_l2 = (target_trace.get(target_trace.size()-1).
                                subtract(temp_trace.get(temp_trace.size()-1))).norm();
        return (endpoint0_l2 + endpoint1_l2) + l2_loss;
    }

    private ArrayList<linkage> mutate_linkage(linkage parent, int num_children, double dl){
        ArrayList<linkage> children = new ArrayList<linkage>();
        quad[] parent_quads = parent.getLengths();
        quad pq0 = parent_quads[0];
        quad pq1 = parent_quads[1];
        quad pq2 = parent_quads[2];
        quad pq3 = parent_quads[3];
        quad pq4 = parent_quads[4];
        for(int i = 0; i < num_children; i++){
            while(true) {
                try {
                    quad dq0 = pq0.constrained_subtract(quad.random_quad(dl));
                    quad dq1 = pq1.constrained_subtract(quad.random_quad(dl));
                    quad dq2 = pq2.constrained_subtract(quad.random_quad(dl));
                    quad dq3 = pq3.constrained_subtract(quad.random_quad(dl));
                    quad dq4 = pq4.constrained_subtract(quad.random_quad(dl));
                    quad[] child_lens = {dq0, dq1, dq2, dq3, dq4};
                    linkage child = new linkage(child_lens);
                    children.add(child);
                    break;
                }catch(IllegalArgumentException iae){}
            }
        }

        return children;
    }

    public ArrayList<linkage> generate_random_pop(int pop_size) {
        ArrayList<linkage> new_children = new ArrayList<linkage>();

        for (int i = 0; i < pop_size; i++) {
            Random r = new Random();
            int d = 150;
            quad q0 = new quad(d, d, d / 2, d / 2);
            linkage child;

            while (true) {
                try {
                    quad quad0 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                            r.nextDouble() * d, r.nextDouble() * d);
                    quad quad1 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                            r.nextDouble() * d, r.nextDouble() * d);
                    quad quad2 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                            r.nextDouble() * d, r.nextDouble() * d);
                    quad quad4 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                            r.nextDouble() * d, r.nextDouble() * d);
                    quad quad5 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                            r.nextDouble() * d, r.nextDouble() * d);
                    quad[] new_funky_lengths = {quad0, quad1, quad2, quad4, quad5};
                    child  = new linkage(new_funky_lengths);
                    break;
                } catch (IllegalArgumentException iae) {
                }
            }
            new_children.add(child);
        }

        return new_children;
    }

    private ArrayList<linkage> get_best_of(ArrayList<linkage> pop, int n){
        ArrayList<linkage> new_pop = new ArrayList<linkage>();
        ArrayList<Double> fitness = new ArrayList<Double>();

        for(linkage l: pop){
            fitness.add(compute_fitness(l.compute_trace(800, this.A0), this.target_trace));
        }

        for(int i = 0; i < n; i++){
            int max = argmin(fitness);
            new_pop.add(pop.get(max));
            fitness.remove(max);
            pop.remove(max);
        }
        return new_pop;
    }

    public linkage run_simulation(Graphics2D g){
        System.out.println("Running Simulation:");
        System.out.print("Computing fitness for generation 0...");
        ArrayList<linkage> gen = get_best_of(this.population, 100);
        System.out.println("done");

        trace_painter tp;
        for(linkage l: gen){
            tp = new trace_painter(l.get_trace());
            tp.set_color(new Color(255, 153, 153, 100));
            tp.paint_trace(g);
        }

        double t;
        ArrayList<linkage> children;
        this.population = new ArrayList<linkage>();
        for(int i = 0; i < this.num_generations; i++){
            t = (double)(this.num_generations-i)/this.num_generations;

            System.out.print(String.format("Computing fitness for generation %d...", i));
            for(linkage l: gen){
                children = mutate_linkage(l, 5, 3*t*t);
                for(linkage c: children){this.population.add(c);}
            }

            gen = get_best_of(this.population, this.num_carry_over);
            System.out.println("done");

            for(linkage l: gen){
                tp = new trace_painter(l.get_trace());
                tp.set_stroke(i/this.num_generations * 2);
                tp.set_color(new Color((int)(255*t),
                                        (int)(153*t),
                                        (int)(153*t),
                                        50));
                tp.paint_trace(g);
            }
        }

        return gen.get(0);
    }

}
