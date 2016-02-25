package com.aetherpass.editor;

import java.awt.*;


public class Light extends LevelObject {
	public static final int WIDTH = 10;
	
	public Light(int x1, int y1, double angle) {
		super(x1, y1, angle);
		type = LevelEditor.LIGHT;
	}
	
	public boolean contains(int x, int y) {
		if (x >= points.get(0).x - WIDTH / 2 && x <= points.get(0).x + WIDTH / 2) {
			if (y >= points.get(0).y - WIDTH / 2 && y <= points.get(0).y + WIDTH / 2) {
				return true;
			}
		}
		return false;
	}
	
	public void drawEditing(Graphics g, int x, int y) {
		
	}
}
