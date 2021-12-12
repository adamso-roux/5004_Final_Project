package linkage_model;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import painters.trace_painter;

/**
 * A class which creates a population of linkage structures whose traces begin
 * to approximate a target trace over time. The trace is defined as a list of tuples,
 * and the measure of fitness is roughly defined as the l2 measure between two traces.
 */
public class genetic_simulation {
    private ArrayList<tuple> target_trace;
    private ArrayList<linkage> population;
    private tuple A0;
    private int num_generations;
    private int init_pool_size;
    private int num_carry_over;

    //these variables only exist for demonstrating the algorithm works with regards to testing.
    public double initial_pop_fitness;
    public double final_pop_fitness;

    /**
     * Initializes a genetic simulation:
     * @param target_trace, the trace used in the fitness function.
     * @param A0, the anchor point for the population of linkages to share, establishes geometry
     * @throws IllegalArgumentException, in the event that the target trace is insufficient for training
     */
    public genetic_simulation(ArrayList<tuple> target_trace,
                              tuple A0) throws IllegalArgumentException{
        if(target_trace.size() < 10) throw new IllegalArgumentException("Target Trace Is Too Small");
        this.target_trace = target_trace;
        this.A0 = A0;
        this.num_generations = 10;
        this.init_pool_size = 1000;
        this.num_carry_over = 80;

        //creates a random population of linkage structures
        this.population = generate_random_pop(init_pool_size);
    }

    public static double l2_norm(tuple A, tuple B){
        return (new tuple(A.get_x() - B.get_x(), A.get_y() - B.get_y())).norm();
    }

    /**
     * A simple argmin function, returns the location (Argument) of the minimum within
     * the provided list of doubles representing fitness.
     * @param fitness an array of doubles containing the fitness of a given population.
     * @return the location/index of the minimum.
     */
    private static int argmin(ArrayList<Double> fitness){
        int min_loc = 0;
        double min = 1000000;
        for(int i = 0; i < fitness.size(); i++){
            double a = fitness.get(i);
            if(a < min){min = a; min_loc = i;}
        }
        return min_loc;
    }

    /**
     * Reparameterizes a trace so that points are equally distributed by distance, and decreases
     * the number of points needed to represent a trace so that fitness can be more efficiently estimated.
     * @param trace the list of tuples representing a curve in space.
     * @param step_len the sub-sampling factor, controlling the distance between sub-sequent samples.
     * @return a sub-sampled trace.
     */
    private static ArrayList<tuple> sub_sample_trace(ArrayList<tuple> trace, double step_len){
        //only used for computing the 'distance' between parametric curves
        //Quick and dirty sub-sampling so that the simulation can be run
        //as quickly as possible, not super accurate.
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
        sub_trace.add(trace.get(trace.size()-1));
        return sub_trace;
    }

    /**
     * Compares two traces and measures the 'distance' between them. The lower the fitness, the closer
     * the traces are to each other in space. We sub-sample each trace based on the step size.
     * @param target_trace the trace from which fitness is measured.
     * @param temp_trace the trace whose fitness is being evaluated
     * @return the fitness of the temp trace relative to the target trace.
     */
    public static double compute_fitness(ArrayList<tuple> target_trace, ArrayList<tuple> temp_trace){
        double step = 2;
        ArrayList<tuple> target_sub_sample = sub_sample_trace(target_trace, step);
        ArrayList<tuple> temp_sub_sample = sub_sample_trace(temp_trace, step);
        int min_size = Math.min(target_sub_sample.size(), temp_sub_sample.size());

        if(min_size < 5){return 100000;}

        double sum_of_loss = 0;
        for(int i = 0; i < min_size; i++){
            sum_of_loss = sum_of_loss + l2_norm(target_sub_sample.get(i), temp_sub_sample.get(i));
        }
        //System.out.print(sum_of_loss + ", ");
        double l2_loss = sum_of_loss/min_size;
        //System.out.print(l2_loss + ", ");

        double endpoint0_l2 = (target_trace.get(0).subtract(temp_trace.get(0))).norm();
        //System.out.print(endpoint0_l2 + ", ");

        double endpoint1_l2 = (target_trace.get(target_trace.size()-1).
                                subtract(temp_trace.get(temp_trace.size()-1))).norm();
        //System.out.println(endpoint1_l2 );

        double fitness = (endpoint0_l2 + endpoint1_l2) + l2_loss;
        //System.out.println(fitness);
        return fitness;
    }

    /**
     * Creates a small population of 'children' which are mutated from the parent based on
     * the dl randomization factor.
     * @param parent the parent linkage from which children are created from.
     * @param num_children the amount of children to be created from the parent.
     * @param dl the randomization factor, each length in the parent trace is changed by dl.
     * @return a list of children generated from the parent.
     */
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

    /**
     * Creates a randomized population of linkages. Before being added to the list, each linkage
     * is checked to make sure it is tractable.
     * @param pop_size, the amount of random linkages to be created.
     * @return a list of random linkages.
     */
    public ArrayList<linkage> generate_random_pop(int pop_size) {
        ArrayList<linkage> new_children = new ArrayList<linkage>();

        for (int i = 0; i < pop_size; i++) {
            Random r = new Random();
            int d = 100;
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

    /**
     * From a list of linkages, this function returns the n top linkages based on their trace.
     * @param pop, the population of linkages to select from.
     * @param n, the amount of linkages which are carried over.
     * @return the best performing linkages of a given population.
     */
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

    /**
     * Runs the genetic simulation, accumulating all the above methods:
     * @param g, the graphics object which is used to display the simulation
     *           over time.
     * @return the best linkage and associated trace according to the simulation.
     */
    public linkage run_simulation(Graphics2D g){
        System.out.println("Running Simulation:");
        System.out.print("Computing fitness for generation 0...");
        ArrayList<linkage> gen = get_best_of(this.population, 100);
        System.out.println("done");

        //computing the average fitness for the first population:
        this.initial_pop_fitness = compute_fitness(this.target_trace, gen.get(0).get_trace());
        this.initial_pop_fitness = this.initial_pop_fitness/gen.size();

        trace_painter tp;

        //drawing the traces if we have passed a graphics object
        if (g != null) {
            for (linkage l : gen) {
                tp = new trace_painter(l.get_trace());
                tp.set_color(new Color(255, 153, 153, 100));
                tp.paint_trace(g);
            }
        }

        double t;
        ArrayList<linkage> children;
        this.population = new ArrayList<linkage>();
        for(int i = 0; i < this.num_generations; i++){
            t = (double)(this.num_generations-i)/this.num_generations;

            //for each generation, we mutate all of the linkages
            System.out.print(String.format("Computing fitness for generation %d...", i));
            for(linkage l: gen){
                children = mutate_linkage(l, 5, 3*t*t);
                for(linkage c: children){this.population.add(c);}
            }

            //compute the best of each generation
            gen = get_best_of(this.population, this.num_carry_over);
            System.out.println("done");

            if (g != null) {
                for (linkage l : gen) {
                    tp = new trace_painter(l.get_trace());
                    tp.set_stroke(i / this.num_generations * 2);
                    tp.set_color(new Color((int) (255 * t),
                            (int) (153 * t),
                            (int) (153 * t),
                            50));
                    tp.paint_trace(g);
                }
            }
        }


        this.final_pop_fitness = compute_fitness(this.target_trace, gen.get(0).get_trace());
        //return the most 'fit' linkage
        return gen.get(0);
    }

}
