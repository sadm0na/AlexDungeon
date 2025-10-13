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
    private BufferedImage backgroundImage; // в румдате путь будет храниться
    private Dungeon dungeon;
    private boolean eKeyPressed = false; // переход в дверь через нажатие e
    
    public MyPanel(Dungeon dungeon) throws IOException {
        this.dungeon = dungeon;
        this.setPreferredSize(new Dimension(400, 400));
        
        loadCurrentRoom(); // передаем данные данной комнаты и рисуем ее
        
        this.lastFrameTime = System.currentTimeMillis(); // сохраняем время последнего фрейма (чтобы движение из-за задержек с ума не сошло, время между обновлением кадров учитываем)
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom(); // данная великая комната
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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        
        alex.draw(g);
        
        // если дверь рядом, текстик появится
        checkDoorProximity(g);
    }
    
    private void checkDoorProximity(Graphics g) { // рядом ли дверь? 
        RoomData room = dungeon.getCurrentRoom();
        for (Door door : room.getDoors()) {
            if (door.isPlayerNear(alex.getX(), alex.getY())) {
                g.drawString("Нажми E", (int)door.getX(), (int)door.getY() - 10);
                break;
            }
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        int keyCode = key.getKeyCode();
        
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
        for (Door door : room.getDoors()) {
            if (door.isPlayerNear(alex.getX(), alex.getY())) {
                try {
                    dungeon.changeRoom(door.getTargetRoomId());
                    eKeyPressed = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}