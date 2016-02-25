package com.aetherpass.managers;

import com.aetherpass.Game;
import com.aetherpass.engine.GameTime;
import com.aetherpass.states.EditorState;
import com.aetherpass.states.GameState;
import com.aetherpass.states.State;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Trent on 2/24/2016.
 */
public class StateManager {
    public static final State[] STATES = {
            new GameState(),
            new EditorState()
    };
    public static final int STATE_GAME = 0;
    public static final int STATE_EDITOR = 1;

    public static State currentState;

    public static void update(double delta) {
        currentState.update(delta);
    }

    public static void render(Graphics2D g) {
        currentState.render(g);
    }

    public static void setState(int state) {
        currentState = STATES[state];
    }
}
