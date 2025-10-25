package src;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.*;

/**
 * Creates a dungeon.
 * Contains main.
 */

public class Dungeon implements Sizes {
    private RoomData[] rooms;
    private int currentRoomId;
    private JFrame frame;
    private MyPanel panel;
    private boolean accessTreasury;
    private int health = 10;
    private int strength = 0;

    public JFrame getFrame() {
        return frame;
    }
    
    public Dungeon() throws IOException {
        currentRoomId = 0;
        health = 10;
        strength = 0;
        accessTreasury = false;

        panel = new MyPanel(this, false);
        frame = new JFrame("Alex Dungeon");

        initializeRooms(panel); // transfers message to chests in MyPanel
        panel.loadCurrentRoom();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    /**
     * Initializes all rooms in Dungeon, creates them and determines the locations of
     * doors, chests and keys.
     */
    private void initializeRooms(MyPanel panel) {
        rooms = new RoomData[6];
        
        // Zero room (home).
        rooms[0] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map1W.png"));
        rooms[0].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2, 
            BORDER_UP, 1)); // Leads to the first room.
        rooms[0].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2, BORDER_DOWN - DOOR_HEIGHT,
             4)); // Leads to the fourth room (the one with the boss).
        rooms[0].addDoor(new Door(BORDER_LEFT, CENTER_VERTICAL - DOOR_HEIGHT / 2, 
            5)); // Leads to the fifth room (the room with a treasure).
        rooms[0].addKey();
        rooms[0].addChest(CENTER_HORIZONTAL, CENTER_VERTICAL, 0, 0, panel);

        // First room.
        rooms[1] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map2W.png"));
        rooms[1].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2, BORDER_DOWN - DOOR_HEIGHT,
             0)); // Leads to the zero room (home).
        rooms[1].addDoor(new Door(BORDER_REIGHT - DOOR_WIDTH, CENTER_VERTICAL - DOOR_HEIGHT / 2, 
             2)); // Leads to the second room.
        rooms[1].addKey();
        rooms[1].addChest(ONE_THIRD_HORIZONTAL, CENTER_VERTICAL, 1, 3, panel);
        rooms[1].addChest(SECOND_THIRD_HORIZONTAL, ONE_THIRD_VERTICAL, 2, 3, panel);
        
        // Second room.
        rooms[2] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map3W.png"));
        rooms[2].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2, 
            BORDER_UP, 1)); // Leads to the first room.
        rooms[2].addDoor(new Door(BORDER_REIGHT - DOOR_WIDTH,  CENTER_VERTICAL - DOOR_HEIGHT / 2, 
             3)); // Leads to the third room.
        rooms[2].addKey();
        rooms[2].addChest(SECOND_THIRD_VERTICAL, SECOND_THIRD_VERTICAL, 1, 4, panel);
        rooms[2].addChest(CENTER_HORIZONTAL, ONE_THIRD_VERTICAL, 2, 2, panel);

        // Third room.
        rooms[3] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map4W.png"));
        rooms[3].addDoor(new Door(BORDER_LEFT,  CENTER_VERTICAL - DOOR_HEIGHT / 2,
              2)); // Leads to the second room.
        rooms[3].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2, BORDER_DOWN - DOOR_HEIGHT, 
              4)); //  Leads to the fourth room (the one with the boss).
        rooms[3].addKey();
        rooms[3].addChest(SECOND_THIRD_VERTICAL, CENTER_VERTICAL, 1, 5, panel);
        rooms[3].addChest(ONE_THIRD_HORIZONTAL, SECOND_THIRD_VERTICAL, 2, 3, panel);

        // Fourth room (the one with the boss)
        rooms[4] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map5W.png"));
        rooms[4].addDoor(new Door(BORDER_REIGHT - DOOR_WIDTH, CENTER_VERTICAL - DOOR_HEIGHT / 2,
             3)); // Leads to the third room.
        rooms[4].addDoor(new Door(CENTER_HORIZONTAL - DOOR_WIDTH / 2,
             BORDER_UP, 0)); // Leads to the zero room (home).
        rooms[4].addKey();
        
        // Fifth room (the room with a treasure).
        rooms[5] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map6W.png"));
        rooms[5].addDoor(new Door(BORDER_REIGHT - DOOR_WIDTH, CENTER_VERTICAL - DOOR_HEIGHT / 2,
             0)); // Leads to the zero room (home).
        rooms[5].addChest(CENTER_HORIZONTAL, CENTER_VERTICAL, 3, 1, panel);
    }

    public boolean isTreasuryAvailable() {
        return accessTreasury;
    }

    public void makeTreasuryAvalilable() {
        accessTreasury = true;
    }
    
    public RoomData getCurrentRoom() {
        return rooms[currentRoomId];
    }

    public RoomData getSpecificRoom(int id) {
        return rooms[id];
    }
    
    /**
     * For the chest that changes Alex hp.
     */
    public void changeHP(Chest h) {
        health += h.getHealth();
        strength += h.getStrength();
    }

    /**
     * 
     */
    public void changeRoom(int newRoomId) throws IOException {
        rooms[currentRoomId].setVisited(true);
        
        if ((newRoomId == 5 && accessTreasury) || rooms[newRoomId].isVisited()) {
            currentRoomId = newRoomId;
            panel.loadCurrentRoom();
        } else {
            int lvl = currentRoomId + 1;
            Game game = new Game(lvl, health, strength);
            boolean b = game.gameControl();
            if (b) {
                currentRoomId = newRoomId;
                panel.loadCurrentRoom();
            }
        }        
    }
    
    public int getCurrentRoomId() {
        return currentRoomId;
    }
    
    /**
     * 
     */
    public void start() throws InterruptedException {
        while (true) { // Main processing cycle.
            frame.repaint();
            panel.updateWorldPhysics();
            Thread.sleep(16); // Restriction of FPS.
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Dungeon dungeon = new Dungeon();
        dungeon.start();
    }
}