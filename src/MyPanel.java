package src;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

public class MyPanel extends JPanel implements KeyEventDispatcher {
    private Alex alex;
    private long lastFrameTime;
    private BufferedImage backgroundImage; // в RoomData путь будет храниться
    private Dungeon dungeon;
    private boolean eKeyPressed = false; // переход в дверь через нажатие e
    String errorMessage;
    
    public MyPanel(Dungeon dungeon) throws IOException {
        this.dungeon = dungeon;
        this.setPreferredSize(new Dimension(400, 400));
        
        loadCurrentRoom(); // передаем данные данной комнаты и рисуем ее
        
        this.lastFrameTime = System.currentTimeMillis(); // сохраняем время последнего фрейма (чтобы движение из-за задержек с ума не сошло, время между обновлением кадров учитываем)
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom(); // прогрузка комнаты
        this.backgroundImage = ImageIO.read(new File(room.getBackgroundPath()));
        
        if (alex == null) {
            alex = new Alex(room.getPlayerStartX(), room.getPlayerStartY());
        } else {
            alex.setPosition(room.getPlayerStartX(), room.getPlayerStartY());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null); // фон
        }
        
        alex.draw(g); // алекс

        // отладка - координаты алекса
        g.drawString("X: " + (int)alex.getX() + " Y: " + (int)alex.getY(), 10, 20);
        
        
        // Отладка - дверь красной рисуем
        RoomData room = dungeon.getCurrentRoom();
        g.setColor(java.awt.Color.RED);
        for (Door door : room.getDoors()) {
            g.drawRect((int)door.getX(), (int)door.getY(), 40, 40);
        }

        // отладка ключа (желтый) (вместо этого надо будет картинку вывести)
        Key key = room.getKey();
        if (!key.isKeyCollected()) {
            g.setColor(java.awt.Color.YELLOW);
            g.drawRect((int)key.getX(), (int)key.getY(), 15, 15);
        }

        // если дверь/ключ рядом, появится надпись (добавить сундук. и вообще надо полиморфизм тут или хз)
        checkDoorProximity(g);
        checkKeyProximity(g);

        // вывод предупреждения при каком либо действии..
        //showCaptions(g);
    }
    
    private void checkDoorProximity(Graphics g) { // надо понять как очищать drawString (может какой то метод из Swing'a альтернативный. либо найти как с awt это сделать)
        RoomData room = dungeon.getCurrentRoom();
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                g.drawString("Нажми E", (int)door.getX(), (int)door.getY() - 10);
                break;
            }
        }
    }

    private void checkKeyProximity(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        Key key = room.getKey();
        if (key.isPlayerNear(alex.getX(), alex.getY())) {
            g.drawString("Нажми F", (int)key.getX(), (int)key.getY() - 10);
        }
    }

    /* 
    public void showCaptions(Graphics g) { // надо понять как на время вывести это. по истечении времени сообщения опять null должно стать
        
    }
    */
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        int keyCode = key.getKeyCode();
        RoomData room = dungeon.getCurrentRoom();
        Key roomKey = room.getKey();
        
        if (key.getID() == KeyEvent.KEY_PRESSED) {
            switch (keyCode) {
                case KeyEvent.VK_A:
                    alex.runLeft();
                    break;
                case KeyEvent.VK_D:
                    alex.runRight();
                    break;
                case KeyEvent.VK_W:
                    alex.runUp();
                    break;
                case KeyEvent.VK_S:
                    alex.runDown();
                    break;
                case KeyEvent.VK_E:
                    eKeyPressed = true;
                    break;
                case KeyEvent.VK_F:
                    roomKey.collectKey();
                    break;

            }
        }
        
        if (key.getID() == KeyEvent.KEY_RELEASED) {
            switch (keyCode) {
                case KeyEvent.VK_A:
                case KeyEvent.VK_D:
                    alex.stopRunningX();
                    break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_S:
                    alex.stopRunningY();
                    break;
                case KeyEvent.VK_E:
                    eKeyPressed = false;
                    break;
            }
        }
        
        return false;
    }
    
    void updateWorldPhysics() { // главный двигатель изменения кадров
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastFrameTime;
        
        alex.update(timeDifference); // передвижение перса
        
        // ароверяем переход в другую комнату
        if (eKeyPressed) {
            checkRoomTransition();
        }
        
        lastFrameTime = currentTime;
    }
    
    private void checkRoomTransition() { // меняем комнату
        RoomData room = dungeon.getCurrentRoom();
        int roomId = dungeon.getCurrentRoomId();

        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                int nextRoomId = door.getTargetRoomId();

                if (nextRoomId > roomId && !room.getKey().isKeyCollected()) {
                    // errorMessage = "Pick up the key!";        чуть позже. надо сделать так чтобы выскакивало предупреждение на условные 10 секунд хз. короче функцию написать для всяких выводов.
                    return;
                }

                try {
                    dungeon.changeRoom(door.getTargetRoomId());
                    eKeyPressed = false;
                    // errorMessage = null; // ??
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}