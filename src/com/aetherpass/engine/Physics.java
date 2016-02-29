package com.aetherpass.engine;

import com.aetherpass.level.Level;
import com.aetherpass.level.Wall;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import java.awt.*;

/**
 * Created by Trent on 2/28/2016.
 */
public class Physics {
    public static final double METERS_TO_PIXELS_SCALE = 120.0;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;

    public static World world;

    public static void initialize() {
        Vec2 gravity = new Vec2(0, 0);
        world = new World(gravity);
    }

    public static void loadLevel(Level level) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        Body body = world.createBody(bodyDef);

        for (Wall wall : level.walls) {
            Vec2[] vertices = new Vec2[wall.vertices.length];
            for (int i = 0; i < wall.vertices.length; i++) {
                int[] vertex = wall.vertices[i];
                vertices[i] = new Vec2((float) (vertex[0] / Physics.METERS_TO_PIXELS_SCALE), (float) (vertex[1] / Physics.METERS_TO_PIXELS_SCALE));
            }

            PolygonShape shape = new PolygonShape();
            shape.set(vertices, vertices.length);
            body.createFixture(shape, 0);
        }
    }
}
