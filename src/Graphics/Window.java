package Graphics;


import Listeners.KeyListener;
import Listeners.MouseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.awt.*;
import java.nio.IntBuffer;
import java.util.Objects;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
public class Window {

    /* values */

    // pointer to the storage location of the window
    private long window;
    // log4j logger:
    private static final Logger logger = LogManager.getLogger(Window.class);
    // title for the frame
    private final String title;
    // display size
    private final Dimension size;

    /* constructor */

    public Window(String title, Dimension size){
        this.title = title;
        this.size = size;
    }

    /* public methods */

    // starts the window
    public void run() {
        logger.info("Starting the frame with title: {} ...", title);

        init();
        loop();

        // free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /* private methods */

    private void init() {
        // set up an error callback to logger; if it fails use standard error output
        try {
            GLFWErrorCallback.create((error, description) -> {
                String errorMessage = GLFWErrorCallback.getDescription(description);
                logger.error("GLFW Error ({}) : {}", error, errorMessage);
            }).set();
        } catch (Exception e) {
            logger.error("Can't add logger to error callback");
            GLFWErrorCallback.createPrint(System.err).set();

        }

        // initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create the window
        window = glfwCreateWindow(this.size.width, this.size.height, this.title, NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        // set listener
        glfwSetCursorPosCallback(window, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(window, KeyListener::keyCallback);

        // make the OpenGL context current
        glfwMakeContextCurrent(window);
        // enable v-sync
        glfwSwapInterval(1);

        // make the window visible
        glfwShowWindow(window);

        // bindings available for use
        GL.createCapabilities();
    }

    private void loop() {
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            // Set the clear color

            // clear the frame buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //swap the color buffers
            glfwSwapBuffers(window);

            // poll events
            glfwPollEvents();
        }
    }
}
