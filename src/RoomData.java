package src;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class RoomData implements Sizes{ // сюда надо будет засунуть и сундуки и ключики
    private String backgroundPath;
    private double playerStartX;
    private double playerStartY;
    private List<Door> doors;
    private Key key;
    private List<Chest> chests;
    private boolean visited;
    private String miniMapPath;

    
    public RoomData(String backgroundPath, String miniMapPath) {
        this.backgroundPath = backgroundPath;
        this.miniMapPath = miniMapPath;
        this.playerStartX = alexStartX;
        this.playerStartY = alexStartY;
        this.doors = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.visited = false;
    }
    
    public void addDoor(Door door) {
        doors.add(door);
    }

    public void addKey() {
        double[] position = getRandomKeyPosition();
        this.key = new Key(position[0], position[1]);
    }
    
    public void addChest(double X, double Y, int type, int h, MyPanel panel) {
        chests.add(new Chest(X, Y, type, h, panel));
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

    public List<Chest> getChest() {
        return chests;
    }
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited(boolean visited) { // посетили комнату только когда карточная игра пройдена
        this.visited = visited;
    }

    // рандомная координата вблизи стены
    private double[] getRandomKeyPosition() { // {320, 325}
        double[] d = {0.1, 0.2};
        //return d;
        
        double[][][] walls = {{{borderLeft, borderLeftInner}, {borderUp, borderDown}},
            {{borderLeft, borderRight}, {borderUp, borderUpInner}}, 
            {{borderRight, borderRightInner },  {borderUp, borderDown}}, 
            {{borderLeft, borderRight}, {borderDown, borderDownInner }}}; // когда размеры фрема поменяются это надо будет изменить
        boolean isSpaceAvailable = false;
        double[] keyPosiiton = new double[2];
        double xMin, xMax, yMin, yMax;

        while (!isSpaceAvailable) {
            isSpaceAvailable = true;
            // выберет стену
            int wallNumber = (int) (Math.random() * 4);

            // минимальная и максимальные значения координат стены
            xMin = walls[wallNumber][0][0];
            xMax = walls[wallNumber][0][1];

            yMin = walls[wallNumber][1][0];
            yMax = walls[wallNumber][1][1];

            // рандомная координата на стене
            keyPosiiton[0] = rnd(Math.min(xMin, xMax), Math.max(xMax, xMin));
            keyPosiiton[1] = rnd(Math.min(yMin, yMax), Math.max(yMax, yMin));

            // провека есть ли в этой зоне дверь
            for (Door door: doors) {
                if (door.isObjectNear(keyPosiiton[0], keyPosiiton[1])) {
                    isSpaceAvailable = false;
                    break;
                }
            }
        }

        return keyPosiiton;
    }

    // рандомное число в пределах минимального максимального числа
    private static double rnd(double min, double max) {
        return (Math.random() * (max - min) + min);
    }
}
