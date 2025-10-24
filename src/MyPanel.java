package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import static src.Sizes.border;

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

    private String errorMessage; // над этим мне надо поработать

    JLabel wIcon;
    Image walls;

    public MyPanel(Dungeon dungeon) throws IOException {
        this.dungeon = dungeon;

        this.setSize((int)wholeScreenW, (int)wholeScreenH);
        this.setPreferredSize(new Dimension((int)wholeScreenW, (int)wholeScreenH));
        
        setDoubleBuffered(true); // в инете пишут ускоряет как то работу, хз как. наверное без разницы есть эта строка или нет

        loadCurrentRoom();
        
        this.lastFrameTime = System.currentTimeMillis();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom();
        // все скейлы и тд и тп делаются только 1 раз когда хотим загрузить комнату

        // комната
        BufferedImage roomBackground = ImageIO.read(new File(room.getBackgroundPath()));
        this.scaledRoomImage = roomBackground.getScaledInstance((int) (backW * wholeScreenW), 
            (int) (backH * wholeScreenH), Image.SCALE_SMOOTH); // делаем только 1 раз
        
        // задний фон
        this.backgroundTexture = ImageIO.read(new File(PathFinder.findFile("misc/Rooms/Firelights.png")));
        
        // загрузка и масштабирование мини карты
        BufferedImage miniMap = ImageIO.read(new File(room.getminiMapPath()));      // делаем только 1 раз              
        this.scaledMiniMap = miniMap.getScaledInstance(
            (int) (miniMapW *  wholeScreenW), (int) (miniMapH * wholeScreenH), Image.SCALE_SMOOTH);
        
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
            g.drawImage(scaledRoomImage, (int) (wholeScreenW * border), 
                 (int) (wholeScreenH * border), null);
        }
        
        //minimap
        if (scaledMiniMap != null) {                                   

            g.drawImage(scaledMiniMap, (int) (wholeScreenW * miniMapX), 
                (int) (wholeScreenH * miniMapY), null);
        }


        //this.add(wIcon);
    
        // Отрисовка компонентов
        drawDoors(g);
        drawKeys(g);
        drawChests(g);

        alex.draw(g); // алекс поверх предметов должен ходить

        // проверить близость к предметам
        checkProximities(g);

        // очень важно!
        //showCaptions(g);
    }

    private void drawDoors(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();

        for (Door door : room.getDoors()) {
            if (door.image != null) {
                g.drawImage(door.image, (int)(wholeScreenW * door.getX()), (int)(wholeScreenH * door.getY()), null);
            }
        }
    }

    private void drawKeys(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        Key key = room.getKey();

        if (key != null && !key.isKeyCollected()) {
            g.setColor(java.awt.Color.YELLOW);
            g.drawImage(key.image, (int)(wholeScreenW * key.getX()), (int)(wholeScreenH * key.getY()), null, null);
        }
    }

    private void drawChests(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();

        for (Chest chest : room.getChest()) {
            //!chest.isChestCollected()
            if (chest != null) {
                g.setColor(java.awt.Color.BLUE);
                g.drawImage(chest.image, (int)(wholeScreenW * chest.getX()), (int)(wholeScreenH * chest.getY()), null, null);
            }   
        }
    }
    
    private void checkProximities(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press E", (int)(wholeScreenW * (alex.getX() + dialogPlusX)), (int)(alex.getY() * wholeScreenH));
                break;
            }
        }

        Key key = room.getKey();
        if (key != null && !key.isKeyCollected() && key.isPlayerNear(alex.getX(), alex.getY())) {
            Messages.drawSpeechBubble(g, "Press F", (int)(wholeScreenW * (alex.getX() + dialogPlusX)), (int)(alex.getY() * wholeScreenH));
        }

        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected() &&  chest.isPlayerNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press K to open the chest", (int)(wholeScreenW * (alex.getX() + dialogPlusX)), (int)(alex.getY() * wholeScreenH));
            }
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
                    alex.RunningSpeed = 0.00030;
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
                    alex.RunningSpeed = 0.00010;
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

                // говнокод. упростить. (не считая перрвый if)
                if (nextRoomId == 5 && !dungeon.isTreasuryAvailable()) {
                    return;
                } else if ((nextRoomId == 4 && roomId == 0) && !dungeon.getSpecificRoom(nextRoomId).getKey().isKeyCollected()) { // переход между боссом и домом
                    return;
                } else if ((nextRoomId > roomId || nextRoomId == 0 && roomId == 4) && !room.getKey().isKeyCollected()) { 
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