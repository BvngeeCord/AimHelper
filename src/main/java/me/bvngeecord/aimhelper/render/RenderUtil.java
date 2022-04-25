package me.bvngeecord.aimhelper.render;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {

    public static final float[] DEFAULT_RGBA = new float[] {0f, 0f, 0f, 0f};

    //Draw a rectangle centered around (x, y) with the given width and height
    public static void rect(float x, float y, float width, float height) {
        glRectf(x - ((width * DisplayManager.sf) / 2), y - (height/2), x + ((width / 2) * DisplayManager.sf), y + (height/2));
    }

    //Sets the color for the next drawing operations based on 4 separate flaot values
    public static void setColor(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
    }

    //Sets the color for the next drawing operations based on a list of 4 float values
    public static void setColor(float[] rgba) {
        glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

}