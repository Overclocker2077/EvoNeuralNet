public class Brain {
    public double[][][] hidden_layers;     // this stores the layers; each layer contains neurons which contains weights and a bias
    public double[][] output_layer;        // store output layer in separate array because it's usually smaller
    /*
    { Layer1: { Neuron1:{weight1, weight2, bias}, 
                Neuron2:{weight1,weight2, bias}},  
      Layer2:{Neurons...},
      Layer3:{Neurons...}
    }
      output_of_neuron = activation_function(inputs[0] * weights[0] + inputs[1] * weights[1] + bias)
    */
    // new brain
    public Brain(int numOf_Hidden_Layers, int hidden_layer_size, int output_size) {
        //input_size = in_size;
        hidden_layers = new double[numOf_Hidden_Layers][hidden_layer_size][hidden_layer_size+1];
        output_layer = new double[output_size][hidden_layer_size+1];
        // Set random weights in hidden and output layer
        for (int layer = 0; layer < hidden_layers.length; layer++) {
            for (int i = 0; i < hidden_layers[layer].length; i++) {
                for (int n = 0; n < hidden_layers[layer][i].length; n++) {
                    hidden_layers[layer][i][n] = Math.random()*2 - 1;   // random num from -1 to 1
                }
            }
        }
        for (int n = 0; n < output_layer.length; n++) {
            for (int w = 0; w < output_layer[0].length; w++) {
                output_layer[n][w] = Math.random() * 2 - 1;     // random num from -1 to 1
            }
        }
    }
    // another constructor used for a pre-defined brain
    public Brain(double[][][] hl, double[][] ol) {
        hidden_layers = hl;
        output_layer = ol;
    }

    public double[] run(double[] inputs) {
        double[] hidden_output = compute_hidden_layers(inputs, 0);
        return compute_output_layer(hidden_output);
    }

    // pre: inputs size should equal the num of weights
    // post: return activation_function((w[0] * in[0]) + (w[1] * in[1]) + bias)
    private double[] compute_hidden_layers(double[] inputs, int curr_layer) {
        // terminate at the end of the hidden_layers array
        if (curr_layer == hidden_layers.length) 
            return inputs;
        
        double[] outputs = new double[hidden_layers[curr_layer].length];
        
        // loop through all the neurons
        for (int i = 0; i < hidden_layers[curr_layer].length; i++) {
            // loop through the neurons weights 
            for (int w = 0; w < inputs.length; w++) {
                outputs[i] += hidden_layers[curr_layer][i][w] * inputs[w];
            }
            outputs[i] += hidden_layers[curr_layer][i][hidden_layers[curr_layer][i].length-1]; // add bias
            outputs[i] = activation_function(outputs[i]);
        }
        // output of this layer gets passed as input to the next hidden layer
        return compute_hidden_layers(outputs, curr_layer+1);
    }
    
    // pre: inputs size should equal the num of weights
    // post: return activation_function((w[0] * in[0]) + (w[1] * in[1]) + bias)
    private double[] compute_output_layer(double[] inputs) {
        double[] outputs = new double[output_layer.length];
        for (int i = 0; i < output_layer.length; i++) {
            for (int w = 0; w < inputs.length; w++)
                outputs[i] += inputs[w] * output_layer[i][w];
            outputs[i] += output_layer[i][output_layer[i].length-1]; // add bias
            outputs[i] = activation_function(outputs[i]);
        }
        return outputs;
    }

    // Tanh activation function
    private double activation_function(double input) {
        //Tahn activation function 
        //https://www.datacamp.com/tutorial/introduction-to-activation-functions-in-neural-networks
        // return (Math.exp(input) - Math.exp(-input)) / (Math.exp(input) + Math.exp(-input));
        //return Math.tanh(input);
        return 0.5 * Math.cbrt(input);  // Should be faster then tanh because it doesn't need to compute exponentials 
        // sigmoid activation function 
        // https://www.sciencedirect.com/topics/computer-science/sigmoid-function
        // return (double)(1 / (1 + Math.exp(-input)));
    }
}
