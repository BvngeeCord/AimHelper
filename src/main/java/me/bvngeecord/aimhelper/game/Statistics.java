package me.bvngeecord.aimhelper.game;

public class Statistics {

    private static final long startTime = System.nanoTime();
    private static int totalClicks = 0;
    private static int hitClicks = 0;
    private static int totalTargets = 0;

    public static void spawnTarget() {
        totalTargets++;
    }

    public static void click(int hits) {
        totalClicks++;
        hitClicks += hits;
    }

    public static float percentHitClicks() {
        return (float) hitClicks / totalClicks * 100;
    }

    public static float percentTargetsKilled() {
        return (float) hitClicks / totalTargets * 100;
    }

    public static long timePlayed() {
        return (System.nanoTime() - startTime) / GameLoopManager.TO_NANO;
    }

    public static void printSummary() {
        System.out.println(
                "Percent of clicks that were hits: " + percentHitClicks() + "% \n\n" +
                        "Percent of all targets that were killed: " + percentTargetsKilled() + "% \n\n" +
                        "Total time played: " + timePlayed() + " Seconds. \n\n"
        );
    }

}
