package com.aetherpass.engine;

import com.aetherpass.Game;
import com.aetherpass.managers.PlayerManager;
import com.aetherpass.managers.StateManager;

/**
 * Created by Trent on 2/24/2016.
 */
public class GameLoop implements Runnable {
    private static final double UPDATE_TIME = 0.01;

    private double accumulator;

    private Game game;

    public GameLoop(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {
            accumulator += GameTime.getTimeSinceLastUpdate() / 1000.0;

            GameTime.update();

            while (accumulator >= UPDATE_TIME) {
                StateManager.update(UPDATE_TIME);

                accumulator -= UPDATE_TIME;
            }

            double alpha = accumulator / UPDATE_TIME;

            PlayerManager.interpolate(alpha);
            StateManager.render(game.getGraphics());
            game.flushGraphics();

            GameInput.clearDeltas();

            int sleepTime = Math.max(17 - GameTime.getTimeSinceLastUpdate(), 2);

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
