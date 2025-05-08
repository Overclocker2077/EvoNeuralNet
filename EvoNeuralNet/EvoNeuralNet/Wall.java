import java.awt.Graphics;
import java.awt.Color;


public class Wall {
    
    public int x;
    public int y;
    public int height;
    public int width;

    public Wall(int x, int y, int h, int w) {
        this.x = x;
        this.y = y;
        height = h;
        width = w;
    }

    public boolean collides(int x1, int y1) {
        //WIP not finished with this logic 
        return ((x1 >= x && x1 <= x+width) && (y1 >= y && y1 <= y+height) || 
                (x1 >= x-10) && (y1+10 >= y && y1+10 <= y+height));
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x,y, width, height);
        g.fillRect(x,y, width, height);

    }   
}
