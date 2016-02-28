package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class RectangleWall extends Wall {
    public RectangleWall(String type, int[][] vertices) {
        super(type, vertices);
    }

    public void render(Graphics2D g) {
        int[] p1 = vertices[0];
        int[] p3 = vertices[2];

        g.fillRect(p1[0], p1[1], p3[0] - p1[0], p3[1] - p1[1]);
    }
}
