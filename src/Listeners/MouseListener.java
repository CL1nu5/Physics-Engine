package Listeners;

import PhysicObjects2D.Vector2D;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {

    /* values */

    private static MouseListener instance;
    private Vector2D scrollDis, currentPos, lastPos;
    private boolean[] mouseButtonPressed;
    private boolean isDragging;

    /* constructor */

    private MouseListener() {
        this.scrollDis = new Vector2D();
        this.currentPos = new Vector2D();
        this.lastPos = new Vector2D();
        this.mouseButtonPressed = new boolean[3]; //a mouse with 3 keys
    }

    /* public methods */

    // creates a new instance ore returns the singleton instance
    public static MouseListener getInstance() {
        if (instance == null){
            instance = new MouseListener();
        }

        return instance;
    }

    // sets the new mouse position
    public static void mousePosCallback(long window, double xPos, double yPos) {
        getInstance().lastPos = getInstance().currentPos;
        getInstance().currentPos = new Vector2D(xPos, yPos);
    }

    // sets mouse pressed events
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < getInstance().mouseButtonPressed.length) {
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }

    //sets the scroll distance
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        getInstance().scrollDis = new Vector2D(xOffset, yOffset);
    }

    //end of frame settings
    public static void endFrame() {
        getInstance().scrollDis = new Vector2D();
        getInstance().lastPos = getInstance().currentPos;
    }

    /* getters */

    public static float getX() {
        return (float) getInstance().currentPos.x;
    }

    public static float getY() {
        return (float) getInstance().currentPos.y;
    }

    public static float getDx() {
        return (float)(getInstance().lastPos.x - getInstance().currentPos.x);
    }

    public static float getDy() {
        return (float)(getInstance().lastPos.y - getInstance().currentPos.y);
    }

    public static float getScrollX() {
        return (float) getInstance().scrollDis.x;
    }

    public static float getScrollY() {
        return (float) getInstance().scrollDis.y;
    }

    public static boolean isDragging() {
        return getInstance().isDragging;
    }

    //returns if a mouse button is pressed
    public static boolean mouseButtonDown(int button) {
        if (button < getInstance().mouseButtonPressed.length) {
            return getInstance().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}

