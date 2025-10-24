package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Key implements Sizes{
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
        image = image.getScaledInstance((int) (keyWidth * wholeScreenW),
             (int) (keyHeight * wholeScreenH), Image.SCALE_DEFAULT);
    }

    public boolean isPlayerNear(double playerX, double playerY) { // проверяю попадает ли в окружность ключа..?
        return Math.pow(x - (playerX + alexWeigth / 2), 2) + Math.pow(y - (playerY + alexHeight / 2), 2) < Math.pow(keyNearArea, 2);
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
