package com.aetherpass.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Trent on 2/25/2016.
 */
public class GraphicsUtils {
    /**
     * Given the graphics object and a point, this method will translate the graphics transformation [point.x, point.y]
     * amount.
     * @param g the graphics object
     * @param x the x translation amount
     * @param y the y translation amount
     * @return the original AffineTransform to reset when youre done
     */
    public static AffineTransform translate(Graphics2D g, double x, double y) {
        AffineTransform originalTransformation = g.getTransform();

        AffineTransform newTransformation = new AffineTransform();
        newTransformation.translate(x, y);

        g.transform(newTransformation);

        return originalTransformation;
    }

    /**
     * Given the graphics object, an angle, and a point, this method will rotate the graphics transformation around the
     * point theta radians.
     * @param g the graphics object
     * @param theta the amount to rotate in radians
     * @param x the rotate anchor x value
     * @param y the rotate anchor y value
     * @return the original AffineTransform to reset when youre done
     */
    public static AffineTransform rotateAroundPoint(Graphics2D g, double theta, double x, double y) {
        AffineTransform originalTransformation = g.getTransform();

        AffineTransform newTransformation = new AffineTransform();
        newTransformation.rotate(theta, x, y);

        g.transform(newTransformation);

        return originalTransformation;
    }
}
