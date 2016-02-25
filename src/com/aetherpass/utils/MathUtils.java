package com.aetherpass.utils;

/**
 * Created by Trent on 2/24/2016.
 */
public class MathUtils {
    public static final double EPSILON = 0.000001;

    /**
     * Returns the smallest angle to get you from fromAngle to toAngle.
     * @param fromAngle the angle you're going from
     * @param toAngle the angle you're going to
     * @return the smallest angle
     */
    public static double smallestAngleBetweenAngles(double fromAngle, double toAngle) {
        return unsignedModulus(toAngle - fromAngle + Math.PI, Math.PI * 2) - Math.PI;
    }

    /**
     * Determines the closest point projection on the line from the given point. Could be the
     * end points of the line.
     * @param line the 2d line
     * @param point the point that will be projected onto the line
     * #return the closest point on the line
     */
    public static double[] closestPointOnLine(double[][] line, double[] point) {
        double[] returnPoint = new double[3];
        double dx = line[1][0] - line[0][0];
        double dy = line[1][1] - line[0][1];

        double length2 = Math.sqrt(dx * dx + dy * dy);
        if (length2 < 0) {
            returnPoint[0] = line[0][0];
            returnPoint[1] = line[0][1];

            return returnPoint;
        }

        double t = ((point[0] - line[0][0]) * (line[1][0] - line[0][0]) + (point[1] - line[0][1]) * (line[1][1] - line[0][1])) / length2;
        if (t < 0) {
            returnPoint[0] = line[0][0];
            returnPoint[1] = line[0][1];

            return returnPoint;
        }
        if (t > 1) {
            returnPoint[0] = line[1][0];
            returnPoint[1] = line[1][1];

            return returnPoint;
        }

        returnPoint[0] = line[0][0] + t * (line[1][0] - line[0][0]);
        returnPoint[1] = line[0][1] + t * (line[1][1] - line[0][1]);

        return returnPoint;
    }

    /**
     * Returns the mod of a value independent of the dividends sign.
     * @param a the value to be modded
     * @param n the value to act as the modulus
     * @return the sign independent modulus value
     */
    public static double unsignedModulus(double a, double n) {
        return a - Math.floor(a / n) * n;
    }
}
