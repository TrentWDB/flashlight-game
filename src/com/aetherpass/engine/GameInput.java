package com.aetherpass.engine;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Trent on 2/24/2016.
 */
public class GameInput implements KeyListener, MouseInputListener {
    public static boolean[] keysDown = new boolean[256];
    public static boolean[] keysDeltaDown = new boolean[256];
    public static boolean[] keysDeltaUp = new boolean[256];
    public static List<Integer> keysDeltaDownClear = new LinkedList<Integer>();
    public static List<Integer> keysDeltaUpClear = new LinkedList<Integer>();

    public static boolean mouseLeftDown;
    public static boolean mouseRightDown;
    public static boolean mouseLeftDeltaDown;
    public static boolean mouseLeftDeltaUp;
    public static boolean mouseRightDeltaDown;
    public static boolean mouseRightDeltaUp;
    public static int[] mousePos = new int[2];
    public static int[] mousePosOld = new int[2];

    public static void clearDeltas() {
        for (Integer keyCode : keysDeltaDownClear) {
            keysDeltaDown[keyCode] = false;
        }
        for (Integer keyCode : keysDeltaUpClear) {
            keysDeltaUp[keyCode] = false;
        }
        mouseLeftDeltaDown = false;
        mouseLeftDeltaUp = false;
        mouseRightDeltaDown = false;
        mouseRightDeltaUp = false;

        keysDeltaDownClear.clear();
        keysDeltaUpClear.clear();

        mousePosOld[0] = mousePos[0];
        mousePosOld[1] = mousePos[1];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        keysDown[keyCode] = true;
        keysDeltaDown[keyCode] = true;
        keysDeltaDownClear.add(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        keysDown[keyCode] = false;
        keysDeltaUp[keyCode] = true;
        keysDeltaUpClear.add(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();

        switch (button) {
            case MouseEvent.BUTTON1:
                mouseLeftDown = true;
                mouseLeftDeltaDown = true;
                break;

            case MouseEvent.BUTTON3:
                mouseRightDown = true;
                mouseRightDeltaDown = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();

        switch (button) {
            case MouseEvent.BUTTON1:
                mouseLeftDown = false;
                mouseLeftDeltaUp = true;
                break;

            case MouseEvent.BUTTON3:
                mouseRightDown = false;
                mouseRightDeltaUp = true;
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos[0] = e.getX();
        mousePos[1] = e.getY();
    }
}
