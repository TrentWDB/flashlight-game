package com.aetherpass.states;

import com.aetherpass.Game;
import com.aetherpass.managers.PlayerManager;
import com.aetherpass.utils.MathUtils;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class GameState implements State {
    @Override
    public void update(double delta) {
        // update the players
        PlayerManager.updatePlayers(delta);
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.width, Game.height);

        // render the players
        PlayerManager.renderPlayers(g);
    }
}
