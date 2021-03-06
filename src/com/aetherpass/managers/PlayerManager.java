package com.aetherpass.managers;

import com.aetherpass.entities.Human;
import com.aetherpass.entities.Player;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class PlayerManager {
    public static Player yourPlayer;

    public static void initialize() {
        yourPlayer = new Human();
    }

    public static void update(double delta) {
        yourPlayer.update(delta);
    }

    public static void renderPlayers(Graphics2D g) {
        yourPlayer.render(g);
    }

    public static void interpolate(double alpha) {
        yourPlayer.interpolate(alpha);
    }

    public static void finalizePhysics() {
        yourPlayer.finalizePhysics();
    }
}
