package managers;

import engine.GameTime;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class StateManager {
    public static void update(double delta) {
    }

    public static void render(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(400, 200, 800, 400);
    }
}
