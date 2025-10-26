package src;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room in the dungeon with doors, keys,chests 
 * and paths to background, room, minimap images.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */
public class RoomData implements Sizes {
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
        this.playerStartX = ALEX_START_X;
        this.playerStartY = ALEX_START_Y;
        this.doors = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.visited = false;
    }
    
    public void addDoor(Door door) {
        doors.add(door);
    }

    /**
     * Adds a key at random position near walls, avoiding doors.
     */
    public void addKey() {
        double[] position = getRandomKeyPosition();
        this.key = new Key(position[0], position[1]);
    }
    
    public void addChest(double x, double y, int type, int h, MyPanel panel) {
        chests.add(new Chest(x, y, type, h, panel));
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
    
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Returns random key position near walls while avoiding door areas.
     */
    private double[] getRandomKeyPosition() {
        // Set wall boundaries for x and y: left, top, right, bottom walls
        double[][][] walls = {
            {{BORDER_LEFT, BORDER_LEFT_INNER - KEY_WIDTH}, {BORDER_UP, BORDER_DOWN - KEY_HEIGHT}},
            {{BORDER_LEFT, BORDER_REIGHT - KEY_WIDTH}, {BORDER_UP, BORDER_UP_INNER - KEY_HEIGHT}}, 
            {{BORDER_REIGHT, BORDER_REIGHT_INNER - KEY_WIDTH}, {BORDER_UP, 
                BORDER_DOWN - KEY_HEIGHT}}, 
            {{BORDER_LEFT, BORDER_REIGHT - KEY_WIDTH}, {BORDER_DOWN, 
                BORDER_DOWN_INNER - KEY_HEIGHT}}
        };
        
        boolean isSpaceAvailable = false;
        double[] keyPosition = new double[2];

        while (!isSpaceAvailable) {
            isSpaceAvailable = true;
            
            // Select random wall
            int wallNumber = (int) (Math.random() * 4);
            double xMin = walls[wallNumber][0][0];
            double xMax = walls[wallNumber][0][1];
            double yMin = walls[wallNumber][1][0];
            double yMax = walls[wallNumber][1][1];

            // Generate random position on selected wall
            keyPosition[0] = rnd(Math.min(xMin, xMax), Math.max(xMax, xMin));
            keyPosition[1] = rnd(Math.min(yMin, yMax), Math.max(yMax, yMin));

            // Check if position conflicts with any door
            for (Door door: doors) {
                if (door.isObjectNear(keyPosition[0], keyPosition[1])) {
                    isSpaceAvailable = false;
                    break;
                }
            }
        }

        return keyPosition;
    }

    private static double rnd(double min, double max) {
        return (Math.random() * (max - min) + min);
    }
}