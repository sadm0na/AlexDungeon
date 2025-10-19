package src;
public class Key {
    private double x;
    private double y;
    private boolean isCollected;


    public Key(double x, double y) {
        this.x = x;
        this.y = y;
        this.isCollected = false;
    }

    public boolean isPlayerNear(double playerX, double playerY) { // проверяю попадает ли в окружность ключа..?
        return Math.pow(x - playerX, 2) + Math.pow(y - playerY, 2) < 65;
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public boolean isKeyCollected() {
        return isCollected;
    }

    public void collectKey() {
        isCollected = true;
    }
}
