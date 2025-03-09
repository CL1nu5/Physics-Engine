package Game;

import PhysicObjects2D.Object2D;
import PhysicObjects2D.Objects.Circle2D;
import PhysicObjects2D.Objects.Polygon2D;
import PhysicObjects2D.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    Dimension size = new Dimension(1000, 800);
    Frame frame;

    ArrayList<Object2D> gameObjects = new ArrayList<>();

    public GamePanel() {
        this.setPreferredSize(size);

        gameObjects.add(new Polygon2D(3, 245, new Vector2D(312,315), new Vector2D(), 1 , 90));
        gameObjects.add(new Circle2D(300, new Vector2D(646,223), new Vector2D()));

        frame = new Frame("Game");
        frame.switchPanel(this,false);

        System.out.println(gameObjects.get(0).checkCollisions(gameObjects.get(1)));

        repaint();
    }

    //paints the gamePanel objects
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        for (Object2D object: gameObjects) {
            object.paint(g2d);
        }
    }
}
