package src;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Alex {
    private BufferedImage alexImage;
    private double x;
    private double y;
    private double RunningSpeed;
    private int xRunningDirection;
    private int yRunningDirection;
    
    public Alex(double x, double y) throws IOException {
        this.x = x;
        this.y = y;
        this.RunningSpeed = 0.15;
        this.xRunningDirection = 0;
        this.yRunningDirection = 0;
        
        alexImage = ImageIO.read(new File(PathFinder.findFile("misc/Alex/AlexPic.png"))); // это немного поменяется когда анимацию сделаю. хотя вроде бы можно в джаве прям написать так чтобы фотка зеркалилась
    }
    
    public void draw(Graphics g) {
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
    
    public void update(long timeDifference) {
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

    // всего лишь то это добавила. для того чтобы сравнивать позицию алекса с другими объектами.

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setPosition(double x, double y) { 
        this.x = x;
        this.y = y;
        stopRunningX();
        stopRunningY();
    }
}