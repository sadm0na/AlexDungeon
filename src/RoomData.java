package src;
import java.util.ArrayList;
import java.util.List;

public class RoomData { // сюда надо будет засунуть и сундуки и ключики
    private String backgroundPath;
    private double playerStartX;
    private double playerStartY;
    private List<Door> doors;
    private Key key;
    private boolean visited;
    
    public RoomData(String backgroundPath, double startX, double startY) {
        this.backgroundPath = backgroundPath;
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

    public Key getKey() {
        return key;
    }
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited(boolean visited) { // посетили комнату только когда карточная игра пройдена
        this.visited = visited;
    }

    // рандомная координата вблизи стены
    private int[] getRandomKeyPosition() { // {320, 325}
        int[][][] walls = {{{50, 55}, {55, 320}}, {{55, 320}, {50, 55}}, {{320, 325}, {55, 320}}, {{55, 320}, {320, 325}}}; // когда размеры фрема поменяются это надо будет изменить
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
