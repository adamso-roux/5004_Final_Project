
import linkage_model.*;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class linkage_model_test {
    private double l = 100;
    private linkage l0;
    private linkage l1;
    private tuple A0;
    private tuple A1;
    private quad q0;
    private genetic_simulation sim;
    ArrayList<tuple> trace1;
    ArrayList<tuple> trace2;

    @Before
    public void init(){
        q0 = new quad(l, l, l, l);

        quad[] lengths0 = {q0,q0,q0};

        l0 = new linkage(lengths0);

        A0 = new tuple(0, 0);
        A1 = new tuple(0, l/2);

        //in order to init a sim I need to provide a trace, so I'll make that here:
        this.trace1 = new ArrayList<tuple>();
        for(int i = 0; i < 1000; i++){
            this.trace1.add(new tuple(i, i));
        }

        this.trace2 = new ArrayList<tuple>();
        for(int i = 0; i < 2000; i++){
            this.trace1.add(new tuple(i, 2*i));
        }

        this.sim = new genetic_simulation(this.trace1, new tuple(0, 0));

    }

    @Test
    public void test_l0(){
        assertEquals(l0.getBase_interval().get_x(), 0, 0.01);
        assertEquals(l0.getBase_interval().get_y(), 2*l, 0.01);
        assertNotNull(l0.getTower_points());
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_l1(){
        //with lengths like this, the linkage should not exist:
        quad[] lengths1 = {q0, new quad(l*3, 0.01, l, l), q0};
        l1 = new linkage(lengths1);
    }

    @Test
    public void test_l1_with_valid_quad(){
        //with quads like this, the base interval should be very small:
        quad[] lengths1 = {q0, new quad(l, 0.001, l, l), q0};
        l1 = new linkage(lengths1);

        assertEquals(l1.getBase_interval().get_x(), l-0.01, 0.01);
        assertEquals(l1.getBase_interval().get_y(), l, 0.01);
        assertNotNull(l1.getTower_points());
    }

    @Test
    public void test_tower_verts_l0(){
        l0.update_anchors(A0, A1);
        assertEquals(l0.getA0().get_x(), A0.get_x(), 0.01);
        assertEquals(l0.getA0().get_y(), A0.get_y(), 0.01);

        assertEquals(l0.getA1().get_x(), A1.get_x(), 0.01);
        assertEquals(l0.getA1().get_y(), A1.get_y(), 0.01);

        l0.getTower_points();
        assertNotNull(l0.getTower_points());

    }

    @Test
    public void test_trace_l0(){
        //making sure we get the correct amount of samples from our get_trace method:
        l0.update_anchors(A0, A1);
        l0.compute_trace(500, A0);
        assertEquals(500, l0.get_trace().size());
    }

    @Test
    public void test_terminal_point_l0(){
        //making sure we get the correct amount of samples from our get_trace method:
        tuple terminal_pt = l0.getTerminalPoint();
        assertNotNull(terminal_pt);
    }

    @Test
    public void test_get_lengths_l1(){
        quad[] lengths1 = {q0, new quad(l, l, l, l), q0};
        l1 = new linkage(lengths1);

        quad Q = l1.getLengths()[1];
        assertEquals(l, Q.get_l0(),0 );
        assertEquals(l, Q.get_l1(),0 );
        assertEquals(l, Q.get_l2(),0 );
        assertEquals(l, Q.get_l3(),0 );
    }


    @Test
    public void test_tower_verts_l1(){
        quad[] lengths1 = {q0, new quad(l, 0.001, l, l), q0};
        l1 = new linkage(lengths1);

        l1.update_anchors(A0, A1);
        l1.getTower_points();
        assertNotNull(l0.getTower_points());
    }


    @Test
    public void test_tuple_A1(){
        assertEquals(A1.get_x(), 0, 0.01);
        assertEquals(A1.get_y(), l/2, 0.01);
        assertEquals(A1.toString(), String.format("[0.00, %.2f]", l/2));
    }

    @Test
    public void test_tuple_new(){
        tuple t = new tuple();
        assertEquals(t.get_x(), 0, 0.01);
        assertEquals(t.get_y(), 0, 0.01);
        assertEquals(t.toString(), "[0.00, 0.00]");
    }

    @Test
    public void test_quad_subtract(){
        quad q0 = new quad(1, 1, 1, 1);
        quad q1 = quad.random_quad(0.5);
        quad diff = q0.constrained_subtract(q1);

        assertEquals(diff.get_l0(), Math.abs(q0.get_l0() - q1.get_l0()), 0);
        assertEquals(diff.get_l1(), Math.abs(q0.get_l1() - q1.get_l1()), 0);
        assertEquals(diff.get_l2(), Math.abs(q0.get_l2() - q1.get_l2()), 0);
        assertEquals(diff.get_l3(), Math.abs(q0.get_l3() - q1.get_l3()), 0);


    }

    @Test
    public void test_sim_basics(){
        assertEquals(genetic_simulation.compute_fitness(this.trace1, this.trace1),
                    0, 0);
    }

    @Test
    public void test_sim_results(){
        l0.compute_trace(600, A0);

        genetic_simulation sim = new genetic_simulation(l0.get_trace(), A0);
        sim.run_simulation(null);
        boolean result = sim.initial_pop_fitness > sim.final_pop_fitness ;
        System.out.println(sim.initial_pop_fitness + " " + sim.final_pop_fitness);

        assertEquals(true, result);
    }

}
