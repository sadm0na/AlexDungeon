import javax.swing.*;

import java.io.IOException;

public class Dungeon {
    private RoomData[] rooms;
    private int currentRoomId;
    private JFrame frame;
    private MyPanel panel;
    
    public Dungeon() throws IOException {
        initializeRooms();
        currentRoomId = 0;
        
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
        rooms = new RoomData[3];
        
        // первая комната, дверь справа только
        rooms[0] = new RoomData("Room1.png", 200, 200);
        rooms[0].addDoor(new Door(300, 180, 35, 40, 1)); // дверь справа
        
        // вторая. дверь справа и слева.
        rooms[1] = new RoomData("Room2.png", 200, 200);
        rooms[1].addDoor(new Door(50, 180, 40, 40, 0)); // дверь слева
        rooms[1].addDoor(new Door(300, 180, 35, 40, 2)); // дверь справа
        
        // третьякомната - дверь только слева
        rooms[2] = new RoomData("Room3.png", 200, 200);
        rooms[2].addDoor(new Door(50, 180, 40, 40, 1)); // дверь слева обратно
    }
    
    public RoomData getCurrentRoom() {
        return rooms[currentRoomId];
    }
    
    public void changeRoom(int newRoomId) throws IOException {
        rooms[currentRoomId].setVisited(true); // у каждой комнаты будет отдельное поле, посетили ее еще или нет (надо будет сделать так чтобы статус менялся после прохождения карточной игры)
        Game game = new Game(1,10,0);
        boolean b = game.GameControl();
        System.out.println(b);
        if (b) {
            currentRoomId = newRoomId;
            panel.loadCurrentRoom();
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