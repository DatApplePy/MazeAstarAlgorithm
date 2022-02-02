package MazeAstarAlgorithm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javax.swing.JPanel;

/**
 *
 * @author where-is-the-semicolon
 */
public class Maze extends JPanel{
    private final int WIDTH = 900;  // Both WIDTH and HEIGHT divided by UNIT must be a whole number
    private final int HEIGHT = 900;
    private final int UNIT = 10;
    private final int ROWS = WIDTH/UNIT;
    private final int COLS = HEIGHT/UNIT;
    private final Spot[][] coordinateMatrix;
    private Spot current;
    private Spot goal;
    private Stack<Spot> backTracker;
    private Random neighborChooser;
    private int switcher; // 0 - Maze generate / 1 - A* pathfinder / 2 - Finish
    private ArrayList<Spot> openSet;
    private ArrayList<Spot> closedSet;
    private ArrayList<Spot> solutionPath;
    
    public Maze() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        coordinateMatrix = new Spot[ROWS][COLS];
        newMaze();
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        if(switcher == 0) {
            mazeGenerateStep();
        } else if(switcher == 1) {
            aStarStep();
        }
        
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                coordinateMatrix[i][j].draw(g2d, null);
            }
        }
        
        for(int i = 0; i < closedSet.size(); ++i) {
            closedSet.get(i).draw(g2d, Color.YELLOW);
        }
        
        for(int i = 0; i < openSet.size(); ++i) {
            openSet.get(i).draw(g2d, Color.ORANGE);
        }
        
        for(int i = 0; i < solutionPath.size(); ++i) {
            solutionPath.get(i).draw(g2d, Color.CYAN);
        }
        
        current.draw(g2d, Color.GREEN);
        goal.draw(g2d, Color.RED);
    }
    
    public void newMaze() {
        System.out.println("Generating maze...\n");
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                coordinateMatrix[i][j] = new Spot(i, j, i*UNIT, j*UNIT, UNIT);
            }
        }
        current = coordinateMatrix[0][0];
        goal = coordinateMatrix[ROWS-1][COLS-1];
        current.isVisited = true;
        current.f = heuristic(current, goal);
        backTracker = new Stack<>();
        backTracker.push(current);
        neighborChooser = new Random();
        switcher = 0;
        openSet = new ArrayList<>();
        closedSet = new ArrayList<>();
        openSet.add(coordinateMatrix[0][0]);
        solutionPath = new ArrayList<>();
    }
    
    private Spot checkNeighbors()
    {
        ArrayList<Spot> neighbors = new ArrayList<>();
        
        Spot top = null;
        if(current.gridY-1 >= 0) {
            top = coordinateMatrix[current.gridX][current.gridY-1];
        }
        Spot right = null;
        if(current.gridX+1 < ROWS) {
            right = coordinateMatrix[current.gridX+1][current.gridY];
        }
        Spot bottom = null;
        if(current.gridY+1 < COLS) {
            bottom = coordinateMatrix[current.gridX][current.gridY+1];
        }
        Spot left = null;
        if(current.gridX-1 >= 0) {
            left = coordinateMatrix[current.gridX-1][current.gridY];
        }
        
        // top
        if(top != null && !top.isVisited) {
            neighbors.add(top);
        }
        // right
        if(right != null && !right.isVisited) {
            neighbors.add(right);
        }
        // bottom
        if(bottom != null && !bottom.isVisited) {
            neighbors.add(bottom);
        }
        // left
        if(left != null && !left.isVisited) {
            neighbors.add(left);
        }
        
        if(neighbors.size() > 0) {
            return neighbors.get(neighborChooser.nextInt(neighbors.size()));
        } else {
            return null;
        }
    }
    
    //Euclidien
    private double heuristic(Spot current, Spot end) {
        return Point.distance(current.gridX, current.gridY, end.gridX, end.gridY);
    }
    
    //Manhattan
//    private double heuristic(Spot current, Spot end) {
//        return Math.abs(end.gridX - current.gridX) + Math.abs(end.gridY - current.gridY);
//    }
    
    public int switcherState() {
        return switcher;
    }
    
    private void mazeGenerateStep() {
        Spot next = checkNeighbors();
        if(next != null) {
            next.previous = current;
            current.nextSpotOptions.add(next);
            
//            System.out.println(current + "[" + current.gridX + "][" + current.gridY + "] -> Prev:" + current.previous + " Next:" + current.nextSpotOptions);
            
            int diffX = (current.gridX-next.gridX);
            int diffY = (current.gridY-next.gridY);

            next.isVisited = true;
            if(diffX == 1) {
                current.isWall[3] = false;
                next.isWall[1] = false;
            } else if(diffX == -1) {
                current.isWall[1] = false;
                next.isWall[3] = false;
            } else if (diffY == 1) {
                current.isWall[0] = false;
                next.isWall[2] = false;
            } else if (diffY == -1) {
                current.isWall[2] = false;
                next.isWall[0] = false;
            }
            
            current = next;
            backTracker.push(current);
        } else if (!backTracker.empty()) {
            current = backTracker.pop();
        } else {
            switcher = 1;
            System.out.println("Maze generating done!\n");
            System.out.println("A* start!\n");
        }
    }
    
    private void aStarStep() {
        if(!openSet.isEmpty()) {            
            Spot winner = openSet.get(0);
            for (int i = 1; i < openSet.size(); ++i) {
                if(openSet.get(i).f < winner.f) {
                    winner = openSet.get(i);
                }
            }
            
            if (winner == goal) {
                while(winner != current) {
                    solutionPath.add(winner);
                    winner = winner.previous;
                }
                switcher = 2;
                System.out.println("A* done!");
            }
            
            openSet.remove(winner);
            closedSet.add(winner);
            
            for (int i = 0; i < winner.nextSpotOptions.size(); ++i) {
                Spot neighbor = winner.nextSpotOptions.get(i);
                if (!closedSet.contains(neighbor)) {
                    neighbor.g = winner.g + 1;
                    neighbor.f = neighbor.g + heuristic(neighbor, goal);
                    openSet.add(neighbor);
                }
            }
            
        } else {
            System.out.println("No path to goal!\n");
            switcher = 2;
        }
        
        
        
    }
}
