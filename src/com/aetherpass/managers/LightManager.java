package com.aetherpass.managers;

import com.aetherpass.level.Wall;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by Trent on 3/1/2016.
 */
public class LightManager {
    public static int[][] castFlashlight(Point2D.Double sourcePoint, double lightAngle) {
        Rectangle screenRectangle = LevelManager.getScreenInWorld();

        ArrayList<Wall> potentiallyVisibleWallList = new ArrayList<Wall>();
        for (Wall wall : LevelManager.level.walls) {
            Rectangle boundingBox = wall.getBoundingBox();

            if (screenRectangle.intersects(boundingBox)) {
                potentiallyVisibleWallList.add(wall);
            }
        }

        Set<Point> pointSet = new HashSet<Point>();
        for (Wall wall : potentiallyVisibleWallList) {
            for (Point vertex : wall.vertices) {
                pointSet.add(vertex);
            }
        }

        Point[] uniquePointArray = pointSet.toArray(new Point[]{});

        // TODO why do I need 3 angles per point
        double[] angleArray = new double[uniquePointArray.length * 3];
        for (int i = 0; i < uniquePointArray.length; i++) {
            Point point = uniquePointArray[i];

            // TODO why do I need 3 angles per point
            double angle = Math.atan2(point.y - sourcePoint.y, point.x - sourcePoint.x);
            angleArray[i * 3] = angle - 0.00001;
            angleArray[i * 3 + 1] = angle;
            angleArray[i * 3 + 1] = angle + 0.00001;
        }

        // intersectable segments
        ArrayList<Line2D.Double> segmentList = new ArrayList<Line2D.Double>();
        for (Wall wall : potentiallyVisibleWallList) {
            Point[] vertices = wall.vertices;

            for (int index = 0; index < vertices.length; index++) {
                int nextIndex = (index + 1) % vertices.length;
                Point v1 = vertices[index];
                Point v2 = vertices[nextIndex];

                Line2D.Double segment = new Line2D.Double(v1.x, v1.y, v2.x, v2.y);
                segmentList.add(segment);
            }

            // add all the screen window segments with a little padding
            Line2D.Double leftScreenSegment = new Line2D.Double(
                    screenRectangle.x - 1,
                    screenRectangle.y - 1,
                    screenRectangle.x - 1,
                    screenRectangle.y + screenRectangle.height + 1);

            Line2D.Double bottomScreenSegment = new Line2D.Double(
                    screenRectangle.x - 1,
                    screenRectangle.y + screenRectangle.height + 1,
                    screenRectangle.x + screenRectangle.width + 1,
                    screenRectangle.y + screenRectangle.height + 1);

            Line2D.Double rightScreenSegment = new Line2D.Double(
                    screenRectangle.x + screenRectangle.width + 1,
                    screenRectangle.y + screenRectangle.height + 1,
                    screenRectangle.x + screenRectangle.width + 1,
                    screenRectangle.y - 1);

            Line2D.Double topScreenSegment = new Line2D.Double(
                    screenRectangle.x + screenRectangle.width + 1,
                    screenRectangle.y - 1,
                    screenRectangle.x - 1,
                    screenRectangle.y - 1);

            segmentList.add(leftScreenSegment);
            segmentList.add(bottomScreenSegment);
            segmentList.add(rightScreenSegment);
            segmentList.add(topScreenSegment);
        }

        ArrayList<Intersection> intersectionList = new ArrayList<Intersection>();
        for (double angle : angleArray) {
            double dx = Math.cos(angle);
            double dy = Math.sin(angle);

            Line2D.Double ray = new Line2D.Double();
            ray.x1 = sourcePoint.x;
            ray.y1 = sourcePoint.y;
            ray.x2 = sourcePoint.x + dx;
            ray.y2 = sourcePoint.y + dy;

            Intersection closestIntersection = null;
            for (Line2D.Double segment : segmentList) {
                    Intersection currentIntersection = getIntersection(ray, segment);
                    if (currentIntersection == null) {
                        continue;
                    }

                    if (closestIntersection == null || currentIntersection.param < closestIntersection.param) {
                        closestIntersection = currentIntersection;
                    }
            }

            if (closestIntersection == null) {
                continue;
            }

            closestIntersection.angle = angle;

            intersectionList.add(closestIntersection);
        }

        Collections.sort(intersectionList, new Comparator<Intersection>() {
            @Override
            public int compare(Intersection a, Intersection b) {
                return (int) Math.signum(a.angle - b.angle);
            }
        });

        int[] xPoints = new int[intersectionList.size()];
        int[] yPoints = new int[intersectionList.size()];
        for (int i = 0; i < intersectionList.size(); i++) {
            xPoints[i] = (int) intersectionList.get(i).point.x;
            yPoints[i] = (int) intersectionList.get(i).point.y;
        }

        int[][] polygon = {xPoints, yPoints};

        return polygon;
    }

    private static Intersection getIntersection(Line2D.Double ray, Line2D.Double segment) {
        Intersection returnIntersection = new Intersection();

        // RAY in parametric: Point + Delta*T1
        double r_px = ray.x1;
        double r_py = ray.y1;
        double r_dx = ray.x2 - ray.x1;
        double r_dy = ray.y2 - ray.y1;

        // SEGMENT in parametric: Point + Delta*T2
        double s_px = segment.x1;
        double s_py = segment.y1;
        double s_dx = segment.x2 - segment.x1;
        double s_dy = segment.y2 - segment.y1;

        // if they're parallel they don't intersect
        double r_mag = Math.sqrt(r_dx * r_dx + r_dy * r_dy);
        double s_mag = Math.sqrt(s_dx * s_dx + s_dy * s_dy);
        if(r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag){
            // unit vectors are the same
            return null;
        }

        // SOLVE FOR T1 & T2
        // r_px+r_dx*T1 = s_px+s_dx*T2 && r_py+r_dy*T1 = s_py+s_dy*T2
        // ==> T1 = (s_px+s_dx*T2-r_px)/r_dx = (s_py+s_dy*T2-r_py)/r_dy
        // ==> s_px*r_dy + s_dx*T2*r_dy - r_px*r_dy = s_py*r_dx + s_dy*T2*r_dx - r_py*r_dx
        // ==> T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy - s_dy*r_dx)
        double t2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
        double t1 = (s_px + s_dx * t2 - r_px) / r_dx;

        // Must be within parametic whatevers for RAY/SEGMENT
        if(t1 < 0) {
            return null;
        }
        if(t2 < 0 || t2 > 1) {
            return null;
        }

        // Return the POINT OF INTERSECTION
        returnIntersection.point = new Point2D.Double(r_px + r_dx * t1, r_py + r_dy * t1);
        returnIntersection.param = t1;

        return returnIntersection;
    }

    private static class Intersection {
        public Point2D.Double point;
        public double param;
        public double angle;

        public Intersection() {

        }
    }
}
