package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class Wall {
    public String type;
    public int[][] vertices;
    // TODO tomorrow when its not 3 am I need to write a custom deserializer so that I can read my shapes correctly

    public Wall(String type, int[][] vertices) {
        this.type = type;
        this.vertices = vertices;
    }

    public void render(Graphics2D g) {

    }
}
