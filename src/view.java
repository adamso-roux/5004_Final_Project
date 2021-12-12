import custom_buttons.*;
import linkage_model.*;
import painters.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.util.Random;


public class view extends JFrame {

    //Where we store the trace drawn by the user/linkages
    private ArrayList<tuple> user_trace;
    //An array of buttons for the genetic algorithm page:
    private ArrayList<button> genetic_algo_ui_buttons;
    //An array of buttons for the interactive page:
    private ArrayList<button> interactive_ui_buttons;
    //A tuple representing the location of the mouse on the panel:
    private tuple mouse;
    //The timer object used to draw things over time
    private Timer t;
    //The update time of the timer object
    private int draw_fps = 1;
    //The background of the panel:
    private Image background;

    //The buttons used to draw the linkages:
    private movable_button a0;
    private movable_button a1;
    private anchored_movable_button ma0;
    private anchored_movable_button ma1;

    //The simulation object:
    private genetic_simulation sim;
    boolean sim_done = true;
    private linkage best_linkage;
    private Graphics2D g;

    //Needed to put the funky linkage code here
    //so that it can be updated outside of the init_window method.
    private linkage funky;
    private tuple funky_T;
    private linkage_painter funky_painter;

    private void init_window(){
        //Set up the window and give it a name!
        setTitle("Linkage");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Initialize the arrays which store the ui buttons :)
        this.interactive_ui_buttons = new ArrayList<>();
        this.genetic_algo_ui_buttons = new ArrayList<>();

        //loading the background image:
        try{
            background = ImageIO.read(new File(System.getProperty("user.dir")+"/src/white.jpg"));
        } catch (IOException error){
            error.printStackTrace();
        }

        //Initialize mouse object and the buttons
        this.mouse = new tuple();
        this.a0 = new movable_button();
        this.a1 = new movable_button();

        a0.set_position(new tuple(200, 200));
        a1.set_position(new tuple(300, 200));

        //A button for switching between the genetic algorithm and interactive mode:
        toggle_button interactive_toggle = new toggle_button("Interactive");
        interactive_toggle.setPos(new tuple(10, 50));
        interactive_toggle.setSize(new tuple(100, 30));

        //A button for collecting/dumping the user trace:
        toggle_button trace_toggle = new toggle_button("Toggle Trace");
        trace_toggle.setPos(new tuple(10, 90));
        trace_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(trace_toggle);
        genetic_algo_ui_buttons.add(trace_toggle);

        //A button for toggling the star linkage
        toggle_button star_toggle = new toggle_button("Star Link");
        star_toggle.setPos(new tuple(10, 130));
        star_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(star_toggle);

        //A button for toggling the scissor linkage
        toggle_button scissor_toggle = new toggle_button("Scissor Link");
        scissor_toggle.setPos(new tuple(10, 170));
        scissor_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(scissor_toggle);

        //A button for toggling a random linkage
        toggle_button funky_toggle = new toggle_button("Funky Link");
        funky_toggle.setPos(new tuple(10, 210));
        funky_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(funky_toggle);

        //A button for starting the genetic algorithm
        toggle_button algo_start = new toggle_button("Begin Genetic Algo.");
        algo_start.setPos(new tuple(10, 130));
        algo_start.setSize(new tuple(150, 30));
        genetic_algo_ui_buttons.add(algo_start);

        //The anchor point for the genetic algorithm:
        toggle_button sim_anchor = new toggle_button("");
        sim_anchor.setPos(new tuple(6*getWidth()/9, 4*getHeight()/5));
        sim_anchor.setSize(new tuple(10, 10));
        genetic_algo_ui_buttons.add(sim_anchor);

        user_trace = new ArrayList<tuple>();
        trace_painter tp = new trace_painter(user_trace);

        //INIT THE SCISSOR LINKAGE
        double d = 100;
        quad q0 = new quad(d, d, d/2, d/2);
        quad q1 = new quad(d /2, d /2, d /2, d /2);
        quad[] scissor_lengths = {q0, q1, q1};
        linkage scissor = new linkage(scissor_lengths);
        tuple scissor_T = linkage_operations.get_t(scissor_lengths);
        linkage_painter scissor_painter = new linkage_painter(scissor);

        //INIT THE STAR LINKAGE
        quad q2 = new quad(d/2, d/2.5, d/2.5, d/2);
        quad[] star_lengths = {q0, q2, q2};
        linkage star = new linkage(star_lengths);
        tuple star_T = linkage_operations.get_t(star_lengths);
        linkage_painter star_painter = new linkage_painter(star);

        //INIT A NEW FUNKY ONE
        load_funky_linkage();

        this.g = (Graphics2D) getGraphics();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                t.stop();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouse = new tuple(e.getX(), e.getY());

                a0.pressed(e, mouse); a1.pressed(e, mouse);

                interactive_toggle.pressed(e, mouse);

                if(!algo_start.getSelected()){
                    sim_done = false;
                    try {
                        sim = new genetic_simulation(user_trace, sim_anchor.getPos());
                    }catch(Exception E){}
                }


                //interactive toggle controls which ui we are working with:
                if(interactive_toggle.getSelected()) {
                    for (button b : interactive_ui_buttons) {b.pressed(e, mouse);}
                }else {
                    for (button b : genetic_algo_ui_buttons) {b.pressed(e, mouse);}
                }
                if(!funky_toggle.getSelected()){
                    load_funky_linkage();
                }
            }

            public void mouseReleased(MouseEvent e){
                a0.select();a1.select();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e){
                mouse = new tuple(e.getX(), e.getY());

                a0.dragged(e, mouse);a1.dragged(e, mouse);

                if (!interactive_toggle.getSelected() && trace_toggle.getSelected()){
                    user_trace.add(a0.get_position());
                }

                //if the trace toggle is selected, we store the terminal points of the respective linage objects:
                if(trace_toggle.getSelected()) {
                    if(scissor_toggle.getSelected()) {
                        user_trace.add(scissor.getTerminalPoint());
                    }
                    if(star_toggle.getSelected()) {
                        user_trace.add(star.getTerminalPoint());
                    }
                    if(funky_toggle.getSelected()) {
                        user_trace.add(funky.getTerminalPoint());
                    }
                }
            }
            public void mouseMoved(MouseEvent e){
                mouse = new tuple(e.getX(), e.getY());

                if(trace_toggle.getSelected()) {
                    if(scissor_toggle.getSelected()) {
                        user_trace.add(scissor.getTerminalPoint());
                    }
                    if(star_toggle.getSelected()) {
                        user_trace.add(star.getTerminalPoint());
                    }
                    if(funky_toggle.getSelected()) {
                        user_trace.add(funky.getTerminalPoint());
                    }
                }
                if(!trace_toggle.getSelected()) {user_trace = new ArrayList<tuple>();}
            }
        });

        //THIS IS WHERE THE MAGIC HAPPENS!
        this.t = new Timer(this.draw_fps, e -> {
            this.g = (Graphics2D) getGraphics();
            g.drawImage(background, 0, 0, getWidth(), getHeight(), Color.WHITE, null);

            interactive_toggle.draw(g, mouse);

            if(interactive_toggle.getSelected()) {

                if (scissor_toggle.getSelected()) {
                    this.ma0 = new anchored_movable_button(a0, a1, scissor_T);
                    this.ma1 = new anchored_movable_button(a0, a1, scissor_T);
                    this.ma0.draw(g, mouse);
                    this.ma1.draw(g, mouse);

                    scissor.update_anchors(a0.get_position(), a1.get_position());
                    scissor_painter.update_linkage(scissor);
                    scissor_painter.paint_linkage(g);
                }
                if (star_toggle.getSelected()) {
                    this.ma0 = new anchored_movable_button(a0, a1, star_T);
                    this.ma1 = new anchored_movable_button(a0, a1, star_T);
                    this.ma0.draw(g, mouse);
                    this.ma1.draw(g, mouse);

                    star.update_anchors(a0.get_position(), a1.get_position());
                    star_painter.update_linkage(star);
                    star_painter.paint_linkage(g);
                }
                if(funky_toggle.getSelected()){
                    this.ma0 = new anchored_movable_button(a0, a1, funky_T);
                    this.ma1 = new anchored_movable_button(a0, a1, funky_T);
                    this.ma0.draw(g, mouse);
                    this.ma1.draw(g, mouse);

                    funky.update_anchors(a0.get_position(), a1.get_position());
                    funky_painter.update_linkage(funky);
                    funky_painter.paint_linkage(g);
                }
                for (button b : interactive_ui_buttons) {b.draw(g, mouse);}

                if (trace_toggle.getSelected()) {
                    tp.update_trace(user_trace);
                    tp.paint_trace(g);
                }

                this.a0.draw(g, mouse);
                this.a1.draw(g, mouse);

            }else

            {//where we run the genetic algorithm vis
                for (button b : genetic_algo_ui_buttons) {b.draw(g, mouse);}

                if (trace_toggle.getSelected()) {
                    tp.update_trace(user_trace);
                    tp.paint_trace(g);
                }
                if(algo_start.getSelected() && user_trace.size()>10 && !sim_done){
                    best_linkage = sim.run_simulation(g);
                    sim_done = true;
                }
                this.a0.draw(g, mouse);

                if(this.best_linkage != null){
                    linkage_painter best_lp = new linkage_painter(this.best_linkage);
                    best_lp.paint_linkage(g);
                    trace_painter best_tp = new trace_painter(this.best_linkage.get_trace());
                    best_tp.set_color(new Color(153, 255, 153));
                    best_tp.paint_trace(g);
                }

            }

        });
        this.t.start();
    }

    private void load_funky_linkage(){
        Random r = new Random();
        int d = 100;
        quad q0 = new quad(d, d, d/2, d/2);

        while(true) {
            try {
                quad new_quad0 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                        r.nextDouble() * d, r.nextDouble() * d);
                quad new_quad1 = new quad(r.nextDouble() * d, r.nextDouble() * d,
                        r.nextDouble() * d, r.nextDouble() * d);
                quad[] new_funky_lengths = {q0, new_quad0, new_quad1, new_quad0, new_quad1};
                this.funky = new linkage(new_funky_lengths);
                this.funky_T = linkage_operations.get_t(new_funky_lengths);
                this.funky_painter = new linkage_painter(funky);
                if(Math.abs(funky_T.get_y() - funky_T.get_x()) > d/5){break;}
            }catch(IllegalArgumentException iae){}
        }
    }

    public static void main(String[] args) {

            SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                view v = new view();
                v.init_window();
                v.setVisible(true);
            }
        });
    }
}
