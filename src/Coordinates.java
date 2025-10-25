package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class Coordinants {
    private int x;
    private int y;

    public Coordinants(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the position of Alex in one-dimention array.
     **/
    public int getOne() {
        return x * 3 + y; 
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}