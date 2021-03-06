package com.aetherpass.editor;

import com.aetherpass.utils.MathUtils;
import com.aetherpass.utils.Triangulator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Trent on 2/25/2016.
 */
public class Polygon extends LevelObject {
    public Polygon(int x, int y) {
        super(x, y);
        type = LevelEditor.POLYGON;
        isWall = true;
    }

    public boolean contains(int x, int y) {
        selectedVertex = -1;
        for (int i = 0; i < points.size(); i++) {
            int distX = points.get(i).x - x;
            int distY = points.get(i).y - y;

            if (Math.sqrt(distX * distX + distY * distY) <= LevelEditor.VERTEX_SELECTION_RADIUS) {
                selectedVertex = i;
                break;
            }
        }
        if (selectedVertex != -1) {
            return true;
        }

        double[] point = {x, y};
        double[][] polygon = new double[points.size()][2];
        for (int i = 0; i < points.size(); i++) {
            polygon[i][0] = points.get(i).x;
            polygon[i][1] = points.get(i).y;
        }

        return MathUtils.isPointInsideConcavePolygon(point, polygon);
    }

    public void drawEditing(Graphics g, int x, int y) {
        g.setColor(new Color(100, 0, 0));

        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = points.get(i).x + x;
            yPoints[i] = points.get(i).y + y;
        }

        g.fillPolygon(xPoints, yPoints, points.size());

        g.setColor(new Color(255, 255, 255));

        for (int i = 0; i < points.size(); i++) {
            int nextIndex = i + 1;

            g.fillRect(
                    points.get(i).x + x - LevelEditor.VERTEX_DRAW_RADIUS,
                    points.get(i).y + y - LevelEditor.VERTEX_DRAW_RADIUS,
                    LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1,
                    LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1
            );

            if (i == selectedVertex) {
                g.drawRect(
                        points.get(i).x + x - LevelEditor.VERTEX_DRAW_RADIUS - 3,
                        points.get(i).y + y - LevelEditor.VERTEX_DRAW_RADIUS - 3,
                        LevelEditor.VERTEX_DRAW_RADIUS * 2 + 6,
                        LevelEditor.VERTEX_DRAW_RADIUS * 2 + 6
                );
            }

            if (!LevelEditor.tool.equals("Polygon")) {
                nextIndex %= points.size();
            }
            if (nextIndex >= points.size()) {
                g.drawLine(points.get(i).x + x, points.get(i).y + y, LevelEditor.snap(LevelEditor.mouseX), LevelEditor.snap(LevelEditor.mouseY));
            } else {
                g.drawLine(points.get(i).x + x, points.get(i).y + y, points.get(nextIndex).x + x, points.get(nextIndex).y + y);
            }
        }
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(new Color(100, 0, 0));

        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            int nextIndex = i + 1;

            xPoints[i] = points.get(i).x + x;
            yPoints[i] = points.get(i).y + y;
        }

        g.fillPolygon(xPoints, yPoints, points.size());
    }

    public boolean shouldRemove() {
        return points.size() < 3;
    }

    public boolean removeSelectedVertex() {
        if (points.size() <= 3) {
            return false;
        }

        if (selectedVertex != -1) {
            points.remove(selectedVertex);
            selectedVertex = -1;

            return true;
        }

        return false;
    }

    public JsonObject serialize() {
        ArrayList<Point> pointList = new ArrayList<Point>();

        int value = 0;
        for (int i = 0; i < points.size(); i++) {
            int nextIndex = (i + 1) % points.size();
            Point currentPoint = points.get(i);
            Point nextPoint = points.get(nextIndex);

            value += (nextPoint.x - currentPoint.x) * (nextPoint.y + currentPoint.y);
        }

        if (value > 0) {
            for (Point p : points) {
                pointList.add(p);
            }
        } else if (value < 0) {
            for (int i = points.size() - 1; i >= 0; i--) {
                pointList.add(points.get(i));
            }
        } else {
            System.err.println("Something is very wrong. The area of the concave polygon is zero.");
        }

        Triangulator triangulator = new Triangulator(pointList);
        ArrayList<Integer> earClippingVertices = triangulator.triangulate();

        JsonArray triangleSerializedObjects = new JsonArray();
        ArrayList<Point> currentTrianglePointList = new ArrayList<Point>();
        for (int i = 0; i < earClippingVertices.size(); i++) {
            int earClippingVertexIndex = earClippingVertices.get(i);
            currentTrianglePointList.add(pointList.get(earClippingVertexIndex));

            if ((i + 1) % 3 == 0) {
                triangleSerializedObjects.add(serializeWithPoints(currentTrianglePointList));
                currentTrianglePointList.clear();
            }
        }

        JsonObject polygonSerialized = serializeWithPoints(pointList);
        polygonSerialized.add("triangles", triangleSerializedObjects);

        return polygonSerialized;
    }
}
