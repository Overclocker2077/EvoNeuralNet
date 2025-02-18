import java.awt.event.*;
import javax.swing.*;
import java.awt.*;


public class ObstacleCourse extends JPanel {
    private Timer t;

    Wall wall;          // obstacle
    int num_of_agents = 10;
    Agent[] agents = new Agent[num_of_agents];           // AI players
    Agent most_fit;           // keep track of most fit agent
    Agent prev_most_fit;       
    //window dimensions
    int win_width;      
    int win_height;
    int startx = 100;
    int starty = 400;
    int dead_count = 0;
    int generation_count = 0;
    int[] time = {0,0}; // survival time of current most fit and previous most fit
    
    public ObstacleCourse(int w, int h) {
        win_height = h;
        win_width = w;
        wall = new Wall(w, h-500, 600, 40);
        
        // make 10 Agents for first generation 
        for (int i = 0; i < num_of_agents; i++)
            agents[i] = new Agent(startx + (15 * i), starty, 10,10);
        prev_most_fit = agents[0];
        t = new Timer(50, new Listener());	   // 0 delay between frames
        t.start(); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        wall.draw_wall(g);
        g.drawString("Gen: " + generation_count, 10, 10);
        // draw all agents
        for (Agent agent : agents) {
            if (!agent.dead)
                agent.draw(g);
        }
    }

    private class Listener implements ActionListener {
       public void actionPerformed(ActionEvent e)	//this is called for each timer iteration
       { 
        // move wall to the left 
        wall.x -= 20;
        
        // Give agents data on it's environment, so it can make decisions
        for (int i = 0; i < agents.length; i++) {
            agents[i].velocity += 4;  // gravity 
            agents[i].update();
            if (!agents[i].dead) {
                agents[i].perform_action(wall.height);  // give agent the wall height, so it could maneuver above it
                // check if agent dies
                if ((wall.collides(agents[i].x, agents[i].y) || agents[i].y > win_height || agents[i].y < 0)) {
                    agents[i].dead = true;
                    dead_count++;
                } 
                else 
                    most_fit = agents[i];   // eventually the last surviving will be stored in most_fit
            }
        }
        // if all agents are dead; get most_fit and start a new generation
        if (dead_count == num_of_agents) {
            if (time[1] < time[0]) 
                most_fit = prev_most_fit;
            else {
                time[0] = time[1];
                prev_most_fit = most_fit;
            }
            time[1] = 0;
            
            double[][][] hl = most_fit.brain.hidden_layers;
            double[][] ol = most_fit.brain.output_layer;
            for (int i = 0; i < agents.length; i++) {
                agents[i].new_brain(hl, ol);
                agents[i].x = startx + (15 * i);
                agents[i].y = starty;
                agents[i].dead = false;
            }
            dead_count = 0;
            wall.x = win_width;
            generation_count++;
        }
        if (wall.x < 0) {
            wall.x = win_width;
            wall.height = (int)(Math.random() * 800);
            wall.y = win_height - wall.height;
        }
        time[1]++;
        repaint();
       }
    }

}
