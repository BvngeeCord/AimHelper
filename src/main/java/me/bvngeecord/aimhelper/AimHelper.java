package me.bvngeecord.aimhelper;

import java.util.Arrays;
import java.util.Scanner;
import me.bvngeecord.aimhelper.game.GameLoopManager;
import me.bvngeecord.aimhelper.render.DisplayManager;

public class AimHelper {

    public static void main(String[] args) {

        init();

    }

    private static void init() {
        Scanner scanner = new Scanner(System.in);
        final boolean fs;
        int[] res;
        final float spawnInterval;
        final float targetLifetime;
        final float targetSize;
        final float targetSpeed;

        System.out.println("Would you like to play in fullscreen? (yes/no)");
        switch (scanner.nextLine().toLowerCase()){
            case "yes" -> fs = true;
            case "no" -> fs = false;
            default -> {
                System.out.println("Invalid syntax. Assuming false");
                fs = false;
            }
        }

        System.out.println("Pick the window width and height ( {width},{height} ), or -1 to have the maximum window size");
        String str = scanner.nextLine();
        if (!str.equals("-1")) {
            res = Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray();
        } else {
            res = new int[] {0, 0};
        }

        System.out.println("Pick the delay in seconds in between each new target spawn (decimals aloud, 0.1 ~ 1.5 recommended):");
        spawnInterval = scanner.nextFloat();

        System.out.println("Pick the maximum lifetime for the targets before they disappear in seconds (decimals aloud, 1 ~ 3 recommended, -1 for no maximum");
        targetLifetime = scanner.nextFloat();

        System.out.println("Pick the size for the targets (decimals aloud, 1 ~ 10 recommended):");
        targetSize = scanner.nextInt() / 10f;

        System.out.println("Pick the speed for the targets (decimals aloud, 5 ~ 50 recommended, 0 for still targets)");
        targetSpeed = scanner.nextFloat();

        try {
            GameLoopManager.init(spawnInterval, targetLifetime, targetSize, targetSpeed);
            DisplayManager.init(fs, res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
