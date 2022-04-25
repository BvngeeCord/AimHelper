package me.bvngeecord.aimhelper.game;

import me.bvngeecord.aimhelper.render.DisplayManager;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoopManager {

    public static final long TO_NANO = 1000000000L;
    private static final Random random = new Random();
    private static final List<Target> targets = new ArrayList<>();

    private static float SPAWN_INTERVAL = 1f; //seconds
    public static float TARGET_LIFETIME = 3f; //seconds
    private static float TARGET_SIZE = 0.1f;
    private static float TARGET_SPEED = 10f;
    private static long timeSinceLastSpawn;
    private static float xBounds;
    private static float yBounds;

    public static void init(float spawnInterval, float targetLifetime, float targetSize, float targetSpeed) {
        SPAWN_INTERVAL = spawnInterval;
        TARGET_LIFETIME = targetLifetime;
        TARGET_SIZE = targetSize;
        TARGET_SPEED = targetSpeed;
    }

    public static void windowUpdate(float sf) {
        xBounds = 1.0f - (DisplayManager.MARGIN * sf);
        yBounds = 1.0f - DisplayManager.MARGIN;
    }

    public static void tick() {

        if (timeSinceLastSpawn <= System.nanoTime() - (SPAWN_INTERVAL * TO_NANO)) {
            targets.add(new Target(
                    random.nextFloat(-0.7f, 0.7f) * (xBounds*DisplayManager.width),
                    random.nextFloat(-0.7f, 0.7f) * (yBounds*DisplayManager.height),
                    TARGET_SIZE,
                    random.nextFloat(-0.5f, 0.5f) * (TARGET_SPEED * DisplayManager.width / 300),
                    random.nextFloat(-0.5f, 0.5f) * (TARGET_SPEED * DisplayManager.height / 300))
            );
            timeSinceLastSpawn = System.nanoTime();
            Statistics.spawnTarget();
        }

        for (int n=0; n<targets.toArray().length; n++) {
            final boolean lifetime = TARGET_LIFETIME == -1f || System.nanoTime() - targets.get(n).spawnTime <= TARGET_LIFETIME * TO_NANO;
            if (targets.get(n).isInBounds(xBounds, yBounds) && lifetime) {
                targets.get(n).tick();
                targets.get(n).render();
            } else {
                targets.remove(n);
            }
        }
    }

    public static void mouseAction(int button, int action, double x, double y) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
            int hits = 0;
            for (int i=0; i<targets.toArray().length; i++) {
                if (targets.get(i).intersects(x, y)) {
                    targets.remove(i);
                    hits++;
                }
            }
            Statistics.click(hits);
        }
    }

}