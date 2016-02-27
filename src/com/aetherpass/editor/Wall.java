package com.aetherpass.editor;

import java.awt.*;


public class Wall extends Polygon {
	public Wall(int x1, int y1) {
		super(x1, y1);
		type = LevelEditor.WALL;
	}
	
	public boolean contains(int x, int y) {
		selectedVertex = -1;
		for (int i = 0; i < points.size(); i++) {
			int distX = points.get(i).x - x;
			int distY = points.get(i).y - y;

			if (Math.sqrt(distX * distX + distY * distY) <= LevelEditor.VERTEX_SELECTION_RADIUS) {
				selectedVertex = i;
				break;
			}
		}
		if (selectedVertex != -1) {
			return true;
		}

		return x >= Math.min(points.get(0).x, points.get(1).x) && x < Math.max(points.get(0).x, points.get(1).x) &&
				y >= Math.min(points.get(0).y, points.get(1).y) && y < Math.max(points.get(0).y, points.get(1).y);
	}

	public void drawEditing(Graphics g, int x, int y) {
		g.setColor(new Color(100, 0, 0));

		int x1 = points.get(0).x;
		int y1 = points.get(0).y;

		int width = LevelEditor.snap(LevelEditor.posX + LevelEditor.mouseX) - x1;
		int height = LevelEditor.snap(LevelEditor.posY + LevelEditor.mouseY) - y1;

		if (!LevelEditor.tool.equals("Wall")) {
			width = points.get(1).x - points.get(0).x;
			height = points.get(1).y - points.get(0).y;
		}

		if (width < 0) {
			width = -width;
			x1 -= width;
		}
		if (height < 0) {
			height = -height;
			y1 -= height;
		}

		g.fillRect(x1 + x, y1 + y, width, height);

		int x2 = x1 + width;
		int y2 = y1 + height;

		g.setColor(new Color(255, 255, 255));

		// corners
		g.fillRect(
				x1 + x - LevelEditor.VERTEX_DRAW_RADIUS,
				y1 + y - LevelEditor.VERTEX_DRAW_RADIUS,
				LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1,
				LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1);
		g.fillRect(
				x2 + x - LevelEditor.VERTEX_DRAW_RADIUS,
				y2 + y - LevelEditor.VERTEX_DRAW_RADIUS,
				LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1,
				LevelEditor.VERTEX_DRAW_RADIUS * 2 + 1);

		if (selectedVertex != -1) {
			g.drawRect(
					points.get(selectedVertex).x + x - LevelEditor.VERTEX_DRAW_RADIUS - 3,
					points.get(selectedVertex).y + y - LevelEditor.VERTEX_DRAW_RADIUS - 3,
					LevelEditor.VERTEX_DRAW_RADIUS * 2 + 6,
					LevelEditor.VERTEX_DRAW_RADIUS * 2 + 6
			);
		}
	}

	public void draw(Graphics g, int x, int y) {
		g.setColor(new Color(100, 0, 0));

		g.fillRect(points.get(0).x + x, points.get(0).y + y, points.get(1).x - points.get(0).x, points.get(1).y - points.get(0).y);
	}
}
