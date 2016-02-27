package com.aetherpass.editor;

import java.awt.Color;
import java.awt.Graphics;


public class Spawn extends LevelObject {
	public Spawn(int x1, int y1) {
		super(x1, y1);
		type = LevelEditor.SPAWN;
	}
	
	public boolean contains(int x, int y) {
		int dx = points.get(0).x - x;
		int dy = points.get(0).y - y;
		double dist = Math.sqrt(dx * dx + dy * dy);

		return dist <= LevelEditor.GUY_WIDTH / 2;
	}
	
	public void drawEditing(Graphics g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillOval(points.get(0).x - LevelEditor.GUY_WIDTH / 2 + x - 2,
				points.get(0).y - LevelEditor.GUY_WIDTH / 2 + y - 2,
				LevelEditor.GUY_WIDTH + 4,
				LevelEditor.GUY_WIDTH + 4);

		draw(g, x, y);
	}

	public void draw(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillOval(points.get(0).x - LevelEditor.GUY_WIDTH / 2 + x,
				points.get(0).y - LevelEditor.GUY_WIDTH / 2 + y,
				LevelEditor.GUY_WIDTH,
				LevelEditor.GUY_WIDTH);

		g.setColor(Color.WHITE);
		g.drawString("SPAWN", points.get(0).x - 21 + x, points.get(0).y + 5 + y);
	}
}
