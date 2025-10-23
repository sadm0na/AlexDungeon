package src;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;


import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Alex {
    private ImageIcon alexImage;
    private double x;
    private double y;
    double RunningSpeed;
    private int xRunningDirection;
    private int yRunningDirection;
    
    public Alex(double x, double y) throws IOException {
        this.x = x;
        this.y = y;
        this.RunningSpeed = 0.15;
        this.xRunningDirection = 0;
        this.yRunningDirection = 0;
        
        alexImage = new ImageIcon(ImageIO.read(new File(PathFinder.findFile("misc/Alex/Alex2.png")))); // это немного поменяется когда анимацию сделаю. хотя вроде бы можно в джаве прям написать так чтобы фотка зеркалилась
        
    }
    
    public void draw(Graphics g) {
        int imageX = (int) x;
        int imageY = (int) y;
        
        Image ai = alexImage.getImage();
        ai = ai.getScaledInstance(90,100,Image.SCALE_DEFAULT);
        g.drawImage(ai, imageX, imageY, null);
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
        
        newX += timeDifference * RunningSpeed * xRunningDirection;
        newY += timeDifference * RunningSpeed * yRunningDirection;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int border = 100;
        int miniMapSize = 250;
        int minimapH = miniMapSize;

        int backW = (int)screenSize.getWidth() - border * 2;
        int backH = (int)screenSize.getHeight() - border - minimapH;

        //border - 10, border - 10, backW,backH 
        int leftBorder = border - 18;
        int rightBorder = leftBorder + backW - 75;
        int upBorder = border - 10;
        int downBorder = border - 52  + backH;

        if (newX >= leftBorder && newX <= rightBorder) {
            x = newX;
        } else {
            stopRunningX();
        }
        
        if (newY >= upBorder && newY <= downBorder) {
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
}