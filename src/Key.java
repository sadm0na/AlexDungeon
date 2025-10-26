package src;

import java.awt.*;
import javax.swing.*;

/**
 * Creates a key for the room in dungeon.
 * Can check if player is near and if it is collected.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */

public class Key implements Sizes {
    private double x;
    private double y;
    private boolean isCollected;
    Image image;

    
    public Key(double x, double y) {
        this.x = x;
        this.y = y;
        this.isCollected = false;
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Keys/key.png"));
        image = img.getImage();
        image = image.getScaledInstance((int) (KEY_WIDTH * WHOLE_SCREEN_W),
             (int) (KEY_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT);
    }

    /**
     * Checks if player is near.
     */
    public boolean isPlayerNear(double playerX, double playerY) { 
        return Math.pow(x - (playerX + ALEX_WIDTH / 2), 2) 
            + Math.pow(y - (playerY + ALEX_HEIGHT / 2), 2) < Math.pow(KEY_NEAR_AREA, 2);
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public boolean isKeyCollected() {
        return isCollected;
    }

    public void collectKey() {
        isCollected = true;
    }
}
