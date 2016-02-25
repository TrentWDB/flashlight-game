package com.aetherpass.editor;

import java.awt.*;

public class Tool {
	public String tool;
	
	public int x;
	public int y;
	
	public Tool(String s, int x, int y) {
		tool = s;
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color((int) tool.hashCode() | 0xff000000));
		g.fillRect(x, y, 40, 40);
		
		if (LevelEditor.tool.equals(tool)) {
			g.setColor(new Color((Integer.MAX_VALUE - (int) tool.hashCode()) | 0xff000000));
			((Graphics2D) g).setStroke(new BasicStroke(2));
			g.drawRect(x, y, 40, 40);
		}
	}
	
	public boolean contains(int x, int y) {
		if (x >= this.x && x <= this.x + 40) {
			if (y >= this.y && y <= this.y + 40) {
				return true;
			}
		}
		return false;
	}
}
