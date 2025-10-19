package src;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class RoomData { // сюда надо будет засунуть и сундуки и ключики
    private String backgroundPath;
    private double playerStartX;
    private double playerStartY;
    private List<Door> doors;
    private Key key;
    private Chest chest;
    private boolean visited;
    private String miniMapPath;

    
    public RoomData(String backgroundPath, String miniMapPath, double startX, double startY) {
        this.backgroundPath = backgroundPath;
        this.miniMapPath = miniMapPath;
        this.playerStartX = startX;
        this.playerStartY = startY;
        this.doors = new ArrayList<>();
        this.visited = false;
    }
    
    public void addDoor(Door door) {
        doors.add(door);
    }

    public void addKey() {
        int[] position = getRandomKeyPosition();
        this.key = new Key(position[0], position[1]);
    }
    
    public void addChest(int X, int Y) {
        this.chest = new Chest(X, Y);
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public String getminiMapPath() {
        return miniMapPath;
    }
    
    public double getPlayerStartX() {
        return playerStartX;
    }
    
    public double getPlayerStartY() {
        return playerStartY;
    }
    
    public List<Door> getDoors() {
        return doors;
    }

    public Key getKey() {
        return key;
    }

    public Chest getChest() {
        return chest;
    }
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited(boolean visited) { // посетили комнату только когда карточная игра пройдена
        this.visited = visited;
    }

    // рандомная координата вблизи стены
    private int[] getRandomKeyPosition() { // {320, 325}
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int border = 100;

        int miniMapSize = 200;
        int minimapW = miniMapSize + (int)((double)miniMapSize * 0.3);
        int minimapH = miniMapSize;

        //int backSize = 800;
        int backW = (int)screenSize.getWidth() - border * 2;
        int backH = (int)screenSize.getHeight() - border - minimapH;

        int keySize = 5;
        int borderLeft = border; // x = 50;
        int borderLeftPlus = borderLeft + keySize; //x = 55
        int borderUp = border - 10; // y = 50;
        int borderDown = border - 10 + backH - keySize;// y = 320;
        int borderDownPlus = border - 10 + backH + keySize;// y = 325;

        int[][][] walls = {{{borderLeft, borderLeftPlus}, {borderLeftPlus, borderDown}},
            {{borderLeftPlus, borderDown}, {borderLeft, borderLeftPlus}}, 
            {{borderDown, borderDownPlus }, {borderLeftPlus, borderDown}}, 
            {{borderLeftPlus, borderDown}, {borderDown, borderDownPlus }}}; // когда размеры фрема поменяются это надо будет изменить
        boolean isSpaceAvailable = false;
        boolean noDoorsAround = true;
        int[] keyPosiiton = new int[2];
        int xMin, xMax, yMin, yMax;

        while (!isSpaceAvailable) {
            // выберет стену
            int wallNumber = (int) (Math.random() * 4);

            // минимальная и максимальные значения координат стены
            xMin = walls[wallNumber][0][0];
            xMax = walls[wallNumber][0][1];

            yMin = walls[wallNumber][1][0];
            yMax = walls[wallNumber][1][1];

            // рандомная координата на стене
            keyPosiiton[0] = rnd(xMin, xMax);
            keyPosiiton[1] = rnd(yMin, yMax);

            // провека есть ли в этой зоне дверь
            for (Door door: doors) {
                if (door.isObjectNear(keyPosiiton[0], keyPosiiton[1])) {
                    noDoorsAround = false;
                }
            }

            if (noDoorsAround) {
                isSpaceAvailable = true;
            }
        }

        return keyPosiiton;
    }

    // рандомное число в пределах минимального максимального числа
    private static int rnd(int min, int max) {
        max -= min;
        return ((int) (Math.random() * ++max) + min);
    }
}
