import java.io.IOException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

// здоровья и силы меча пока нет. надо будет добавить.
public class Alex {
    private BufferedImage alexImage;

    private double x;
    private double y;
    private double xRunningSpeed;
    private int xRunningDirection; // 0 - not running, 1 - right, -1 - left

    public Alex(double x, double y) throws IOException { // тоже анимация добавится
        this.x = x;
        this.y = y;
        this.xRunningSpeed = 0.3;
        this.xRunningDirection = 0;
    }

    public void draw(Graphics g) { // сюда добавятся строки для анимации
        int imageX = (int) x;
        int imageY = (int) y;

        g.drawImage(alexImage, imageX, imageY, null);
    }

    public void runLeft() {
        xRunningDirection = -1;
    }

    public void runRight() {
        xRunningDirection = 1;
    }

    public void stopRunning() {
        xRunningDirection = 0;
    }

    public void update(long timeDifference) { // сюда тоже добавятся строки для анимации
        x += timeDifference * xRunningSpeed * xRunningDirection;
    }
}