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
    private int health = 0;
    private int strength = 0;
    Image image;
    private int width = 50;
    private int height = 45;


    public Chest(double x, double y, int type, int h) {
        this.type = type;
        if (type == 1) {
            health = h;
        }
        if (type == 2) {
            strength = h;
        }
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

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
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

    public void collectChest(JFrame frame) {
        if (!isCollected) {
            if (type == 0) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "Read the instruction to the game: ");
            } else {
                if (type == 1) {
                    JOptionPane.showMessageDialog(frame.getComponent(0), "You have +" + health + " to your maximal hp!");
                } else {
                    if (type == 2) {
                        JOptionPane.showMessageDialog(frame.getComponent(0), "You have +" + strength + " strength to all swords!");
                    } else {
                        JOptionPane.showMessageDialog(frame.getComponent(0), "You have have won the game!");
                    }
                }
            }
        }
        isCollected = true;
    }
}
