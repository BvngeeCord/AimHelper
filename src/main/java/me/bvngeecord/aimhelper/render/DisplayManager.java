package me.bvngeecord.aimhelper.render;

import me.bvngeecord.aimhelper.game.GameLoopManager;
import me.bvngeecord.aimhelper.game.Statistics;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class DisplayManager {

    public static final float MARGIN = 0.1f;
    private static boolean fullscreen;
    private static long window;
    public static int width;
    public static int height;
    public static float sf;
    private static double mouseX;
    private static double mouseY;


    public static void init(boolean fs, int[] res) {
        fullscreen = fs;
        width = res[0];
        height = res[1];

        DisplayManager.createDisplay();
        GL.createCapabilities();
        renderLoop();
    }

    private static void renderLoop() {
        while (!DisplayManager.isCloseRequested()) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //set the clear color
            DisplayManager.updateDisplay();
        }
        Statistics.printSummary();
        DisplayManager.destroyDisplay();
    }

    public static void createDisplay(){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (width == 0 && height == 0) {
            width = vidMode.width();
            height = vidMode.height();
        }
        sf = (float) height / width;
        GameLoopManager.windowUpdate(sf);

        window = glfwCreateWindow(width, height, "Aim Helper", fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

        setGLFWCallbacks();

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight); // Get the window size passed to glfwCreateWindow
            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2); //center window on screen
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); //enable v-sync
        glfwShowWindow(window);
    }

    private static void setGLFWCallbacks() {
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                glfwSetWindowShouldClose(window, true);
        });
        glfwSetCursorPosCallback(window, (window, xPos, yPos) -> {
            mouseX = xPos;
            mouseY = yPos;
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            GameLoopManager.mouseAction(button, action, mouseX, mouseY);
        });
    }

    private static void updateDisplay(){
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        float halfMargin = MARGIN / 2;

        glColor4f(0.1f, 0.1f, 0.1f, 0.3f);
        RenderUtil.rect(-1 + halfMargin*sf, 0f, MARGIN, 2.0f);
        RenderUtil.rect(1 - halfMargin*sf, 0f, MARGIN, 2.0f);
        RenderUtil.rect(0f, 1 - halfMargin, 10f, MARGIN);
        RenderUtil.rect(0f, -1 + halfMargin, 10f, MARGIN);

        GameLoopManager.tick();

        glfwSwapBuffers(window);
    }

    public static boolean isCloseRequested(){
        return glfwWindowShouldClose(window);
    }

    private static void destroyDisplay(){
        cleanUp();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void cleanUp() {
        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}