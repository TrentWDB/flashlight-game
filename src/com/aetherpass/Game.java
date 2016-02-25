package com.aetherpass;

import com.aetherpass.engine.GameLoop;
import com.aetherpass.managers.StateManager;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class Game {
    public static int DEFAULT_WIDTH = 1600;
    public static int DEFAULT_HEIGHT = 800;
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

        StateManager.setState(StateManager.STATE_GAME);

        resize(width, height);
        new Thread(gameLoop).start();
    }

    public void resize(int width, int height) {
        Game.width = width;
        Game.height = height;

        graphics = mainFrame.resizeGraphics(width, height);
        mainFrame.setSize(new Dimension(width, height));
    }

    public void flushGraphics() {
        mainFrame.repaint();
    }

    public Graphics2D getGraphics() {
        return graphics;
    }
}
