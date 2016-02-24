package engine;

/**
 * Created by Trent on 2/24/2016.
 */
public class GameTime {
    private static long currentTime;
    private static long lastTime;

    private static long lastUpdateTime;
    private static int framesSinceLastUpdate;

    private static int fps;

    static {
        currentTime = System.currentTimeMillis();
        lastTime = currentTime;
    }

    public static void update() {
        lastTime = currentTime;
        currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdateTime >= 1000) {
            fps = framesSinceLastUpdate;
            lastUpdateTime = currentTime;
            framesSinceLastUpdate = 0;
        }

        framesSinceLastUpdate++;
    }

    public static int getFPS() {
        return fps;
    }

    public static int getDeltaTime() {
        return (int) (currentTime - lastTime);
    }

    public static int getTimeSinceLastUpdate() {
        return (int) (System.currentTimeMillis() - currentTime);
    }
}
