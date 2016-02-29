package com.aetherpass.level;

import com.aetherpass.Game;
import com.aetherpass.managers.PlayerManager;
import com.aetherpass.utils.GraphicsUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Type;

/**
 * Created by Trent on 2/24/2016.
 */
public class Level {
    public Wall[] walls;

    public Level(Wall[] walls) {
        this.walls = walls;
    }

    public void update(double delta) {

    }

    public void render(Graphics2D g) {
        g.setColor(Color.GRAY);

        AffineTransform originalFeetTransform = GraphicsUtils.translate(g, -PlayerManager.yourPlayer.posX + Game.width / 2, -PlayerManager.yourPlayer.posY + Game.height / 2);

        for (Wall wall : walls) {
            wall.render(g);
        }

        g.setTransform(originalFeetTransform);
    }
}
