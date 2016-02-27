package com.aetherpass.states;

import com.aetherpass.Game;
import com.aetherpass.engine.GameTime;
import com.aetherpass.managers.PlayerManager;
import com.aetherpass.utils.MathUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

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
        g.clearRect(0, 0, Game.width, Game.height);

        // render the players
        PlayerManager.renderPlayers(g);

        g.setColor(Color.WHITE);
        g.drawString("" + GameTime.getFPS(), 20, 16);
    }
}
