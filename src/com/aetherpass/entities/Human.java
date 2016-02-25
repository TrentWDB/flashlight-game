package com.aetherpass.entities;

import javax.imageio.ImageIO;
import java.awt.*;
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
    }

    @Override
    public void render(Graphics2D g) {
        renderPlayer(g);
    }

    private void updateAnimation(double delta) {
        if (velX == 0 && velY == 0) {
            if (feetAnimationState != ANIMATION_STATE_FEET_IDLE) {
                feetAnimationState = ANIMATION_STATE_FEET_IDLE;
                feetAnimationTime = 0;
            }

            if (playerAnimationState != ANIMATION_STATE_PLAYER_IDLE) {
                playerAnimationState = ANIMATION_STATE_PLAYER_IDLE;
                playerAnimationTime = 0;
            }
        }

        feetAnimationTime += delta;
        playerAnimationTime += delta;
    }

    private void renderPlayer(Graphics2D g) {
        BufferedImage feetImage = getAnimationImage(feetAnimationTime, animationFeetImages.get(feetAnimationState));
        int feetWidth = feetImage.getWidth() / 2;
        int feetHeight = feetImage.getHeight() / 2;

        g.drawImage(feetImage, (int) (posX - feetWidth / 2), (int) (posY - feetHeight / 2), feetWidth, feetHeight, null);

        BufferedImage playerImage = getAnimationImage(playerAnimationTime, animationPlayerImages.get(playerAnimationState));
        int playerWidth = playerImage.getWidth() / 2;
        int playerHeight = playerImage.getHeight() / 2;

        g.drawImage(playerImage, (int) (posX - playerWidth / 2), (int) (posY - playerHeight / 2), playerWidth, playerHeight, null);
    }

    private BufferedImage getAnimationImage(double time, ArrayList<BufferedImage> imageList) {
        int frame = (int) (Math.round(time / 0.05) % imageList.size());

        return imageList.get(frame);
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
