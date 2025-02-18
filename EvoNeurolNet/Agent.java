import java.awt.Graphics;
import java.awt.Color;

public class Agent {
    public int x;
    public int y;
    public int height;
    public int width;
    public double velocity = 0;
    public boolean dead = false;
    public Brain brain;
    private double mutation_rate = 0.5;
    private double mutation_size = 3;

    public Agent(int x, int y, int h, int w) {
        this.x = x;
        this.y = y;
        height = h;
        width = w;
        // 1 hidden layer with 6 neurons and 2 inputs (bird height and height of pipe) and 1 output
        brain = new Brain(1, 8, 1);
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(x,y, width, height);
        g.fillRect(x,y, width, height);
    }

    // insert a new brain copied from the best agent and mutate it
    public void new_brain(double[][][] hl, double[][] ol) {
        // double[][][] new_hl = new double[hl.length][hl[0].length][hl[0][0].length];
        double[][][] new_hl = hl.clone();
        double[][] new_ol = ol.clone(); 

        for (int i = 0; i < new_hl.length; i++) 
            mutate(new_hl[i]);
        mutate(new_ol);
        brain.hidden_layers = new_hl;
        brain.output_layer = new_ol;
    }

    // pre: layer
    // post: randomly mutate the layer
    public void mutate(double[][] layer) {
        for (int row = 0; row < layer.length; row++) {
            if (Math.random() > mutation_rate) {
                for (int col = 0; col < layer[0].length; col++)
                    layer[row][col] += Math.random() * mutation_size - (mutation_size/2);
            }
        }
    }

    // use the neural network to make decisions
    public void perform_action(double input) {
        // TODO:  Use brain
        double action = brain.run(new double[] {y, input})[0];  // 0 = don't flap and 1 = flap 
        System.out.println(action);
        if (action >= 1)
            flap();
    }

    public void flap() {
        velocity = -20;
    }

    public void update() {
        y += velocity;
    }

}
