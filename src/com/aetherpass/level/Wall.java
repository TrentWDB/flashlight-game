package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class Wall {
    public String type;
    public int[][] vertices;
    public boolean collidable;
    // TODO tomorrow when its not 3 am I need to write a custom deserializer so that I can read my shapes correctly

    public Wall(String type, int[][] vertices, boolean collidable) {
        this.type = type;
        this.vertices = vertices;
        this.collidable = collidable;
    }

    public void render(Graphics2D g) {
        g.setColor(Color.GRAY);

        switch (type) {
            case "rectangle":
                int[] p1 = vertices[0];
                int[] p3 = vertices[2];
                g.fillRect(p1[0], p1[1], p3[0] - p1[0], p3[1] - p1[1]);

            case "polygon":
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
}
