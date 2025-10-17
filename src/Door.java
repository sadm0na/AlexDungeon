package src;
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
    
    public boolean isObjectNear(double objectX, double objectY) { // дубликаты часто используются. надо наверное в отдельный класс items вынести эти функции
        return objectX >= x && objectX <= x + width &&
               objectY >= y && objectY <= y + height;
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