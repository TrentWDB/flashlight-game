package com.aetherpass;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Trent on 2/23/2016.
 */

public class MainFrame extends Canvas {
    private static JFrame frame;

    private Image buffer;
    public Graphics2D graphics;

    public MainFrame() {
        setPreferredSize(new Dimension(Game.DEFAULT_WIDTH, Game.DEFAULT_HEIGHT));
    }

    public Graphics2D resizeGraphics(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) buffer.getGraphics();

        return graphics;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public boolean processEvent(Event e) {
        System.out.println(e);
        return false;
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        Game game = new Game(mainFrame);

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(mainFrame);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
