package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class PolygonWall extends Wall {
    public PolygonWall(String type, int[][] vertices) {
        super(type, vertices);
    }

    public void render(Graphics2D g) {
        int[] xPoints = new int[vertices.length];
        int[] yPoints = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            int[] point = vertices[i];
            xPoints[i] = point[0];
            yPoints[i] = point[1];
        }

        g.fillPolygon(xPoints, yPoints, vertices.length);
    }
}
