import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyPanel extends JPanel implements KeyEventDispatcher {
    private Alex alex;
    private long lastFrameTime; // когда последний раз обновлялось
    private BufferedImage backgroundImage;

    public MyPanel() throws IOException {
        this.setPreferredSize(new Dimension(400, 400));
        this.backgroundImage = ImageIO.read(new File("Room.png"));
        this.alex = new Alex(200, 200); // в зависимости от комнаты (двери в комнате) мб будет появляться в разных местах, а не центре
        this.lastFrameTime = System.currentTimeMillis();

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); 
        }

        alex.draw(g);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        int keyCode = key.getKeyCode();

        if (key.getID() == KeyEvent.KEY_PRESSED) { // кнопка была нажата
            switch (keyCode) {
                case KeyEvent.VK_A:
                    alex.runLeft();
                    break;
                case KeyEvent.VK_D:
                    alex.runRight();
                    break;
                case KeyEvent.VK_W:
                    alex.runUp();
                    break;
                case KeyEvent.VK_S:
                    alex.runDown();
                    break;
            }
        }

        if (key.getID() == KeyEvent.KEY_RELEASED) { // отпустили
            switch (keyCode) {
                case KeyEvent.VK_A:
                case KeyEvent.VK_D:
                    alex.stopRunningX();
                    break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_S:
                    alex.stopRunningY();
                    break;
            }
        }

        return false;
    }

    void updateWorldPhysics() {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastFrameTime;

        alex.update(timeDifference);

        lastFrameTime = currentTime;
    }
}
