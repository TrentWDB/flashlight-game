package com.aetherpass.editor;

import java.awt.*;
import java.util.ArrayList;


public class LevelObject {
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public double angle;
	
	public int type;
	
	public LevelObject(int x1, int y1, int x2, int y2) {
		points.add(new Point(LevelEditor.snap(x1), LevelEditor.snap(y1)));
		points.add(new Point(LevelEditor.snap(x2), LevelEditor.snap(y2)));
	}
	
	public LevelObject(int x1, int y1, double angle) {
		points.add(new Point(LevelEditor.snap(x1), LevelEditor.snap(y1)));
		this.angle = angle;
	}
	
	public LevelObject(int x1, int y1) {
		points.add(new Point(LevelEditor.snap(x1), LevelEditor.snap(y1)));
	}
	
	public void move(int x, int y) {
		for (Point p : points) {
			p.x += x;
			p.y += y;

			p.x = LevelEditor.snap(p.x);
			p.y = LevelEditor.snap(p.y);
		}
	}
	
	public boolean contains(int x, int y) {
		return false;
	}

	public boolean removeSelectedVertex() {
		return false;
	}
	
	public void drawEditing(Graphics g, int x, int y) {
		
	}

	public void draw(Graphics g, int x, int y) {

	}
	
	public String serialize() {
		String s = "";

		
		return s;
	}
}
