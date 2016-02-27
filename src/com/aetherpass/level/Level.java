package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class Level {
    public Wall[] walls;

    public Level(Wall[] walls) {
        this.walls = walls;
    }

    public void update(double delta) {

    }

    public void render(Graphics2D g) {
        for (Wall wall : walls) {
            wall.render(g);
        }
    }
}
