package Game;

import PhysicObjects2D.CollisionInfo;
import PhysicObjects2D.Object2D;
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

        try {
            gameObjects.add(new Polygon2D(10, 200, new Vector2D(400, 400), new Vector2D(), 1, 0));
            gameObjects.add(new Polygon2D(4, 100, new Vector2D(600, 330), new Vector2D(), 1, 30));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
