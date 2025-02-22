import java.awt.Graphics;
import java.awt.Color;

public class Agent {
    public int x;
    public int y;
    public int height;
    public int width;
    public boolean dead = false;
    public Brain brain;
    private double mutation_rate = 0.5;
    private double mutation_size = 3;

    public Agent(int x, int y, int h, int w) {
        this.x = x;
        this.y = y;
        height = h;
        width = w;
        // 1 hidden layer with 8 neurons and 5 inputs (coordinates of finish line (x,y),
        // coordinates of agent (x,y), is touching wall (1 or 0, yes or no)) 
        // and 4 output (move up (0, 1), move down (0,1), move left (0,1), move right (0, 1))
        brain = new Brain(1, 10, 4);
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(x,y, width, height);
        g.fillRect(x,y, width, height);
    }

    // insert a new brain copied from the best agent and mutate it
    public void new_brain(double[][][] hl, double[][] ol) {
        double[][] new_ol = deepCopy(ol); 
        double[][][] new_hl = hl_copy(hl);
        for (int i = 0; i < new_hl.length; i++) 
            mutate(new_hl[i]);               // mutate every layer
        mutate(new_ol);                      // mutate output layer 
        // update brain with new layers
        brain.hidden_layers = new_hl;
        brain.output_layer = new_ol;
    }

    // pre: layer
    // post: randomly mutate the layer
    public void mutate(double[][] layer) {
        for (int row = 0; row < layer.length; row++) {
            if (Math.random() > mutation_rate) {
                for (int col = 0; col < layer[0].length; col++)
                    layer[row][col] += (Math.random() - 0.5) * mutation_size;
            }
        }
    }

    // use the neural network to make decisions based on it's environment
    public void perform_action(double[] inputs) {
        // TODO:  Use brain
        double[] actions = brain.run(inputs);  // left, right, up, down
        // System.out.println(Math.round(actions[0]));
        // System.out.println(Math.round(actions[1]));
        // System.out.println(Math.round(actions[2]));
        // System.out.println(Math.round(actions[3]));
        if (Math.round(actions[0]) == 1 && x >= 10)
            x -= 5;
        if (Math.round(actions[1]) == 1 && Math.round(inputs[4]) != 1 && x < 1000)
            x += 5;
        if (Math.round(actions[2]) == 1 && y < 800)
            y -= 5;
        if (Math.round(actions[3]) == 1 && y >= 10)
            y += 5;
    }

    // post: return fitness of agents
    public double fitness(int goalx, int goaly) {
        // lower numbers = greater fitness because fitness is calculated by measuring the distance  
        return (Math.sqrt((Math.pow(goalx - x, 2) + Math.pow(goaly - y, 2)))); // sqrt(a^2 + b^2)
    }

    // The clone method doesn't clone subarrays so this is needed
    public double[][] deepCopy(double[][] original) {
        double[][] copy = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone(); 
        }
        return copy;
    }

    public double[][][] hl_copy(double[][][] hl) {
        double[][][] new_hl = new double[hl.length][hl[0].length][hl[0][0].length];
        for (int layer = 0; layer < new_hl.length; layer++)
            new_hl[layer] = deepCopy(hl[layer]);
        return new_hl;
    }
    
}
