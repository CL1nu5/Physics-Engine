package Game;

import PhysicObjects2D.Object2D;
import PhysicObjects2D.Objects.Circle2D;
import PhysicObjects2D.Objects.Polygon2D;
import PhysicObjects2D.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements MouseListener {

    Dimension size = new Dimension(1000, 800);
    Frame frame;

    ArrayList<Object2D> gameObjects = new ArrayList<>();

    public GamePanel() {
        this.setPreferredSize(size);

        gameObjects.add(new Polygon2D(3, 250, new Vector2D(312,315), new Vector2D(), 1 , 0));
        gameObjects.add(new Circle2D(185, new Vector2D(626,233), new Vector2D()));

        frame = new Frame("Game");
        frame.switchPanel(this,false);

        System.out.println(gameObjects.get(0).checkCollisions(gameObjects.get(1)));

        this.addMouseListener(this);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Vector2D pos = new Vector2D(e.getPoint());
        System.out.println(gameObjects.get(1).contains(pos));
        System.out.println(pos);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
