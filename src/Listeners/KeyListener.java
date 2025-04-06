package Listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    /* values */

    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    /* constructor */

    //private constructor for singleton
    private KeyListener() {}

    /* public methods */

    // creates and / or returns the instance
    public static KeyListener getInstance() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    // setts a pressed key
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            getInstance().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            getInstance().keyPressed[key] = false;
        }
    }

    //returns if a key was pressed
    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }
}
