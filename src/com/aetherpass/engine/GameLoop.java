package com.aetherpass.engine;

import com.aetherpass.Game;
import com.aetherpass.managers.StateManager;

/**
 * Created by Trent on 2/24/2016.
 */
public class GameLoop implements Runnable {
    private Game game;

    public GameLoop(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {
            GameTime.update();

            StateManager.update(GameTime.getDeltaTime() / 1000.0);
            StateManager.render(game.getGraphics());
            game.flushGraphics();

            int sleepTime = 17 - GameTime.getTimeSinceLastUpdate();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
