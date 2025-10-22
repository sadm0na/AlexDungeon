package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.*;

import java.util.List;

public class MyPanel extends JPanel implements KeyEventDispatcher {
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
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        this.setPreferredSize(screenSize);
        
        setDoubleBuffered(true); // в инете пишут ускоряет как то работу, хз как. наверное без разницы есть эта строка или нет

        loadCurrentRoom();
        
        this.lastFrameTime = System.currentTimeMillis();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int border = 100;
        int backW = (int)screenSize.getWidth() - border * 2 - 50;
        int backH = (int)screenSize.getHeight() - border - 200;
        
        // все скейлы и тд и тп делаются только 1 раз когда хотим загрузить комнату

        // комната
        BufferedImage roomBackground = ImageIO.read(new File(room.getBackgroundPath()));
        this.scaledRoomImage = roomBackground.getScaledInstance(backW, backH, Image.SCALE_SMOOTH); // делаем только 1 раз
        
        // задний фон
        this.backgroundTexture = ImageIO.read(new File(PathFinder.findFile("misc/Rooms/Firelights.png")));
        
        // загрузка и масштабирование мини карты
        int miniMapSize = 200;
        int miniMapW = miniMapSize + (int)((double)miniMapSize * 0.8); 
        int miniMapH = miniMapSize;  

        BufferedImage miniMap = ImageIO.read(new File(room.getminiMapPath()));      // делаем только 1 раз              
        this.scaledMiniMap = miniMap.getScaledInstance(miniMapW, miniMapH, Image.SCALE_SMOOTH);
        
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
        int border = 100;
        int backW = (int)screenSize.getWidth() - border * 2 - 50;
        
        // проблема главная была в том что здесь использовался getScaledInstance(каждый кадр работа с размером изображения), а также чтение файла ImageIcon (многоразовое обращение к файловой системе)

        // Рисуем из КЭША 
        if (backgroundTexture != null) {
            g.drawImage(backgroundTexture, 0, 0, screenSize.width, screenSize.height, null);
        }
        
        if (scaledRoomImage != null) {
            int roomX = (screenSize.width - backW) / 2;
            g.drawImage(scaledRoomImage, roomX, border - 10, null);
        }
        
        if (scaledMiniMap != null) {
            int miniMapSize = 200;
            int minimapW = miniMapSize + (int)((double)miniMapSize * 0.8);  
            int minimapH = miniMapSize;                                     
            
            int miniMapX = screenSize.width - minimapW - 20;
            int miniMapY = screenSize.height - minimapH - 20;

            g.drawImage(scaledMiniMap, miniMapX, miniMapY, null);
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
                g.setColor(java.awt.Color.RED);
                g.drawImage(door.image, (int)door.getX(), (int)door.getY(), null);
            }
        }
    }

    private void drawKeys(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        Key key = room.getKey();

        if (key != null && !key.isKeyCollected()) {
            g.setColor(java.awt.Color.YELLOW);
            g.drawImage(key.image, (int)key.getX(), (int)key.getY(), null, null);
        }
    }

    private void drawChests(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();

        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected()) {
                g.setColor(java.awt.Color.BLUE);
                g.drawImage(chest.image, (int)chest.getX(), (int)chest.getY(), null, null);
            }   
        }
    }
    
    private void checkProximities(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                g.drawString("Нажми E", (int)door.getX(), (int)door.getY() - 10);
                break;
            }
        }

        Key key = room.getKey();
        if (key != null && !key.isKeyCollected() && key.isPlayerNear(alex.getX(), alex.getY())) {
            g.drawString("Нажми F", (int)key.getX(), (int)key.getY() - 10);
        }

        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected() &&  chest.isPlayerNear(alex.getX(), alex.getY())) {
                g.drawString("Press K to open a chest", (int)chest.getX(), (int)chest.getY() - 20);
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
                    if (roomKey != null) {
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