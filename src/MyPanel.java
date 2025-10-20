package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

public class MyPanel extends JPanel implements KeyEventDispatcher {
    private Alex alex;
    private long lastFrameTime;
    private BufferedImage backgroundImage; // в RoomData путь будет храниться
    private Dungeon dungeon;
    private boolean eKeyPressed = false; // переход в дверь через нажатие e
    String errorMessage;
    JLabel wIcon;
    Image scaleImage;
    Image scaleBack;
    Image walls;

    public MyPanel(Dungeon dungeon) throws IOException {
        this.dungeon = dungeon;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        this.setPreferredSize(screenSize);
        //this.setPreferredSize(new Dimension(700, 700));
        
        loadCurrentRoom(); // передаем данные данной комнаты и рисуем ее
        
        this.lastFrameTime = System.currentTimeMillis(); // сохраняем время последнего фрейма (чтобы движение из-за задержек с ума не сошло, время между обновлением кадров учитываем)
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom(); // прогрузка комнаты
        //this.backgroundImage = ImageIO.read(new File(room.getBackgroundPath()));
        scaleBack = ImageIO.read(new File(room.getBackgroundPath()));

        int miniMapSize = 250;
        int minimapW = miniMapSize + (int)((double)miniMapSize * 0.4);
        int minimapH = miniMapSize;

        BufferedImage wPic = ImageIO.read(new File(room.getminiMapPath()));
        
        ImageIcon icon = new ImageIcon(wPic);
        scaleImage = icon.getImage().getScaledInstance(minimapW, minimapH,Image.SCALE_DEFAULT);
        wIcon = new JLabel(new ImageIcon(scaleImage));
        wIcon.setBounds(300,300, minimapW, minimapH);
        
        //wIcon.setBounds(300,300, 30, 30);
        
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
        
        

        // отладка - координаты алекса
        

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        
        int border = 100;

        int miniMapSize = 200;
        int minimapW = miniMapSize + (int)((double)miniMapSize * 0.3);
        int minimapH = miniMapSize;

        //int backSize = 800;
        int backW = (int)screenSize.getWidth() - border * 2;
        int backH = (int)screenSize.getHeight() - border - minimapH;

        scaleBack = scaleBack.getScaledInstance(backW, backH ,Image.SCALE_DEFAULT);

        ImageIcon ii = new ImageIcon(PathFinder.findFile("misc/Rooms/Back3.png"));

        g.drawImage(ii.getImage(), 0,0, (int)screenSize.getWidth(),(int)screenSize.getHeight(), null, null);

        g.drawImage(scaleBack,border - 10, border - 10, backW,backH ,null,null);

        g.drawString("X: " + (int)alex.getX() + " Y: " + (int)alex.getY(), 10, 20);

        
        
        
        g.drawImage(scaleImage, (int)screenSize.getWidth() - minimapW,
            (int)screenSize.getHeight() - minimapH - 10,minimapW,minimapH - 10,null,null);


        //this.add(wIcon);

        alex.draw(g); // алекс
    
        // Отладка - дверь красной рисуем
        RoomData room = dungeon.getCurrentRoom();
        g.setColor(java.awt.Color.RED);
        for (Door door : room.getDoors()) {
            
            g.drawImage(door.image, (int)door.getX(), (int)door.getY(), null, null);
            //g.drawRect((int)door.getX(), (int)door.getY(), 40, 40);
        }

        // отладка ключа (желтый) (вместо этого надо будет картинку вывести)
        Key key = room.getKey();
        if (key != null && !key.isKeyCollected()) {
            g.setColor(java.awt.Color.YELLOW);
            g.drawImage(key.image, (int)key.getX(), (int)key.getY(), null, null);
           // g.drawRect((int)key.getX(), (int)key.getY(), 15, 15);
        }

        for (Chest chest : room.getChest()) {
            //chest = room.getChest();
            if (chest != null && !chest.isChestCollected()) {
                g.setColor(java.awt.Color.BLUE);
                g.drawImage(chest.image, (int)chest.getX(), (int)chest.getY(), null, null);
                //g.drawRect((int)chest.getX(), (int)chest.getY(), 45, 45);
            }   
        }
        // если дверь/ключ рядом, появится надпись (добавить сундук. и вообще надо полиморфизм тут или хз)
        checkDoorProximity(g);
        checkKeyProximity(g);
        checkChestProximity(g);

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
        if (key != null && !key.isKeyCollected() && key.isPlayerNear(alex.getX(), alex.getY())) {
            g.drawString("Нажми F", (int)key.getX(), (int)key.getY() - 10);
        }
    }

     private void checkChestProximity(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected() &&  chest.isPlayerNear(alex.getX(), alex.getY())) {
                g.drawString("Press K to open a chest", (int)chest.getX(), (int)chest.getY() - 90);
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