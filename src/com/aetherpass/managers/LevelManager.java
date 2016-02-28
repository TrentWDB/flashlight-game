package com.aetherpass.managers;

import com.aetherpass.level.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Trent on 2/27/2016.
 */
public class LevelManager {
    public static Level level;

    public static void loadLevel(File file) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Wall.class, new WallDeserializer());
            level = gsonBuilder.create().fromJson(new FileReader(file), Level.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
