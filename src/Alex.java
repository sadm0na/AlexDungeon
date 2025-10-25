package src;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

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
    
    public void draw(Graphics g) {
        Image ai = alexImage.getImage();
        ai = ai.getScaledInstance((int) (alexWeigth * wholeScreenW), 
            (int) (alexHeight * wholeScreenH), Image.SCALE_DEFAULT);
        g.drawImage(ai, (int) (x * wholeScreenW), (int) (y * wholeScreenH), null);
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
    
    public void update(long timeDifference) {
        double newX = x;
        double newY = y;
        
        newX += timeDifference * runningSpeed * xRunningDirection;
        newY += timeDifference * runningSpeed * yRunningDirection;
 

        if (newX >= borderLeftInner && newX <= borderRightInner - alexWeigth) {
            x = newX;
        } else {
            stopRunningX();
        }
        
        if (newY >= borderUpInner && newY <= borderDownInner - alexHeight) {
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