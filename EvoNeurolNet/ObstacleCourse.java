import java.awt.event.*;
import javax.swing.*;
import java.awt.Graphics;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;
import java.util.Scanner;
import java.awt.MouseInfo;
import java.awt.Point;
// import java.io.File;  // Import the File class
// import java.io.FileNotFoundException;

public class ObstacleCourse extends JPanel {
    private Timer t;

    Wall[] walls;          // obstacle
    int num_of_agents = 80;
    Agent[] agents = new Agent[num_of_agents];           // AI players
    Agent most_fit;           // keep track of most fit agent
    Agent prev_most_fit;       
    int goalx = 950;
    int goaly = 350;
    double[][][] most_fit_hidden_layers;    
    double[][] most_fit_output_layer;

    int win_width;      
    int win_height;
    int startx = 100 + 700;
    int starty = 400;
    int timer = 0;
    int generation_count = 0;
    int touching = 0;
    int[] time = {0,0}; // survival time of current most fit and previous most fit
    
    public ObstacleCourse(int w, int h) {
        win_height = h;
        win_width = w;
        
        // walls = new Wall[] {new Wall(150, win_height-600, 400, 15)};
        // make 10 Agents for first generation 
        for (int i = 0; i < num_of_agents; i++)
            agents[i] = new Agent(startx, starty, 10,10);
        prev_most_fit = agents[0];
        most_fit = agents[0];
        t = new Timer(0, new Listener());	   // 0 delay between frames
        t.start(); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // for (Wall wall: walls)
        //     wall.draw(g);

        g.drawString("Gen: " + generation_count, 10, 10);
        g.drawRect(goalx, goaly, 10, 10);
        // draw all agents
        for (Agent agent : agents) {
            agent.draw(g);
        }
    }

    private class Listener implements ActionListener {
       public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
       {
        
        Point p = MouseInfo.getPointerInfo().getLocation();
        goalx = p.x;
        goaly = p.y;
        // System.out.println(goalx + " " + goaly);
        for (int i = 0; i < num_of_agents; i++) {
            // for (Wall wall: walls) {
            //     if (wall.collides(agents[i].x, agents[i].y))
            //         touching = 1;
            // }
            agents[i].perform_action(new double[] {goalx, goaly, agents[i].x, agents[i].y, touching});
            // touching = 0;
        }
        if (timer > 180) {
            for (int i = 0; i < num_of_agents; i++) {
                //System.out.println(agents[i].fitness(goalx, goaly));
                // fitness returns distance from the goal, so smaller value = better fitness
                if (agents[i].fitness(goalx, goaly) < most_fit.fitness(goalx, goaly))
                    most_fit = agents[i];
            }
            if (prev_most_fit.fitness(goalx, goaly) < most_fit.fitness(goalx, goaly)) 
                most_fit = prev_most_fit;
            prev_most_fit = most_fit;

            // Save the most fit neural network 
            // if (generation_count == 20) {
            //     save_network(most_fit.brain.hidden_layers, most_fit.brain.output_layer);
            // }
            // update population with most_fit
            most_fit_hidden_layers = most_fit.hl_copy(most_fit.brain.hidden_layers);  
            most_fit_output_layer = most_fit.deepCopy(most_fit.brain.output_layer);    
            for (int i = 0; i < num_of_agents; i++) {
                agents[i].new_brain(most_fit_hidden_layers, most_fit_output_layer);
                agents[i].x = startx;
                agents[i].y = starty;
            }
            timer = 0;
            generation_count++;
            if (generation_count % 25 == 0) {
                goaly = (int)(Math.random() * 500);
                goalx = (int)(Math.random() * 500) + 500;
            }
        }

        //System.out.println(timer);
        timer++;
        repaint();
       }
    }

    public void save_network(double[][][] hl, double[][] ol) {
        try {
            FileWriter writer = new FileWriter("Brain.txt");
            for (double[][] l : hl) {
                for (double[] n : l) {
                    for (double v: n) {    
                        writer.write(v + ","); 
                    }
                    writer.write("\n");
                }
                writer.write("Layer End\n");
            }
            for (double[] l : ol) {
                for (double v: l) {
                    writer.write(v + ",");
                }
            }
            writer.write("\nLayer End");
            writer.close();
            System.exit(1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // public double[][][] load_network(String name) throws FileNotFoundException{
    //     File file = new File(name);
    //     Scanner reader = new Scanner(file);
    //     while (reader.hasNext("")) {
    //         if ()
    //     }
    // }

}
