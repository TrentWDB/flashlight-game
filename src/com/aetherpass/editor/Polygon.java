package com.aetherpass.editor;

import com.aetherpass.engine.Level;
import com.aetherpass.utils.MathUtils;

import java.awt.*;

/**
 * Created by Trent on 2/25/2016.
 */
public class Polygon extends LevelObject {
    private int selectedVertex = -1;

    public Polygon(int x, int y) {
        super(x, y);
        type = LevelEditor.POLYGON;
    }

    public void move(int x, int y) {
        if (selectedVertex == -1) {
            for (Point p : points) {
                p.x += x;
                p.y += y;

                p.x = LevelEditor.snap(p.x);
                p.y = LevelEditor.snap(p.y);
            }
        } else {
            points.get(selectedVertex).x += x;
            points.get(selectedVertex).y += y;

            points.get(selectedVertex).x = LevelEditor.snap(points.get(selectedVertex).x);
            points.get(selectedVertex).y = LevelEditor.snap(points.get(selectedVertex).y);
        }
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
}
