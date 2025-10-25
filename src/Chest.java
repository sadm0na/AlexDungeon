package src;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Creates a chest for the room in dungeon.
 * Can check if player is near and if it is collected.
 * 
 */

public class Chest implements Sizes {
    private MyPanel panel;
    private double x;
    private double y;
    private boolean isCollected;
    private int type; // 0 - info, 1 - + health, 2 - + strength, 3 - special.
    private int health = 0;
    private int strength = 0;
    private Image image;

    public Chest(double x, double y, int type, int variableToSet, MyPanel panel) {
        this.panel = panel;
        this.type = type;
        if (type == 1) {
            health = variableToSet;
        } else if (type == 2) {
            strength = variableToSet;
        }
        this.x = x;
        this.y = y;
        this.isCollected = false;

        String imagePathString = "misc/Chests/closedChest" + type + ".png";
        ImageIcon img = new ImageIcon(PathFinder.findFile(imagePathString));

        image = img.getImage();
        image = image.getScaledInstance((int) (CHEST_WIDTH[type] * WHOLE_SCREEN_W),
            (int) (CHEST_HEIGHT[type] * WHOLE_SCREEN_H), Image.SCALE_DEFAULT);
    }

    /**
     * Checks if player is near.
     */
    public boolean isPlayerNear(double playerX, double playerY) { 
        return (Math.pow((x - CHEST_WIDTH[type] / 2) - playerX, 2) 
            + Math.pow((y - CHEST_HEIGHT[type] / 2) - playerY, 2)) < Math.pow(CHEST_NEAR_AREA, 2);
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

    /**
     * If Chest has not been collected before changes it to collected Chest.
     * 
     */
    public void collectChest(JFrame frame) {
        if (isCollected) {
            return;
        }
        if (type == 0) { // For the chest in the first room with instruction.
            
            Scanner scanner = null;
            String line = ""; // Is used to transfer text from time to a text in MessageDialog.
            
            try {
                scanner = new Scanner(new File(PathFinder.findFile("misc/Chests/intro.txt")));
                while (scanner.hasNextLine()) {
                    String stringInFile = scanner.nextLine();
                    line += "\n" + stringInFile;
                }
                scanner.close();
            } catch (FileNotFoundException fe) {
                return;
            }
            JOptionPane.showMessageDialog(frame.getComponent(0),
                "Read the instruction to the game: " + line);


        } else {
            if (type == 1) {
                panel.setChestMessage("You have +" + health + " to your maximal hp!");
            } else {
                if (type == 2) {
                    panel.setChestMessage("You have +" + strength + " strength to all swords!");
                } else {
                    panel.setChestMessage("You have won the game!");
                }
            }   
        }
        isCollected = true;

        String imagePathString = "misc/Chests/openedChest" + type + ".png";
        ImageIcon img = new ImageIcon(PathFinder.findFile(imagePathString));

        image = img.getImage();
        image = image.getScaledInstance((int) (CHEST_WIDTH[type] * WHOLE_SCREEN_W),
            (int) (CHEST_HEIGHT[type] * WHOLE_SCREEN_H), Image.SCALE_DEFAULT); // Scale image.
    }

    public Image getImage() {
        return image;
    }
}
