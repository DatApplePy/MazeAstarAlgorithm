package MazeAstarAlgorithm;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author where-is-the-semicolon
 */
public class MainFrame extends JFrame{
    private final int FPS = 60;
    private Maze maze;
    private Timer timer;
    
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        maze = new Maze();
        add(maze, BorderLayout.CENTER);
        
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getExtendedKeyCode() == KeyEvent.VK_F5) {
                    System.out.println("F5 pressed");
                    maze.newMaze();
                    timer.restart();
                }
            }
        });
        
        timer = new Timer(1000/FPS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().sync();
                maze.repaint();
                if(maze.switcherState() == 2) {
                    timer.stop();
                    System.out.println("Algorithms finished!");
                }
            }
        });
        
        timer.start();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
