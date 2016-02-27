package com.aetherpass.entities;

import com.aetherpass.Game;
import com.aetherpass.engine.GameInput;
import com.aetherpass.utils.GraphicsUtils;
import com.aetherpass.utils.MathUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Trent on 2/24/2016.
 */
public class Human implements Player {
    private static final int ANIMATION_STATE_FEET_IDLE = 0;
    private static final int ANIMATION_STATE_FEET_WALK_FORWARD = 1;
    private static final int ANIMATION_STATE_FEET_WALK_BACKWARD = 2;
    private static final int ANIMATION_STATE_FEET_RUN_FORWARD = 3;
    private static final int ANIMATION_STATE_FEET_STRAFE_LEFT = 4;
    private static final int ANIMATION_STATE_FEET_STRAFE_RIGHT = 5;
    private int feetAnimationState;
    private double feetAnimationTime;

    private static final int ANIMATION_STATE_PLAYER_IDLE = 0;
    private static final int ANIMATION_STATE_PLAYER_MOVE = 1;
    private int playerAnimationState;
    private double playerAnimationTime;

    private static ArrayList<ArrayList<BufferedImage>> animationFeetImages = new ArrayList<ArrayList<BufferedImage>>();
    private static ArrayList<ArrayList<BufferedImage>> animationFeetNormalImages = new ArrayList<ArrayList<BufferedImage>>();

    private static ArrayList<ArrayList<BufferedImage>> animationPlayerImages = new ArrayList<ArrayList<BufferedImage>>();
    private static ArrayList<ArrayList<BufferedImage>> animationPlayerNormalImages = new ArrayList<ArrayList<BufferedImage>>();

    // TODO this shouldnt be set like this
    private double posX = 800;
    private double posY = 400;
    private double velX;
    private double velY;
    private double angle;

    private double feetAngle;
    private double bodyAngle;

    // this is the closest right angle to the players aim point, either 0, pi / 2, pi, or -pi / 2
    private double closestRightAngle;

    static {
        animationFeetImages.add(ANIMATION_STATE_FEET_IDLE, new ArrayList<BufferedImage>());
        animationFeetImages.add(ANIMATION_STATE_FEET_WALK_FORWARD, new ArrayList<BufferedImage>());
        animationFeetImages.add(ANIMATION_STATE_FEET_WALK_BACKWARD, new ArrayList<BufferedImage>());
        animationFeetImages.add(ANIMATION_STATE_FEET_RUN_FORWARD, new ArrayList<BufferedImage>());
        animationFeetImages.add(ANIMATION_STATE_FEET_STRAFE_LEFT, new ArrayList<BufferedImage>());
        animationFeetImages.add(ANIMATION_STATE_FEET_STRAFE_RIGHT, new ArrayList<BufferedImage>());

        animationPlayerImages.add(ANIMATION_STATE_PLAYER_IDLE, new ArrayList<BufferedImage>());
        animationPlayerImages.add(ANIMATION_STATE_PLAYER_MOVE, new ArrayList<BufferedImage>());

        try {
            loadAnimation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Human() {

    }

    @Override
    public void update(double delta) {
        updateAnimation(delta);

        // set angles
        angle = Math.atan2(velY, velX);
        // calculate body angle
        bodyAngle = Math.atan2(GameInput.mousePos[1] - Game.height / 2, GameInput.mousePos[0] - Game.width / 2);
        // calculate feet angle
        if (feetAnimationState == ANIMATION_STATE_FEET_RUN_FORWARD || feetAnimationState == ANIMATION_STATE_FEET_WALK_FORWARD) {
            // youre moving forward
            feetAngle = Math.atan2(velY, velX);
        } else if (feetAnimationState == ANIMATION_STATE_FEET_WALK_BACKWARD) {
            // youre moving backward
            feetAngle = Math.atan2(velY, velX) + Math.PI;
        } else if (feetAnimationState == ANIMATION_STATE_FEET_STRAFE_RIGHT) {
            // youre moving right
            feetAngle = Math.atan2(velY, velX) - Math.PI / 2;
        } else if (feetAnimationState == ANIMATION_STATE_FEET_STRAFE_LEFT) {
            // youre moving left
            feetAngle = Math.atan2(velY, velX) + Math.PI / 2;
        } else if (feetAnimationState == ANIMATION_STATE_FEET_IDLE) {
            // youre idle
            feetAngle = Math.round(bodyAngle / (Math.PI / 4)) * (Math.PI / 4);
        }
    }

    @Override
    public void render(Graphics2D g) {
        renderPlayer(g);
    }

    private void updateAnimation(double delta) {
        if (velX == 0 && velY == 0) {
            // youre idle
            setFeetAnimationState(ANIMATION_STATE_FEET_IDLE);
            setPlayerAnimationState(ANIMATION_STATE_PLAYER_IDLE);
        } else {
            // youre moving
            double forwardAngle = Math.atan2(GameInput.mousePos[1] - Game.height / 2, GameInput.mousePos[0] - Game.width / 2);
            double backwardAngle = forwardAngle + Math.PI;
            double rightAngle = forwardAngle + Math.PI / 2;
            double leftAngle = forwardAngle - Math.PI / 2;

            double velAngle = Math.atan2(velY, velX);

            double angleFromVelToForward = MathUtils.smallestAngleBetweenAngles(velAngle, forwardAngle);
            double angleFromVelToBackward = MathUtils.smallestAngleBetweenAngles(velAngle, backwardAngle);
            double angleFromVelToRight = MathUtils.smallestAngleBetweenAngles(velAngle, rightAngle);
            double angleFromVelToLeft = MathUtils.smallestAngleBetweenAngles(velAngle, leftAngle);

            if (Math.abs(angleFromVelToForward) < Math.abs(angleFromVelToBackward) &&
                    Math.abs(angleFromVelToForward) < Math.abs(angleFromVelToRight) &&
                    Math.abs(angleFromVelToForward) < Math.abs(angleFromVelToLeft)) {
                // youre walking forward
                setFeetAnimationState(ANIMATION_STATE_FEET_RUN_FORWARD);
                setPlayerAnimationState(ANIMATION_STATE_PLAYER_MOVE);
            }
            if (Math.abs(angleFromVelToBackward) < Math.abs(angleFromVelToForward) &&
                    Math.abs(angleFromVelToBackward) < Math.abs(angleFromVelToRight) &&
                    Math.abs(angleFromVelToBackward) < Math.abs(angleFromVelToLeft)) {
                // youre walking backward
                setFeetAnimationState(ANIMATION_STATE_FEET_WALK_BACKWARD);
                setPlayerAnimationState(ANIMATION_STATE_PLAYER_MOVE);
            }
            if (Math.abs(angleFromVelToRight) < Math.abs(angleFromVelToForward) &&
                    Math.abs(angleFromVelToRight) < Math.abs(angleFromVelToBackward) &&
                    Math.abs(angleFromVelToRight) < Math.abs(angleFromVelToLeft)) {
                // youre walking right
                setFeetAnimationState(ANIMATION_STATE_FEET_STRAFE_RIGHT);
                setPlayerAnimationState(ANIMATION_STATE_PLAYER_MOVE);
            }
            if (Math.abs(angleFromVelToLeft) < Math.abs(angleFromVelToForward) &&
                    Math.abs(angleFromVelToLeft) < Math.abs(angleFromVelToBackward) &&
                    Math.abs(angleFromVelToLeft) < Math.abs(angleFromVelToRight)) {
                // youre walking left
                setFeetAnimationState(ANIMATION_STATE_FEET_STRAFE_LEFT);
                setPlayerAnimationState(ANIMATION_STATE_PLAYER_MOVE);
            }
        }

        feetAnimationTime += delta;
        playerAnimationTime += delta;
    }

    private void renderPlayer(Graphics2D g) {
        BufferedImage feetImage = getAnimationImage(feetAnimationTime, animationFeetImages.get(feetAnimationState));
        int feetWidth = feetImage.getWidth() / 2;
        int feetHeight = feetImage.getHeight() / 2;

        AffineTransform originalFeetTransform = GraphicsUtils.rotateAroundPoint(g, feetAngle, Game.width / 2, Game.height / 2);

        g.drawImage(feetImage, (int) (posX - feetWidth / 2), (int) (posY - feetHeight / 2), feetWidth, feetHeight, null);

        g.setTransform(originalFeetTransform);


        BufferedImage playerImage = getAnimationImage(playerAnimationTime, animationPlayerImages.get(playerAnimationState));
        int playerWidth = playerImage.getWidth() / 2;
        int playerHeight = playerImage.getHeight() / 2;

        AffineTransform originalPlayerTransform = GraphicsUtils.rotateAroundPoint(g, bodyAngle, Game.width / 2, Game.height / 2);

        g.drawImage(playerImage, (int) (posX - playerWidth / 2), (int) (posY - playerHeight / 2), playerWidth, playerHeight, null);

        g.setTransform(originalPlayerTransform);
    }

    private BufferedImage getAnimationImage(double time, ArrayList<BufferedImage> imageList) {
        int frame = (int) (Math.round(time / 0.05) % imageList.size());

        return imageList.get(frame);
    }

    private void setFeetAnimationState(int state) {
        if (feetAnimationState != state) {
            feetAnimationState = state;
            feetAnimationTime = 0;
        }
    }

    private void setPlayerAnimationState(int state) {
        if (playerAnimationState != state) {
            playerAnimationState = state;
            playerAnimationTime = 0;
        }
    }

    private static void loadAnimation() throws IOException {
        // TODO I'm ignoring normal maps for now until I know this animation looks good

        // feet idle animation
        loadAnimationFromDirectory(
                new File("assets/human/feet/idle"),
                animationFeetImages.get(ANIMATION_STATE_FEET_IDLE)
        );

        // feet run forward animation
        loadAnimationFromDirectory(
                new File("assets/human/feet/run"),
                animationFeetImages.get(ANIMATION_STATE_FEET_RUN_FORWARD)
        );

        // feet walk forward animation
        loadAnimationFromDirectory(
                new File("assets/human/feet/walk"),
                animationFeetImages.get(ANIMATION_STATE_FEET_WALK_FORWARD)
        );

        // feet walk backward animation
        for (int i = animationFeetImages.get(ANIMATION_STATE_FEET_WALK_FORWARD).size() - 1; i >= 0; i--) {
            BufferedImage image = animationFeetImages.get(ANIMATION_STATE_FEET_WALK_FORWARD).get(i);
            animationFeetImages.get(ANIMATION_STATE_FEET_WALK_BACKWARD).add(image);
        }

        // feet strafe left animation
        loadAnimationFromDirectory(
                new File("assets/human/feet/strafe-left"),
                animationFeetImages.get(ANIMATION_STATE_FEET_STRAFE_LEFT)
        );

        // feet strafe right animation
        loadAnimationFromDirectory(
                new File("assets/human/feet/strafe-right"),
                animationFeetImages.get(ANIMATION_STATE_FEET_STRAFE_RIGHT)
        );

        // player idle animation
        loadAnimationFromDirectory(
                new File("assets/human/body/idle"),
                animationPlayerImages.get(ANIMATION_STATE_PLAYER_IDLE)
        );

        // player move animation
        loadAnimationFromDirectory(
                new File("assets/human/body/move"),
                animationPlayerImages.get(ANIMATION_STATE_PLAYER_MOVE)
        );
    }

    private static void loadAnimationFromDirectory(File directory, ArrayList<BufferedImage> animationList) throws IOException {
        FilenameFilter filesOnlyFilter = new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return !new File(current, name).isDirectory();
            }
        };

        File[] animationFiles = directory.listFiles(filesOnlyFilter);
        if (animationFiles == null) {
            System.err.println("Animation directory files list " + directory.getAbsolutePath() + " is null.");
            return;
        }

        // pre load into an array since we dont know the size when we pass in the arraylist to this method
        BufferedImage[] animationFrameArray = new BufferedImage[animationFiles.length];

        for (File file : animationFiles) {
            String[] fileNameParts = file.getName().split("[-.]");
            int index = Integer.parseInt(fileNameParts[0]);

            BufferedImage image = ImageIO.read(file);

            animationFrameArray[index] = image;
        }

        animationList.addAll(new ArrayList<>(Arrays.asList(animationFrameArray)));
    }
}
