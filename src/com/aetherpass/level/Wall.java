package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class Wall {
    public String type;
    public Point[] vertices;

    public Wall(String type, Point[] vertices) {
        this.type = type;
        this.vertices = vertices;
    }

    public void render(Graphics2D g) {

    }

    public Point[][] getPhysicsShapes() {
        return new Point[][]{};
    }

    public Rectangle getBoundingBox() {
        return null;
    }
}
