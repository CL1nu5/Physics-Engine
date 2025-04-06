package Graphics;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.awt.*;
import java.util.Objects;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
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
        while (!glfwWindowShouldClose(window)) {
            // poll events
            glfwPollEvents();

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(window);
        }
    }
}
