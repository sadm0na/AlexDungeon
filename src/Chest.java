package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Chest {
    private MyPanel panel;
    private double x;
    private double y;
    private boolean isCollected;
    private int type; // 0 - info, 1 - + health, 2 - + strength, 3 - special
    private int health = 0;
    private int strength = 0;
    Image image;
    private int width = 50;
    private int height = 45;


    public Chest(double x, double y, int type, int h, MyPanel panel) {
        this.panel = panel;

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
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest0.png"));
        if (type == 0) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest0.png"));
        }
        if (type == 1) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest1.png"));
        }
        if (type == 2) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest2.png"));
        }
        if (type == 3) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/closedChest3.png"));
        }
        image = img.getImage();
        image = image.getScaledInstance(50,45,Image.SCALE_DEFAULT);
        if (type == 0) {
            image = image.getScaledInstance(80,50,Image.SCALE_DEFAULT);
        }
        if (type == 3) {
            image = image.getScaledInstance(100,60,Image.SCALE_DEFAULT);
        }
    }

    public boolean isPlayerNear(double playerX, double playerY) { // проверяю попадает ли в окружность ключа..?
        int nearArea = 50;
        return (Math.pow((x - width / 2) - playerX, 2) + Math.pow((y - height /2)  - playerY, 2)) < Math.pow(nearArea, 2);
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
                
                Scanner scanner = null;
                String line = "";
                try{
                    scanner = new Scanner(new File(PathFinder.findFile("misc/Chests/intro.txt")));

                    while (scanner.hasNextLine()) {

                        String st = scanner.nextLine();
                        line += "\n" + st;
                        
                        //System.out.println(line); // Обработка строки, возможно, с последующим разбором на части

                    }
                } catch (FileNotFoundException fe) {
                    //System.out.println(fe);
                }
                JOptionPane.showMessageDialog(frame.getComponent(0), "Read the instruction to the game: " + line);


            } else {
                if (type == 1) {
                    panel.setChestMessage("You have +" + health + " to your maximal hp!");
                    //JOptionPane.showMessageDialog(frame.getComponent(0), "You have +" + health + " to your maximal hp!");
                } else {
                    if (type == 2) {
                        panel.setChestMessage("You have +" + strength + " strength to all swords!");
                        //JOptionPane.showMessageDialog(frame.getComponent(0), "You have +" + strength + " strength to all swords!");
                    } else {
                        panel.setChestMessage("You have won the game!");
                        //JOptionPane.showMessageDialog(frame.getComponent(0), "You have won the game!");
                    }
                }
            }
        }

        isCollected = true;
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Chests/openedChest0.png"));

        if (type == 0) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/openedChest0.png"));
        }
        if (type == 1) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/openedChest1.png"));
        }
        if (type == 2) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/openedChest2.png"));
        }
        if (type == 3) {
            img = new ImageIcon(PathFinder.findFile("misc/Chests/openedChest3.png"));
        }
        image = img.getImage();
        image = image.getScaledInstance(50,45,Image.SCALE_DEFAULT);
        if (type == 0) {
            image = image.getScaledInstance(80,50,Image.SCALE_DEFAULT);
        }
        if (type == 3) {
            image = image.getScaledInstance(100,60,Image.SCALE_DEFAULT);
        }
    }
}
