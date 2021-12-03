package linkage_model;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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
                              tuple A0){
        this.A0 = A0;
        this.target_trace = target_trace;
        this.num_generations = 5;
        this.disp_every = 2;
        this.init_pool_size = 30;
        this.num_carry_over = 15;

        this.population = generate_random_pop(init_pool_size);
    }

    public static double l2_norm(tuple A, tuple B){
        return (new tuple(A.get_x() - B.get_x(), A.get_y() - B.get_y())).norm();
    }

    //only used for computing the 'distance' between parametric curves
    //Quick and dirty sub-sampling so that the simulation can be run
    //as quickly as possible, not super accurate.
    private static ArrayList<tuple> sub_sample_trace(ArrayList<tuple> trace, double step_len){
        ArrayList<tuple> sub_trace = new ArrayList<tuple>();
        tuple start = trace.get(0);
        sub_trace.add(start);
        tuple temp;

        //almost O(n^2), but not really!
        int pos = 0;
        while(pos < trace.size()){
            pos+=10;
            temp = trace.get(pos);
            while((l2_norm(temp, start) < step_len)){
                if(pos >= trace.size()){break;}
                temp = trace.get(pos);
                pos+=10;
            }
            start = temp;
            sub_trace.add(start);
        }
        return sub_trace;
    }

    public static double compute_fitness(ArrayList<tuple> target_trace, ArrayList<tuple> temp_trace){
        double step = 100;
        ArrayList<tuple> target_sub_sample = sub_sample_trace(target_trace, step);
        ArrayList<tuple> temp_sub_sample = sub_sample_trace(temp_trace, step);
        int min_size = Math.min(target_sub_sample.size(), temp_sub_sample.size());

        System.out.println(min_size);

        double sum_of_loss = 0;
        for(int i = 0; i < min_size; i++){
            sum_of_loss += l2_norm(target_sub_sample.get(i), temp_sub_sample.get(i));
        }
        return sum_of_loss/min_size;
    }

    private ArrayList<linkage> mutate_linkage(linkage parent, int num_children){
        ArrayList<linkage> children;

        quad[] parent_quads = parent.getLengths();
        
        for(int i = 0; i < num_children; i++){
            quad dq0 = quad.random_quad(2);
            quad dq1 = quad.random_quad(2);
            quad dq2 = quad.random_quad(2);
            quad dq3 = quad.random_quad(2);

        }
    }

    private ArrayList<linkage> generate_random_pop(int pop_size) {
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
                    quad[] new_funky_lengths = {quad0, quad1, quad2, quad4};
                    child  = new linkage(new_funky_lengths);
                    break;
                } catch (IllegalArgumentException iae) {
                }
            }
            new_children.add(child);
        }

        return new_children;
    }

}
