package com.aetherpass.level;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.awt.*;
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

        for (Wall wall : walls) {
            wall.render(g);
        }
    }
}
