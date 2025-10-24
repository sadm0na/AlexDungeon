package src;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.IOException;

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
        initializeRooms();
        currentRoomId = 0;
        health = 10;
        strength = 0;
        accessTreasury = false;
        
        // Создаем окно и панель
        panel = new MyPanel(this); // все картинки/функции для обновления главные вызываются отсюда
        frame = new JFrame("Alex Dungeon");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    private void initializeRooms() {
        rooms = new RoomData[6];

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // учитываем центрирование комнаты
       
        
        // первая комната, дверь справа только
        rooms[0] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map1W.png"));
        rooms[0].addDoor(new Door(centerHorizontal - doorWidth / 2, borderUp, 1)); // первый уровень
        rooms[0].addDoor(new Door(centerHorizontal - doorWidth / 2, borderDown - doorHeight,
             4)); // комната босса
        rooms[0].addDoor(new Door(borderLeft, centerVertical - doorHeight / 2, 5)); // сокровищница
        rooms[0].addKey();
        rooms[0].addChest(centerHorizontal, centerVertical, 0, 0);

        // вторая комната
        rooms[1] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map2W.png"));
        rooms[1].addDoor(new Door(centerHorizontal - doorWidth / 2, borderDown - doorHeight,
             0)); // дом
        rooms[1].addDoor(new Door(borderRight - doorWidth, centerVertical - doorHeight / 2, 
             2)); // второй уровень
        rooms[1].addKey();
        rooms[1].addChest(oneThirdHorizontal, centerVertical,1, 3);
        rooms[1].addChest(secondThirdHorizontal, oneThirdVertical,2, 3);
        
        // третья комната
        rooms[2] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map3W.png"));
        rooms[2].addDoor(new Door(centerHorizontal - doorWidth / 2, 
            borderUp, 1)); // первый уровень
        rooms[2].addDoor(new Door(borderRight - doorWidth,  centerVertical - doorHeight / 2, 
             3)); // третий уровень
        rooms[2].addKey();
        rooms[2].addChest(secondThirdVertical, secondThirdVertical, 1, 4);
        rooms[2].addChest(centerHorizontal, oneThirdVertical, 2, 2);

        // четвертая комната
        rooms[3] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map4W.png"));
        rooms[3].addDoor(new Door(borderLeft,  centerVertical - doorHeight / 2,
              2)); // второй уровень
        rooms[3].addDoor(new Door(centerHorizontal - doorWidth / 2, borderDown - doorHeight, 
              4)); // босс
        rooms[3].addKey();
        rooms[3].addChest(secondThirdVertical, centerVertical, 1, 5);
        rooms[3].addChest(oneThirdHorizontal, secondThirdVertical, 2, 3);

        // пятая комната
        rooms[4] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map5W.png"));
        rooms[4].addDoor(new Door(borderRight - doorWidth, centerVertical - doorHeight / 2,
             3)); // третий уровень
        rooms[4].addDoor(new Door(centerHorizontal - doorWidth / 2,
             borderUp, 0)); // дом
        rooms[4].addKey();
        //rooms[4].addChest(centerHorizontal, centerVertical, 1, 1);
        
        // шестая комната
        rooms[5] = new RoomData(PathFinder.findFile("misc/Rooms/testRoom.png"),
            PathFinder.findFile("misc/MiniMap/Map6W.png"));
        rooms[5].addDoor(new Door(borderRight - doorWidth, centerVertical - doorHeight / 2,
             0)); // дом
        rooms[5].addChest(centerHorizontal, centerVertical, 3 ,1);
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
    
    public void changeHP(Chest h) {
        health += h.getHealth();
        strength += h.getStrength();
    }


    public void changeRoom(int newRoomId) throws IOException {
        rooms[currentRoomId].setVisited(true);
        
        if ((newRoomId == 5 && accessTreasury) || rooms[newRoomId].isVisited()) {
            currentRoomId = newRoomId;
            panel.loadCurrentRoom();
        } else {
            int lvl = currentRoomId + 1;
            Game game = new Game(lvl, health, strength);
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
            Thread.sleep(16); // ограничение фпс обязательно, а то процессор на полную катку будет работать что не очень безопасно и правильно, около 60 кадров.
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        Dungeon dungeon = new Dungeon();
        dungeon.start();
    }
}