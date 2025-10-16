import java.util.ArrayList;
import java.util.List;

public class RoomData { // сюда надо будет засунуть и сундуки и ключики
    private String backgroundPath;
    private double playerStartX;
    private double playerStartY;
    private List<Door> doors;
    private boolean visited;
    
    public RoomData(String backgroundPath, double startX, double startY) {
        this.backgroundPath = backgroundPath;
        this.playerStartX = startX;
        this.playerStartY = startY;
        this.doors = new ArrayList<>(); // список дверей. будет по 1 или 2
        this.visited = false;
    }
    
    public void addDoor(Door door) {
        doors.add(door);
    }
    
    public String getBackgroundPath() {
        return backgroundPath;
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
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited(boolean visited) { // посетили комнату только когда карточная игра пройдена
        this.visited = visited;
    }
}
