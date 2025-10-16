public class Door { // позиция, размеры двери, и то куда она ведет персонажа
    private double x, y;
    private double width, height;
    private int targetRoomId; // индекс комнаты в которую попадем
    
    public Door(double x, double y, double width, double height, int targetRoomId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.targetRoomId = targetRoomId;
    }
    
    public boolean isPlayerNear(double playerX, double playerY) {
        return playerX >= x && playerX <= x + width &&
               playerY >= y && playerY <= y + height;
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