import linkage_model.linkage_operations;
import linkage_model.quad;
import linkage_model.tuple;
import linkage_model.linkage;
import linkage_model.genetic_simulation;

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

    private ArrayList<tuple> user_trace;
    private ArrayList<button> genetic_algo_ui_buttons;
    private ArrayList<button> interactive_ui_buttons;
    private int mouseX, mouseY;
    private Timer t;
    private int draw_fps = 1;
    private Image background;

    moveable_button a0;
    moveable_button a1;
    anchored_moveable_button ma0;
    anchored_moveable_button ma1;

    genetic_simulation sim;

    //Needed to put the funky linkage code here
    //so that it can be updated outside of the init_window method.
    private linkage funky;
    private tuple funky_T;
    private linkage_painter funky_painter;

    private void init_window(){
        setTitle("Linkage");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.interactive_ui_buttons = new ArrayList<>();
        this.genetic_algo_ui_buttons = new ArrayList<>();

        //loading the background image:
        try{
            background = ImageIO.read(new File(System.getProperty("user.dir")+"/src/white.jpg"));
        } catch (IOException error){
            error.printStackTrace();
        }

        this.a0 = new moveable_button();
        this.a1 = new moveable_button();

        a0.set_position(new tuple(200, 200));
        a1.set_position(new tuple(300, 200));

        toggle_button sim_anchor = new toggle_button("");
        sim_anchor.setPos(new tuple(3*getWidth()/9, 4*getHeight()/5));
        sim_anchor.setSize(new tuple(10, 10));
        genetic_algo_ui_buttons.add(sim_anchor);

        toggle_button interactive_toggle = new toggle_button("Interactive");
        interactive_toggle.setPos(new tuple(10, 50));
        interactive_toggle.setSize(new tuple(100, 30));

        toggle_button trace_toggle = new toggle_button("Toggle Trace");
        trace_toggle.setPos(new tuple(10, 90));
        trace_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(trace_toggle);
        genetic_algo_ui_buttons.add(trace_toggle);

        toggle_button star_toggle = new toggle_button("Star Link");
        star_toggle.setPos(new tuple(10, 130));
        star_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(star_toggle);

        toggle_button scissor_toggle = new toggle_button("Scissor Link");
        scissor_toggle.setPos(new tuple(10, 170));
        scissor_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(scissor_toggle);

        toggle_button funky_toggle = new toggle_button("Funky Link");
        funky_toggle.setPos(new tuple(10, 210));
        funky_toggle.setSize(new tuple(100, 30));
        interactive_ui_buttons.add(funky_toggle);

        toggle_button algo_start = new toggle_button("Begin Genetic Algo.");
        algo_start.setPos(new tuple(10, 130));
        algo_start.setSize(new tuple(150, 30));
        genetic_algo_ui_buttons.add(algo_start);

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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                t.stop();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                a0.pressed(e, mouseX, mouseY);
                a1.pressed(e, mouseX, mouseY);

                interactive_toggle.pressed(e, mouseX, mouseY);

                if(algo_start.getSelected()){
                    sim = new genetic_simulation(user_trace, sim_anchor.getPos());
                }

                if(interactive_toggle.getSelected()) {
                    for (button b : interactive_ui_buttons) {
                        b.pressed(e, mouseX, mouseY);
                    }
                }else {
                    for (button b : genetic_algo_ui_buttons) {
                        b.pressed(e, mouseX, mouseY);
                    }
                }
                if(!funky_toggle.getSelected()){
                    load_funky_linkage();
                }
            }

            public void mouseReleased(MouseEvent e){
                a0.select();
                a1.select();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e){
                mouseX = e.getX();
                mouseY = e.getY();
                a0.dragged(e, mouseX, mouseY);
                a1.dragged(e, mouseX, mouseY);

                if (!interactive_toggle.getSelected() && trace_toggle.getSelected()){
                    user_trace.add(a0.get_position());
                }

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
                mouseX = e.getX();
                mouseY = e.getY();

                if(trace_toggle.getSelected()) {
                    if(scissor_toggle.getSelected()) {
                        user_trace.add(scissor.getTerminalPoint());
                    }
                    if(star_toggle.getSelected()) {
                        user_trace.add(star.getTerminalPoint());
                    }
                }
                if(!trace_toggle.getSelected()) {user_trace = new ArrayList<tuple>();}
            }
        });

        this.t = new Timer(this.draw_fps, e -> {
            Graphics2D g = (Graphics2D) getGraphics();
            g.drawImage(background, 0, 0, getWidth(), getHeight(), Color.WHITE, null);

            interactive_toggle.draw(g, mouseX, mouseY);

            if(interactive_toggle.getSelected()) {

                if (scissor_toggle.getSelected()) {
                    this.ma0 = new anchored_moveable_button(a0, a1, scissor_T);
                    this.ma1 = new anchored_moveable_button(a0, a1, scissor_T);
                    this.ma0.draw(g, mouseX, mouseY);
                    this.ma1.draw(g, mouseX, mouseY);

                    scissor.update_tower_points(a0.get_position(), a1.get_position());
                    scissor_painter.update_linkage(scissor);
                    scissor_painter.paintlinkage(g);
                }
                if (star_toggle.getSelected()) {
                    this.ma0 = new anchored_moveable_button(a0, a1, star_T);
                    this.ma1 = new anchored_moveable_button(a0, a1, star_T);
                    this.ma0.draw(g, mouseX, mouseY);
                    this.ma1.draw(g, mouseX, mouseY);

                    star.update_tower_points(a0.get_position(), a1.get_position());
                    star_painter.update_linkage(star);
                    star_painter.paintlinkage(g);
                }
                if(funky_toggle.getSelected()){
                    this.ma0 = new anchored_moveable_button(a0, a1, funky_T);
                    this.ma1 = new anchored_moveable_button(a0, a1, funky_T);
                    this.ma0.draw(g, mouseX, mouseY);
                    this.ma1.draw(g, mouseX, mouseY);

                    funky.update_tower_points(a0.get_position(), a1.get_position());
                    funky_painter.update_linkage(funky);
                    funky_painter.paintlinkage(g);
                }
                for (button b : interactive_ui_buttons) {b.draw(g, mouseX, mouseY);}

                if (trace_toggle.getSelected()) {
                    tp.update_trace(user_trace);
                    tp.paint_trace(g);
                }

                this.a0.draw(g, mouseX, mouseY);
                this.a1.draw(g, mouseX, mouseY);

            }else {
                for (button b : genetic_algo_ui_buttons) {b.draw(g, mouseX, mouseY);}

                this.a0.draw(g, mouseX, mouseY);
                if (trace_toggle.getSelected()) {
                    tp.update_trace(user_trace);
                    tp.paint_trace(g);
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
