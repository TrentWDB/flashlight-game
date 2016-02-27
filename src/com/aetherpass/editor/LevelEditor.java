package com.aetherpass.editor;

import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class LevelEditor extends Applet implements Runnable {
	public static final int VERTEX_DRAW_RADIUS = 4;
	public static final int VERTEX_SELECTION_RADIUS = 6;
	public static final int SNAP_AMOUNT = 12;

	public static final int KEY_SPACE = 32;
	public static final int KEY_S = 115;
	public static final int KEY_DELETE = 127;
	public static final int KEY_BACKSPACE = 8;

	private int windowWidth, windowHeight;
	private Image offScreenImage;
	private Graphics graphics;
	private Thread updateThread;
	
	public static int posX;
	public static int posY;
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private boolean[] keys = new boolean[65536];
	
	private ArrayList<Tool> tools = new ArrayList<Tool>();
	private ArrayList<LevelObject> level = new ArrayList<LevelObject>();
	
	public static String tool;

	public static int initMouseX;
	public static int initMouseY;
	public static int mouseX;
	public static int mouseY;
	private boolean mouseDown;
	private boolean mouseAntiSpam;

	private boolean keyAntispam;
	public static boolean snapping;
	
	private int editing = -1;
	
	private Font font;
	
	public static final int SPAWN = 0;
	public static final int WALL = 1;
	public static final int LIGHT = 2;
	public static final int POLYGON = 3;
	
	public static final int GUY_WIDTH = 30;
	
	public void init() {
		windowWidth = (int) screenSize.getWidth();
		windowHeight = (int) screenSize.getHeight();
		offScreenImage = createImage(windowWidth, windowHeight);
		graphics = offScreenImage.getGraphics();
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		font = new Font("Arial", Font.PLAIN, 12);
		graphics.setFont(font);
		
		tools.add(new Tool("Move", 10, 60));
		tools.add(new Tool("Wall", 10, 110));
		tools.add(new Tool("Polygon", 10, 160));
		tools.add(new Tool("Light", 10, 210));
		tools.add(new Tool("Spawn", 10, 260));
		tools.add(new Tool("Export", 10, 460));
		
		tool = tools.get(0).tool;
		
		updateThread = new Thread(this);
		updateThread.start();
	}
	
	public void updateStuff() {
		PointerInfo a = MouseInfo.getPointerInfo();

		int oldMouseX = mouseX;
		int oldMouseY = mouseY;
		try {
			mouseX = (int) (a.getLocation().getX() - this.getLocationOnScreen().x);
			mouseY = (int) (a.getLocation().getY() - this.getLocationOnScreen().y);
		} catch (Exception e) {
		}

		if (keys[KEY_S] && keyAntispam) {
			editing = -1;
			snapping = !snapping;
		}

		// quick and easy way to remove things that got added accidentally that aren't visible due to too few vertices
		if (editing < 0 && level.size() > 0) {
			if (level.get(level.size() - 1).shouldRemove()) {
				level.remove(level.size() - 1);
			}
		}
		
		if (mouseAntiSpam) {
			if (mouseDown) {
				initMouseX = mouseX;
				initMouseY = mouseY;
			} else if (!mouseDown) {
				for (int i = 0; i < tools.size(); i++) {
					if (tools.get(i).contains(mouseX, mouseY)) {
						tool = tools.get(i).tool;
						editing = -1;

						if (tool.equals("Export")) {
							StringBuilder s = new StringBuilder();

							s.append("[");

							for (int b = 0; b < level.size(); b++) {
								s.append(level.get(b).serialize());

								if (b < level.size() - 1) {
									s.append(",");
								}
							}

							s.append("]");

//							File file = new File("level.json");
//							PrintWriter pw = null;
//							try {
//								pw = new PrintWriter(new FileOutputStream(file));
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//							System.out.println("here we go");
//							pw.println(s.toString());
//							pw.flush();
//							pw.close();
//							System.out.println("done " + file.getAbsolutePath());

							Toolkit toolkit = Toolkit.getDefaultToolkit();
							Clipboard clipboard = toolkit.getSystemClipboard();
							StringSelection strSel = new StringSelection(s.toString());
							clipboard.setContents(strSel, null);
						}
						break;
					}
				}
			}
		}

		boolean inBox = false;

		if (mouseX < 63) {
			if (mouseY > 47 && mouseY < 513) {
				inBox = true;
			}
		}
		if (mouseX < 82 && mouseY < 18) {
			inBox = true;
		}

		if ((keys[KEY_DELETE] || keys[KEY_BACKSPACE]) && keyAntispam) {
			if (editing >= 0 && !level.get(editing).removeSelectedVertex()) {
				level.remove(editing);
				editing = -1;
			}
		}
		
		if (tool.equals("Move")) {
			if (mouseDown) {
				if (mouseAntiSpam && !inBox) {
					// let everything know that we clicked
					level.forEach(LevelObject::clicked);

					editing = -2;
					LevelObject obj = null;
					for (int i = level.size() - 1; i >= 0; i--) {
						obj = level.get(i);
						if (obj.contains(mouseX + posX, mouseY + posY)) {
							editing = i;
							break;
						}
					}
				}
				if (editing == -2) {
					posX -= (mouseX - oldMouseX);
					posY -= (mouseY - oldMouseY);
				}
				if (editing >= 0 && !inBox) {
					level.get(editing).move(mouseX + posX, mouseY + posX);
				}
			}
		} else if (tool.equals("Wall")) {
			if (mouseDown && mouseAntiSpam && !inBox) {
				if (editing == -1) {
					int x = LevelEditor.snap(mouseX + posX);
					int y = LevelEditor.snap(mouseY + posY);

					level.add(new Wall(x, y));

					editing = level.size() - 1;
				} else {
					LevelObject obj = level.get(editing);

					int x1 = obj.points.get(0).x;
					int y1 = obj.points.get(0).y;
					int x2 = LevelEditor.snap(mouseX + posX);
					int y2 = LevelEditor.snap(mouseY + posY);

					obj.points.clear();
					obj.points.add(new Point(Math.min(x1, x2), Math.min(y1, y2)));
					obj.points.add(new Point(Math.max(x1, x2), Math.max(y1, y2)));

					editing = -1;
				}
			}
		} else if (tool.equals("Polygon")) {
			if (mouseDown && mouseAntiSpam && !inBox) {
				if (editing == -1) {
					int x = LevelEditor.snap(mouseX + posX);
					int y = LevelEditor.snap(mouseY + posY);

					level.add(new Polygon(x, y));

					editing = level.size() - 1;
				} else {
					int x = LevelEditor.snap(mouseX + posX);
					int y = LevelEditor.snap(mouseY + posY);

					LevelObject obj = level.get(editing);

					int xDistToStart = x - obj.points.get(0).x;
					int yDistToStart = y - obj.points.get(0).y;

					if (Math.sqrt(xDistToStart * xDistToStart + yDistToStart * yDistToStart) <= 4) {
						editing = -1;
					} else {
						obj.points.add(new Point(x, y));
					}
				}
			}
		} else if (tool.equals("Light")) {
			
		} else if (tool.equals("Spawn")) {
			if (mouseDown && mouseAntiSpam && !inBox) {
				int x1 = LevelEditor.snap(mouseX + posX);
				int y1 = LevelEditor.snap(mouseY + posY);

				level.add(new Spawn(x1, y1));

				for (int i = 0; i < level.size() - 1; i++) {
					if (level.get(i) instanceof Spawn) {
						level.remove(i);
					}
				}

				editing = level.size() - 1;
			}
		}
		
		mouseAntiSpam = false;
		keyAntispam = false;
	}
	
	public void paint(Graphics g) {
		drawGrid(graphics, 72, 72, 5);
		
		drawLevel(graphics);
		
		drawSideBar(graphics);
		g.drawImage(offScreenImage, 0, 0, this);
	}
	
	public void drawLevel(Graphics g) {
		for (int i = 0; i < level.size(); i++) {
			LevelObject obj = level.get(i);

			if (editing == i) {
				obj.drawEditing(graphics, -posX, -posY);
			} else {
				obj.draw(graphics, -posX, -posY);
			}
		}

		/*
		g.setColor(Color.RED);
		for (int i = 0; i < level.size(); i++) {
			LevelObject obj1 = level.get(i);
			
			if (obj1 instanceof Wall) {
				for (int a = 0; a < level.size(); a++) {
					LevelObject obj2 = level.get(a);
					
					if (a != i && obj2 instanceof Wall) {
						int x1 = Math.max(obj1.points.get(0).x, obj2.points.get(0).x);
						int y1 = Math.max(obj1.points.get(0).y, obj2.points.get(0).y);
						int x2 = Math.min(obj1.points.get(1).x, obj2.points.get(1).x);
						int y2 = Math.min(obj1.points.get(1).y, obj2.points.get(1).y);
						
						int width1 = Math.max(obj1.points.get(0).x, obj1.points.get(1).x) - Math.min(obj2.points.get(0).x, obj2.points.get(1).x);
						int width2 = Math.min(obj1.points.get(0).x, obj1.points.get(1).x) - Math.max(obj2.points.get(0).x, obj2.points.get(1).x);
						
						int height1 = Math.max(obj1.points.get(0).y, obj1.points.get(1).y) - Math.min(obj2.points.get(0).y, obj2.points.get(1).y);
						int height2 = Math.min(obj1.points.get(0).y, obj1.points.get(1).y) - Math.max(obj2.points.get(0).y, obj2.points.get(1).y);
						
						boolean insideX = Math.signum(width1) * Math.signum(width2) == -1;
						boolean lessThanWidth = !insideX && Math.min(Math.abs(width1), Math.abs(width2)) < GUY_WIDTH;
						boolean insideY = Math.signum(height1) * Math.signum(height2) == -1;
						boolean lessThanHeight = !insideY && Math.min(Math.abs(height1), Math.abs(height2)) < GUY_WIDTH;
						
						boolean touching = insideX && insideY;
						
						if (((Math.abs(y2 - y1) < GUY_WIDTH && (insideX || lessThanWidth)) || (Math.abs(x2 - x1) < GUY_WIDTH && (insideY || lessThanHeight))) && !touching) {
							drawX(graphics, x1 - posX, y1 - posY, x2 - posX, y2 - posY);
						}
					}
				}
			}
		}*/
	}
	
	public void drawX(Graphics g, int x1a, int y1a, int x2a, int y2a) {
		int x1 = Math.min(x1a, x2a);
		int y1 = Math.min(y1a, y2a);
		int x2 = Math.max(x1a, x2a);
		int y2 = Math.max(y1a, y2a);
		
		((Graphics2D) g).setStroke(new BasicStroke(1));
		g.drawRect(x1, y1, x2 - x1, y2 - y1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y1, x1, y2);
	}
	
	public void drawSideBar(Graphics g) {
		int width = 60;
		int height = 460;
		int outlineThickness = 2;
		
		g.setColor(new Color(100, 100, 100));
		// main tool background
		g.fillRect(0, 50, width, height);
		// tool string background
		g.fillRect(0, 0, 80, 16);
		// snapped on off background
		g.fillRect(0, getHeight() - 16, 80, 16);
		
		g.setColor(new Color(255, 255, 255));
		// main tool outline
		g.fillRect(0, 50 - outlineThickness, width + outlineThickness, outlineThickness);
		g.fillRect(width, 50, outlineThickness, height);
		g.fillRect(0, 50 + height, width + outlineThickness, outlineThickness);
		// tool string outline
		g.fillRect(80, 0, outlineThickness, 16);
		g.fillRect(0, 16, 80 + outlineThickness, 2);
		// snapping on off outline
		g.fillRect(80, getHeight() - 16, outlineThickness, 16);
		g.fillRect(0, getHeight() - 16 - outlineThickness, 80 + outlineThickness, outlineThickness);

		g.drawString(tool, 5, 13);
		g.drawString(snapping ? "snapping on" : "snapping off", 5, getHeight() - 16 + 13);
		
		for (int i = 0; i < tools.size(); i++) {
			tools.get(i).draw(g);
		}
	}
	
	public void drawGrid(Graphics g, int width, int height, int lineWidth) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(new Color(75, 75, 75));
		for (int x = (int) (-posX % (width + lineWidth)); x < windowWidth; x += width + lineWidth) {
			g.fillRect(x - lineWidth, 0, lineWidth, windowHeight);
		}
		for (int y = (int) (-posY % (height + lineWidth)); y < windowHeight; y += height + lineWidth) {
			g.fillRect(0, y - lineWidth, windowWidth, lineWidth);
		}
	}

	public static int snap(int n) {
		if (!snapping) {
			return n;
		}

		return (int) Math.round((double) n / SNAP_AMOUNT) * SNAP_AMOUNT;
	}
	
	public void run() {
		while (true) {
			updateStuff();
			repaint();
			pause(15);
		}
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	private void pause(int milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException ie) {
		}
	}
	
	public boolean handleEvent(Event e) {
		if (e.id == 501) { 
			mouseDown = true;
			mouseAntiSpam = true;
		} else if (e.id == 502) {
			mouseDown = false;
			mouseAntiSpam = true;
		} else if (e.id == Event.KEY_PRESS) {
			if (!keys[e.key]) {
				keyAntispam = true;
			}
			keys[e.key] = true;
		} else if (e.id == Event.KEY_RELEASE) {
			keys[e.key] = false;
		}
		return false;
	}
}
