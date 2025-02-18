import java.util.*;
import javax.swing.JFrame;

class mainDriver {
    public static void main(String[] args) {
        int w = 1500;
        int h = 800;
        JFrame frame = new JFrame("Window");
        frame.setSize(w, h); // Set the size of the frame
        frame.setVisible(true); // Make the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ObstacleCourse panel = new ObstacleCourse(w, h);
        frame.setContentPane(panel); 
        
    }

}