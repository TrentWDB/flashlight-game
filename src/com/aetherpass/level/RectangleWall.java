package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class RectangleWall extends Wall {
    private Point position;
    private Point dimensions;

    public RectangleWall(String type, Point[] vertices) {
        super(type, vertices);

        position = vertices[0];
        dimensions  = new Point(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y);
    }

    public void render(Graphics2D g) {
        g.fillRect(position.x, position.y, dimensions.x, dimensions.y);
    }

    public Point[][] getPhysicsShapes() {
        Point[][] returnShapes = new Point[1][4];

        returnShapes[0] = vertices;

        return returnShapes;
    }
}
