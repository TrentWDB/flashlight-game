package com.aetherpass.entities;

import com.aetherpass.engine.GameInput;
import com.aetherpass.engine.GameTime;
import com.aetherpass.engine.Physics;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Trent on 2/24/2016.
 */
public class Player {
    public static final double ACCEL = 6000;
    public static final double FRICTION = ACCEL / 2;
    public static final double MAX_VEL = 500;
    public static final int PLAYER_RADIUS = 30;

    public Body body;

    public double renderX = 0;
    public double renderY = 0;

    public double oldPosX = 0;
    public double oldPosY = 0;
    public double posX = 0;
    public double posY = 0;
    protected double velX;
    protected double velY;
    protected double angle;

    public Player() {
        createBody();
    }

    public void update(double delta) {

    }

    public void render(Graphics2D g) {

    }

    public void updatePhysics(double delta) {
        // update velocity
        updateFriction(delta);
        updateVelocity(delta);

        body.setLinearVelocity(new Vec2((float) (velX / Physics.METERS_TO_PIXELS_SCALE), (float) (velY / Physics.METERS_TO_PIXELS_SCALE)));
    }

    public void finalizePhysics() {
        oldPosX = posX;
        oldPosY = posY;

        Vec2 velocity = body.getLinearVelocity();
        velX = velocity.x * Physics.METERS_TO_PIXELS_SCALE;
        velY = velocity.y * Physics.METERS_TO_PIXELS_SCALE;

        Vec2 position = body.getPosition();
        posX = position.x * Physics.METERS_TO_PIXELS_SCALE;
        posY = position.y * Physics.METERS_TO_PIXELS_SCALE;
    }

    public void interpolate(double alpha) {
        double correctness = 1 - alpha;
        renderX = posX * alpha + oldPosX * correctness;
        renderY = posY * alpha + oldPosY * correctness;
    }

    public void updateFriction(double delta) {
        if (velX == 0 && velY == 0) {
            return;
        }

        double angle = Math.atan2(velY, velX);
        double frictionX = -Math.cos(angle) * FRICTION * delta;
        double frictionY = -Math.sin(angle) * FRICTION * delta;

        if (Math.abs(velX) < Math.abs(frictionX)) {
            velX = 0;
        } else {
            velX += frictionX;
        }
        if (Math.abs(velY) < Math.abs(frictionY)) {
            velY = 0;
        } else {
            velY += frictionY;
        }
    }

    public void updateVelocity(double delta) {
        double initialVel = Math.sqrt(velX * velX + velY * velY);

        int xDir = 0;
        int yDir = 0;
        if (GameInput.keysDown[KeyEvent.VK_A]) {
            xDir -= 1;
        }
        if (GameInput.keysDown[KeyEvent.VK_D]) {
            xDir += 1;
        }
        if (GameInput.keysDown[KeyEvent.VK_W]) {
            yDir -= 1;
        }
        if (GameInput.keysDown[KeyEvent.VK_S]) {
            yDir += 1;
        }

        if (xDir == 0 && yDir == 0) {
            return;
        }

        double accelAngle = Math.atan2(yDir, xDir);
        double xAccel = Math.cos(accelAngle) * ACCEL * delta;
        double yAccel = Math.sin(accelAngle) * ACCEL * delta;

        velX += xAccel;
        velY += yAccel;

        // doing this stuff with max vel so you dont slow down if you get pushed
        double vel = Math.sqrt(velX * velX + velY * velY);
        double maxVel = Math.max(MAX_VEL, initialVel);

        if (vel == 0 || vel <= maxVel) {
            return;
        }

        double scale = maxVel / vel;
        velX *= scale;
        velY *= scale;
    }

    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(new Vec2((float) (posX / Physics.METERS_TO_PIXELS_SCALE), (float) (posY / Physics.METERS_TO_PIXELS_SCALE)));
        bodyDef.type = BodyType.DYNAMIC;

        body = Physics.world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = (float) (PLAYER_RADIUS / Physics.METERS_TO_PIXELS_SCALE); // Diameter of 1m.

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0;
        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
    }
}
