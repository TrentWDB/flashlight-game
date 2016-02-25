package com.aetherpass.states;

import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public interface State {
    public void update(double delta);
    public void render(Graphics2D g);
}
