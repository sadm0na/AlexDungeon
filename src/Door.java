package src;

import java.awt.*;
import javax.swing.*;

/**
 * Creates a door for the room in dungeon.
 * Can check if object is near.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */

public class Door implements Sizes {
    private double x;
    private double y;
    private int targetRoomId; // index of the room in whith Alex will be transfered
    Image image;
    
    public Door(double x, double y, int targetRoomId) {
        this.x = x;
        this.y = y;
        this.targetRoomId = targetRoomId;

        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Doors/Door1.png"));

        image = img.getImage();
        image = image.getScaledInstance((int) (DOOR_WIDTH * WHOLE_SCREEN_W),
            (int) (DOOR_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT);
    }
    
    /**
     * Checks if player is near.
     */
    public boolean isObjectNear(double objectX, double objectY) {
        return (Math.pow(x - objectX, 2) + Math.pow(y - objectY, 2) < Math.pow(DOOR_NEAR_AREA, 2));
    }
    
    public int getTargetRoomId() {
        return targetRoomId;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}