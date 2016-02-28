package com.aetherpass;

import com.aetherpass.engine.GameLoop;
import com.aetherpass.level.Level;
import com.aetherpass.managers.LevelManager;
import com.aetherpass.managers.StateManager;
import com.aetherpass.states.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Trent on 2/24/2016.
 */
public class Game {
    public static final int DEFAULT_WIDTH = 1600;
    public static final int DEFAULT_HEIGHT = 800;
    public static int width;
    public static int height;

    private MainFrame mainFrame;
    private GameLoop gameLoop;
    private Graphics2D graphics;

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        gameLoop = new GameLoop(this);

        initialize();
    }

    private void initialize() {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;

        LevelManager.loadLevel(new File("level.json"));

        StateManager.setState(StateManager.STATE_GAME);

        resize(width, height);
        new Thread(gameLoop).start();
    }

    public void resize(int width, int height) {
        Game.width = width;
        Game.height = height;

        graphics = mainFrame.resizeGraphics(width, height);

        RenderingHints antiAliasRenderingHint = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHints(antiAliasRenderingHint);

        mainFrame.setSize(new Dimension(width, height));
    }

    public void flushGraphics() {
        mainFrame.repaint();
    }

    public Graphics2D getGraphics() {
        return graphics;
    }
}
