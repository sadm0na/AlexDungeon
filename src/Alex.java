package src;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Represents the main character Alex in the game.
 * Handles movement, drawing, and position updates.
 */
public class Alex implements Sizes {
    private ImageIcon alexImage;
    private double x;
    private double y;
    private double runningSpeed;
    private int xRunningDirection;
    private int yRunningDirection;
    
    public Alex(double x, double y) throws IOException {
        this.x = x;
        this.y = y;
        this.runningSpeed = 0.00015;
        this.xRunningDirection = 0;
        this.yRunningDirection = 0;
    
        alexImage = new ImageIcon(ImageIO.read(new File(PathFinder.findFile(
            "misc/Alex/Alex2.png"))));
    }
    
    /**
     * Draws Alex on the screen at current position.
     */
    public void draw(Graphics g) {
        Image ai = alexImage.getImage();
        ai = ai.getScaledInstance((int) (ALEX_WIDTH * WHOLE_SCREEN_W), 
            (int) (ALEX_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT);
        g.drawImage(ai, (int) (x * WHOLE_SCREEN_W), (int) (y * WHOLE_SCREEN_H), null);
    }
    
    public void runUp() {
        yRunningDirection = -1;
    }
    
    public void runDown() {
        yRunningDirection = 1;
    }
    
    public void runLeft() {
        xRunningDirection = -1;
    }
    
    public void runRight() {
        xRunningDirection = 1;
    }
    
    public void stopRunningX() {
        xRunningDirection = 0;
    }
    
    public void stopRunningY() {
        yRunningDirection = 0;
    }
    
    /**
     * Updates Alex's position based on movement direction and time.
     */
    public void update(long timeDifference) {
        double newX = x;
        double newY = y;
        
        newX += timeDifference * runningSpeed * xRunningDirection;
        newY += timeDifference * runningSpeed * yRunningDirection;

        if (newX >= BORDER_LEFT_INNER && newX <= BORDER_REIGHT_INNER - ALEX_WIDTH) {
            x = newX;
        } else {
            stopRunningX();
        }
        
        if (newY >= BORDER_UP_INNER && newY <= BORDER_DOWN_INNER - ALEX_HEIGHT) {
            y = newY;
        } else {
            stopRunningY();
        }
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    /**
     * Sets Alex to a new position and stops movement.
     */
    public void setPosition(double x, double y) { 
        this.x = x;
        this.y = y;
        stopRunningX();
        stopRunningY();
    }

    public void setRunningSpeed(double runningSpeed) {
        this.runningSpeed = runningSpeed;
    }
}