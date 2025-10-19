package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Chest {
    private double x;
    private double y;
    private boolean isCollected;
    private int type; // 0 - info, 1 - + health, 2 - + strength, 3 - special
    Image image;
    private int width = 50;
    private int height = 45;


    public Chest(double x, double y) {
        this.x = x;
        this.y = y;
        this.isCollected = false;
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest1.png"));
        image = img.getImage();
        image = image.getScaledInstance(50,45,Image.SCALE_DEFAULT);
    }

    public boolean isPlayerNear(double playerX, double playerY) { // проверяю попадает ли в окружность ключа..?
        return (Math.pow((x - width / 2) - playerX, 2) + Math.pow((y - height /2)  - playerY, 2)) < 10000;
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public boolean isChestCollected() {
        return isCollected;
    }

    public void collectChest() {
        isCollected = true;
    }
}
