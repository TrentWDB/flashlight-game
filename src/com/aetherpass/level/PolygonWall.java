package com.aetherpass.level;

import java.awt.*;

/**
 * Created by Trent on 2/27/2016.
 */
public class PolygonWall extends Wall {
    private TrianglePart[] triangleParts;

    private int[] xPoints;
    private int[] yPoints;

    public PolygonWall(String type, Point[] vertices, TrianglePart[] triangleParts) {
        super(type, vertices);

        this.triangleParts = triangleParts;

        xPoints = new int[vertices.length];
        yPoints = new int[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            xPoints[i] = vertices[i].x;
            yPoints[i] = vertices[i].y;
        }
    }

    public void render(Graphics2D g) {
        g.fillPolygon(xPoints, yPoints, vertices.length);
    }

    public Point[][] getPhysicsShapes() {
        Point[][] returnShapes = new Point[triangleParts.length][3];

        for (int i = 0; i < triangleParts.length; i++) {
            returnShapes[i][0] = triangleParts[i].vertices[0];
            returnShapes[i][1] = triangleParts[i].vertices[1];
            returnShapes[i][2] = triangleParts[i].vertices[2];
        }

        return returnShapes;
    }
}
