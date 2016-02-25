package com.aetherpass.managers;

import com.aetherpass.entities.Human;
import com.aetherpass.entities.Player;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class PlayerManager {
    private static Player yourPlayer = new Human();

    public static void updatePlayers(double delta) {
        yourPlayer.update(delta);
    }

    public static void renderPlayers(Graphics2D g) {
        yourPlayer.render(g);
    }
}
