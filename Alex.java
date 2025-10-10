import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

// здоровья и силы меча пока нет. надо будет добавить.
public class Alex {
    private BufferedImage alexImage;

    private double x;
    private double y;
    private double RunningSpeed;
    private int xRunningDirection; // 0 - not running, 1 - right, -1 - left
    private int yRunningDirection;

    public Alex(double x, double y) throws IOException { // тоже анимация добавится
        this.x = x;
        this.y = y;
        this.RunningSpeed = 0.15;
        this.xRunningDirection = 0;
        this.yRunningDirection = 0;

        alexImage = ImageIO.read(new File("AlexPic.png"));
    }

    public void draw(Graphics g) { // сюда добавятся строки для анимации
        int imageX = (int) x;
        int imageY = (int) y;

        g.drawImage(alexImage, imageX, imageY, null);
    }

    public void runUp() {
        yRunningDirection = -1;
    }

    public void runDown() {
        yRunningDirection = 1;
    }

    public void runLeft() {
        xRunningDirection = -1;
    }

    public void runRight() {
        xRunningDirection = 1;
    }

    public void stopRunningX() {
        xRunningDirection = 0;
    }

    public void stopRunningY() {
        yRunningDirection = 0;
    }

    public void update(long timeDifference) { // сюда тоже добавятся строки для анимации
        double newX = x;
        double newY = y;

        newX += timeDifference * RunningSpeed * xRunningDirection;
        newY += timeDifference * RunningSpeed * yRunningDirection;

        if (newX >= 50 && newX <= 335) {
            x = newX;
        } else {
            stopRunningX(); 
        }
        
        if (newY >= 50 && newY <= 325) {
            y = newY;
        } else {
            stopRunningY();
        }
    }
}