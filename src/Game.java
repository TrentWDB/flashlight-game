import java.awt.*;

/**
 * Created by Trent on 2/24/2016.
 */
public class Game {
    public static int DEFAULT_WIDTH = 1600;
    public static int DEFAULT_HEIGHT = 800;

    private MainFrame mainFrame;
    private GameLoop gameLoop;
    private Graphics2D graphics;

    public Game(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        gameLoop = new GameLoop(this);

        initialize();
    }

    private void initialize() {
        resize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        new Thread(gameLoop).start();
    }

    public void resize(int width, int height) {
        graphics = mainFrame.resizeGraphics(width, height);
        mainFrame.setSize(new Dimension(width, height));
    }

    public void flushGraphics() {
        mainFrame.repaint();
    }

    public Graphics2D getGraphics() {
        return graphics;
    }
}
