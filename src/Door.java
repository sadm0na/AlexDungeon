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

public class Door { // позиция, размеры двери, и то куда она ведет персонажа
    private double x, y;
    private double width, height;
    private int targetRoomId; // индекс комнаты в которую попадем
    Image image;
    
    public Door(double x, double y, double width, double height, int targetRoomId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targetRoomId = targetRoomId;
        ImageIcon img = new ImageIcon(PathFinder.findFile("misc/Doors/Door1.png"));
        image = img.getImage();
        image = image.getScaledInstance(85,100,Image.SCALE_DEFAULT);
    }
    
    public boolean isObjectNear(double objectX, double objectY) { // дубликаты часто используются. надо наверное в отдельный класс items вынести эти функции
        int nearArea = 100;
        return (Math.pow(x - objectX, 2) + Math.pow(y - objectY, 2) < 10000);
        // objectX >= x && objectX <= x + width + nearArea &&
              // objectY >= y && objectY <= y + height + nearArea;
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