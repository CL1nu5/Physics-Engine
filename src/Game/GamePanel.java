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

        gameObjects.add(new Polygon2D(5, 200, new Vector2D(600, 330), new Vector2D(), 1, 30));
        gameObjects.add(new Polygon2D(3, 150, new Vector2D(340, 300), new Vector2D(), 1, 90));
        gameObjects.add(new Polygon2D(4, 123, new Vector2D(133, 342), new Vector2D(), 1, 0));
        gameObjects.add(new Polygon2D(4, 123, new Vector2D(133, 342), new Vector2D(), 1, 70));

        frame = new Frame("Game");
        frame.switchPanel(this);

        System.out.println(gameObjects.get(0).checkCollisions(gameObjects.get(1)));

        repaint();
    }


    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        for (Object2D object: gameObjects) {
            object.paint(g2d);
        }
    }
}
