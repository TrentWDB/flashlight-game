package com.aetherpass.editor;

import java.awt.Color;
import java.awt.Graphics;


public class Spawn extends LevelObject {
	public static final int WIDTH = 60;
	
	public Spawn(int x1, int y1) {
		super(x1, y1);
		type = LevelEditor.SPAWN;
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
		g.setColor(Color.BLACK);
		g.fillOval(points.get(0).x - WIDTH / 2 + x, points.get(0).y - WIDTH / 2 + y, WIDTH, WIDTH);
		
		g.setColor(Color.WHITE);
		g.drawString("SPAWN", points.get(0).x - 21 + x, points.get(0).y + 5 + y);
	}
}
