package src;
import javax.swing.*;

// import src.Game;

import java.io.IOException;

public class Dungeon {
    private RoomData[] rooms;
    private int currentRoomId;
    private JFrame frame;
    private MyPanel panel;
    private boolean accessTreasury;
    private int health;
    private int strength;
    
    public Dungeon() throws IOException {
        initializeRooms();
        currentRoomId = 0;
        health = 10;
        strength = 0;
        accessTreasury = false;
        
        // Создаем окно и панель
        panel = new MyPanel(this); // все картинки/функции для обновления главные вызываются отсюда
        frame = new JFrame("Alex Dungeon");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    private void initializeRooms() {
        rooms = new RoomData[6];
        
        // первая комната, дверь справа только
        rooms[0] = new RoomData(PathFinder.findFile("misc/Rooms/Room0.png"), 200, 200);
        rooms[0].addDoor(new Door(175, 50, 35, 40, 1)); // первый уровень
        rooms[0].addDoor(new Door(175, 310, 40, 40, 4)); // комната босса
        rooms[0].addDoor(new Door(50, 180, 40, 40, 5)); // сокровищница
        rooms[0].addKey();

        // вторая комната
        rooms[1] = new RoomData(PathFinder.findFile("misc/Rooms/Room1.png"), 200, 200);
        rooms[1].addDoor(new Door(175, 310, 40, 40, 0)); // дом
        rooms[1].addDoor(new Door(300, 200, 35, 40, 2)); // второй уровень
        rooms[1].addKey();
        
        // третья комната
        rooms[2] = new RoomData(PathFinder.findFile("misc/Rooms/Room2.png"), 200, 200);
        rooms[2].addDoor(new Door(175, 50, 40, 40, 1)); // первый уровень
        rooms[2].addDoor(new Door(300, 200, 35, 40, 3)); // третий уровень
        rooms[2].addKey();

        // четвертая комната
        rooms[3] = new RoomData(PathFinder.findFile("misc/Rooms/Room3.png"), 200, 200);
        rooms[3].addDoor(new Door(50, 180, 40, 40, 2)); // второй уровень
        rooms[3].addDoor(new Door(175, 310, 35, 40, 4)); // босс
        rooms[3].addKey();

        // пятая комната
        rooms[4] = new RoomData(PathFinder.findFile("misc/Rooms/Room4.png"), 200, 200);
        rooms[4].addDoor(new Door(300, 200, 40, 40, 3)); // третий уровень
        rooms[4].addDoor(new Door(175, 50, 35, 40, 0)); // дом
        rooms[4].addKey();
        
        // шестая комната
        rooms[5] = new RoomData(PathFinder.findFile("misc/Rooms/Room5.png"), 200, 200);
        rooms[5].addDoor(new Door(300, 200, 40, 40, 0)); // дом
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
    
    public void changeRoom(int newRoomId) throws IOException {
        rooms[currentRoomId].setVisited(true);
        
        if ((newRoomId == 5 && accessTreasury) || rooms[newRoomId].isVisited()) {
            currentRoomId = newRoomId;
            panel.loadCurrentRoom();
        } else {
            int lvl = currentRoomId + 1;
            Game game = new Game(1, 10,0);
            boolean b = game.GameControl();
            if (b) {
                currentRoomId = newRoomId;
                panel.loadCurrentRoom();
            }
        }        
    }
    
    public int getCurrentRoomId() {
        return currentRoomId;
    }
    
    public void start() throws InterruptedException {
        // это главный цикл обработки
        while (true) {
            frame.repaint();
            panel.updateWorldPhysics();
            Thread.sleep(20);
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Dungeon dungeon = new Dungeon();
        dungeon.start();
    }
}