package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

//import static src.Sizes.border;

import java.awt.*;

import java.util.List;

public class MyPanel extends JPanel implements KeyEventDispatcher, Sizes {
    // Image scaleImage;  вот эти две переменные заменяю на следующие
    // Image scaleBack;
    // кэшируем масштабированное изображение и текстуру фона
    private Image scaledRoomImage; // комната
    private Image backgroundTexture; // за комнатой
    private Image scaledMiniMap; 

    private Dungeon dungeon;
    private Alex alex;
    
    private boolean eKeyPressed = false; 
    private long lastFrameTime;

    // Система сообщений
    private String warningMessage = null;
    private long warningStartTime = 0;
    private final long WARNING_DURATION = 3500; // 3.5 секунды

    private String chestMessage = null;
    private long chestStartTime = 0;

    JLabel wIcon;
    Image walls;

    public MyPanel(Dungeon dungeon, boolean loadRoom) throws IOException {
        this.dungeon = dungeon;

        this.setSize((int)WHOLE_SCREEN_W, (int)WHOLE_SCREEN_H);
        this.setPreferredSize(new Dimension((int)WHOLE_SCREEN_W, (int)WHOLE_SCREEN_H));
        
        setDoubleBuffered(true); // в инете пишут ускоряет как то работу, хз как. наверное без разницы есть эта строка или нет

        if (loadRoom) {
            loadCurrentRoom();
        }
        
        this.lastFrameTime = System.currentTimeMillis();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom();
        // все скейлы и тд и тп делаются только 1 раз когда хотим загрузить комнату

        // комната
        BufferedImage roomBackground = ImageIO.read(new File(room.getBackgroundPath()));
        this.scaledRoomImage = roomBackground.getScaledInstance((int) (BACK_WIDTH * WHOLE_SCREEN_W), 
            (int) (BACK_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_SMOOTH); // делаем только 1 раз
        
        // задний фон
        this.backgroundTexture = ImageIO.read(new File(PathFinder.findFile("misc/Rooms/Firelights.png")));
        
        // загрузка и масштабирование мини карты
        BufferedImage miniMap = ImageIO.read(new File(room.getminiMapPath()));      // делаем только 1 раз              
        this.scaledMiniMap = miniMap.getScaledInstance(
            (int) (MINI_MAP_WIDTH *  WHOLE_SCREEN_W), (int) (MINI_MAP_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_SMOOTH);
        
        if (alex == null) {
            alex = new Alex(room.getPlayerStartX(), room.getPlayerStartY());
        } else {
            alex.setPosition(room.getPlayerStartX(), room.getPlayerStartY());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       

        
        // проблема главная была в том что здесь использовался getScaledInstance(каждый кадр работа с размером изображения), а также чтение файла ImageIcon (многоразовое обращение к файловой системе)

        // Рисуем из КЭША 
        if (backgroundTexture != null) {
            g.drawImage(backgroundTexture, 0, 0, screenSize.width, screenSize.height, null);
        }

        //room
        if (scaledRoomImage != null) {
            g.drawImage(scaledRoomImage, (int) (WHOLE_SCREEN_W * BORDER), 
                 (int) (WHOLE_SCREEN_H * BORDER), null);
        }
        
        //minimap
        if (scaledMiniMap != null) {                                   

            g.drawImage(scaledMiniMap, (int) (WHOLE_SCREEN_W * MINI_MAP_X), 
                (int) (WHOLE_SCREEN_H * MINI_MAP_Y), null);
        }


        //this.add(wIcon);

        drawObjects(g);
        alex.draw(g); // алекс поверх предметов должен ходить

        // проверить близость к предметам
        checkProximities(g);
        showMessages(g);
    }

    private void drawObjects(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        Key key = room.getKey();

        for (Door door : room.getDoors()) {
            if (door.image != null) {
                g.drawImage(door.image, (int)(WHOLE_SCREEN_W * door.getX()), (int)(WHOLE_SCREEN_H * door.getY()), null);
            }
        }

        if (key != null && !key.isKeyCollected()) {
            g.drawImage(key.image, (int)(WHOLE_SCREEN_W * key.getX()), (int)(WHOLE_SCREEN_H * key.getY()), null, null);
        }

        for (Chest chest : room.getChest()) {
            if (chest != null) {
                g.drawImage(chest.getImage(), (int)(WHOLE_SCREEN_W * chest.getX()), (int)(WHOLE_SCREEN_H * chest.getY()), null, null);
            }   
        }
    }
    
    private void checkProximities(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press E", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
                break;
            }
        }

        Key key = room.getKey();
        if (key != null && !key.isKeyCollected() && key.isPlayerNear(alex.getX(), alex.getY())) {
            Messages.drawSpeechBubble(g, "Press F", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
        }

        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected() &&  chest.isPlayerNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press K to open the chest", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
            }
        }
    }

    
    public void setChestMessage(String message) {
        this.chestMessage = message;
        this.chestStartTime = System.currentTimeMillis();
    }
    
    public void setWarningMessage(String message) {
        this.warningMessage = message;
        this.warningStartTime = System.currentTimeMillis();
    }

    private void updateMessages() {
        long currentTime = System.currentTimeMillis();
        
        if (chestMessage != null && currentTime - chestStartTime > WARNING_DURATION) {
            chestMessage = null;
        }
        if (warningMessage != null && currentTime - warningStartTime > WARNING_DURATION) {
            warningMessage = null;
        }
    }

    public void showMessages(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        if (chestMessage != null) {
            Messages.showMessage(g, chestMessage, screenSize.width, screenSize.height, 1); // снизу
        }
        if (warningMessage != null) {
            Messages.showMessage(g, warningMessage, screenSize.width, screenSize.height, 0); // сверху
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        int keyCode = key.getKeyCode();
        RoomData room = dungeon.getCurrentRoom();
        int roomID = dungeon.getCurrentRoomId();
        Key roomKey = room.getKey();
        List<Chest> roomChests = room.getChest();
        
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
                    if (roomKey != null && roomKey.isPlayerNear(alex.getX(), alex.getY())) {
                        roomKey.collectKey();
                        if (roomID == 4) {
                            dungeon.makeTreasuryAvalilable();
                        }
                    }
                    break;
                case KeyEvent.VK_K:
                    for (Chest roomChest : roomChests) {
                        if (roomChest != null && roomChest.isPlayerNear(alex.getX(), alex.getY())) {
                            roomChest.collectChest(dungeon.getFrame());
                            dungeon.changeHP(roomChest);
                        }
                    }
                    break;
                case KeyEvent.VK_SHIFT:
                    alex.setRunningSpeed(0.00030);
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
                case KeyEvent.VK_K:
                    eKeyPressed = false;
                    break;
                case KeyEvent.VK_SHIFT:
                    alex.setRunningSpeed(0.00015);
                    break;
            }
        }
        
        return false;
    }
    
    void updateWorldPhysics() { // главный двигатель изменения кадров
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastFrameTime;
        
        alex.update(timeDifference); // передвижение перса
        updateMessages();
        
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

                // говнокод. упростить. (не считая перрвый if)
                if (nextRoomId == 5 && !dungeon.isTreasuryAvailable()) {
                    setWarningMessage("Kill the boss to unlock treasury!");
                    return;
                } else if ((nextRoomId == 4 && roomId == 0) && !dungeon.getSpecificRoom(nextRoomId).getKey().isKeyCollected()) { // переход между боссом и домом
                    setWarningMessage("Win other lvl's to unlock boss fight!");
                    return;
                } else if ((nextRoomId > roomId || nextRoomId == 0 && roomId == 4) && !room.getKey().isKeyCollected()) { 
                    setWarningMessage("Grab the key!");
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