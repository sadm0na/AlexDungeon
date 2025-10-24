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

public class Door implements Sizes { // позиция, размеры двери, и то куда она ведет персонажа
    private double x, y;
    private int targetRoomId; // индекс комнаты в которую попадем
    Image image;
    
    public Door(double x, double y, int targetRoomId) {
        this.x = x;
        this.y = y;
        this.targetRoomId = targetRoomId;
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Doors/Door1.png"));
        image = img.getImage();
        image = image.getScaledInstance((int) (doorWidth * wholeScreenW),
            (int) (doorHeight * wholeScreenH), Image.SCALE_DEFAULT);
    }
    
    public boolean isObjectNear(double objectX, double objectY) { // дубликаты часто используются. надо наверное в отдельный класс items вынести эти функции
        return (Math.pow(x - objectX, 2) + Math.pow(y - objectY, 2) < Math.pow(doorNearArea, 2));
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