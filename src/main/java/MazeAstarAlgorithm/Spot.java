package MazeAstarAlgorithm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author where-is-the-semicolon
 */
public class Spot {
    public int gridX;
    public int gridY;
    public int visualX;
    public int visualY;
    public boolean isVisited;
    public boolean[] isWall;
    private int unitSize;
    public Spot previous;
    public ArrayList<Spot> nextSpotOptions;
    public double f;
    public double g;
    public double h;
    
    public Spot(int gridX, int gridY, int visualX, int visualY, int unitSize) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.visualX = visualX;
        this.visualY = visualY;
        this.unitSize = unitSize;
        isVisited = false;
        isWall = new boolean[4];
        for (int i = 0; i < isWall.length; ++i) {
            isWall[i] = true;
        }
        nextSpotOptions = new ArrayList<>();
        f = 0;
        g = 0;
        h = 0;
    }
    
    // isWall
    // 0 - top
    // 1 - right
    // 2 - bottom
    // 3 - left
    
    public void draw(Graphics g, Color color) {
        if(color == null) {
            if(isVisited) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.GRAY);
            }
        } else {
            g.setColor(color);
        }
        
        
        g.fillRect(this.visualX, this.visualY, unitSize, unitSize);
        
        g.setColor(Color.BLACK);
        if(isWall[0]) {
            g.drawLine(this.visualX, this.visualY, this.visualX+unitSize, this.visualY);
        }
        if(isWall[1]) {
            g.drawLine(this.visualX+unitSize, this.visualY, this.visualX+unitSize, this.visualY+unitSize);
        }
        if(isWall[2]) {
            g.drawLine(this.visualX+unitSize, this.visualY+unitSize, this.visualX, this.visualY+unitSize);
        }
        if(isWall[3]) {
            g.drawLine(this.visualX, this.visualY+unitSize, this.visualX, this.visualY);
        }
    }
}
