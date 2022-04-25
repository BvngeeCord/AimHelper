package me.bvngeecord.aimhelper.game;

import me.bvngeecord.aimhelper.render.DisplayManager;
import me.bvngeecord.aimhelper.render.RenderUtil;

public class Target {

    public float x;
    public float y;
    public float size;
    private final float xMotion;
    private final float yMotion;
    public final long spawnTime;
    private static final float[] TARGET_RGBA = new float[] {0f, 0f, 1f, 1f};

    public Target(float x, float y, float size, float xMotion, float yMotion) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xMotion = xMotion;
        this.yMotion = yMotion;
        this.spawnTime = System.nanoTime();
    }

    public void render() {
        float lifetime = System.nanoTime() - spawnTime;
        float alpha = GameLoopManager.TARGET_LIFETIME - (lifetime / GameLoopManager.TO_NANO);
        System.out.println(alpha);
        RenderUtil.setColor(TARGET_RGBA[0], TARGET_RGBA[1], TARGET_RGBA[2], alpha);
        RenderUtil.rect(x / DisplayManager.width, y / DisplayManager.height, size, size);
        RenderUtil.setColor(RenderUtil.DEFAULT_RGBA);
    }

    public void tick() {
        x += xMotion;
        y += yMotion;
    }

    public boolean isInBounds(float xBounds, float yBounds) {
        final float halfSize = size / 2.0f;
        final float xf = x / (DisplayManager.width);
        final float yf = y / (DisplayManager.height);
        return ((xf + (halfSize * DisplayManager.sf) < xBounds && xf - (halfSize * DisplayManager.sf) > -1*xBounds) && (yf + halfSize < yBounds && yf - halfSize > -1*yBounds));
    }

    public boolean intersects(double mx, double my) {
        final double mxf = (mx - (DisplayManager.width / 2f)) / (DisplayManager.width / 2f);
        final double myf = (my - (DisplayManager.height / 2f)) / (DisplayManager.height / -2f);
        final float xf = x / (DisplayManager.width);
        final float yf = y / (DisplayManager.height);
        final float halfSize = size / 2.0f;
        return ((mxf <= xf + (halfSize * DisplayManager.sf) && mxf >= xf - (halfSize * DisplayManager.sf)) && (myf <= yf + halfSize && myf >= yf - halfSize));
    }

}