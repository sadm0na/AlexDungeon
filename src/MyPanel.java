package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;

/**
 * Main game panel handling rendering, input, and game state updates.
 */
public class MyPanel extends JPanel implements KeyEventDispatcher, Sizes {
    private Image scaledRoomImage;      // Current room image
    private Image backgroundTexture;    // Background behind room
    private Image scaledMiniMap;        // Mini map image

    private Dungeon dungeon;
    private Alex alex;
    
    private boolean eKeyPressed = false; 
    private long lastFrameTime;

    // Variables for managing UI warnings and messages for player
    private String warningMessage = null;
    private long warningStartTime = 0;
    private final long WARNING_DURATION = 3500; 

    private String chestMessage = null;
    private long chestStartTime = 0;

    public MyPanel(Dungeon dungeon, boolean loadRoom) throws IOException {
        this.dungeon = dungeon;

        this.setSize((int)WHOLE_SCREEN_W, (int)WHOLE_SCREEN_H);
        this.setPreferredSize(new Dimension((int)WHOLE_SCREEN_W, (int)WHOLE_SCREEN_H));
        
        setDoubleBuffered(true);

        if (loadRoom) {
            loadCurrentRoom();
        }
        
        this.lastFrameTime = System.currentTimeMillis();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
    }
    
    /**
     * Loads and scales images for the current room.
     */
    public void loadCurrentRoom() throws IOException {
        RoomData room = dungeon.getCurrentRoom();

        // Load and scale room background
        BufferedImage roomBackground = ImageIO.read(new File(room.getBackgroundPath()));
        this.scaledRoomImage = roomBackground.getScaledInstance((int) (BACK_WIDTH * WHOLE_SCREEN_W), 
            (int) (BACK_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_SMOOTH);
        
        // Load background texture
        this.backgroundTexture = ImageIO.read(new File(PathFinder.findFile("misc/Rooms/Firelights.png")));
        
        // Load and scale mini map
        BufferedImage miniMap = ImageIO.read(new File(room.getminiMapPath()));                  
        this.scaledMiniMap = miniMap.getScaledInstance(
            (int) (MINI_MAP_WIDTH *  WHOLE_SCREEN_W), (int) (MINI_MAP_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_SMOOTH);
        
        // Initialize or reposition Alex
        if (alex == null) {
            alex = new Alex(room.getPlayerStartX(), room.getPlayerStartY());
        } else {
            alex.setPosition(room.getPlayerStartX(), room.getPlayerStartY());
        }
    }
    
    /**
     * Main rendering method - draws all game elements in correct order.
     * Order: room background => room => mini map => interactive objects => Alex => hints and warnings
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Draw background
        if (backgroundTexture != null) {
            g.drawImage(backgroundTexture, 0, 0, screenSize.width, screenSize.height, null);
        }

        // Draw current room
        if (scaledRoomImage != null) {
            g.drawImage(scaledRoomImage, (int) (WHOLE_SCREEN_W * BORDER), 
                 (int) (WHOLE_SCREEN_H * BORDER), null);
        }
        
        // Draw mini map in corner
        if (scaledMiniMap != null) {                                   
            g.drawImage(scaledMiniMap, (int) (WHOLE_SCREEN_W * MINI_MAP_X), 
                (int) (WHOLE_SCREEN_H * MINI_MAP_Y), null);
        }

        drawObjects(g);
        alex.draw(g); // Draw Alex on top of objects

        checkProximities(g);
        showMessages(g);
    }

    /**
     * Draws all interactive objects in the room.
     */
    private void drawObjects(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        Key key = room.getKey();

        // Draw all doors in the room
        for (Door door : room.getDoors()) {
            if (door.image != null) {
                g.drawImage(door.image, (int)(WHOLE_SCREEN_W * door.getX()), (int)(WHOLE_SCREEN_H * door.getY()), null);
            }
        }

        // Draw key if not collected
        if (key != null && !key.isKeyCollected()) {
            g.drawImage(key.image, (int)(WHOLE_SCREEN_W * key.getX()), (int)(WHOLE_SCREEN_H * key.getY()), null, null);
        }

        // Draw all chests in the room
        for (Chest chest : room.getChest()) {
            if (chest != null) {
                g.drawImage(chest.getImage(), (int)(WHOLE_SCREEN_W * chest.getX()), (int)(WHOLE_SCREEN_H * chest.getY()), null, null);
            }   
        }
    }
    
    /**
     * Checks proximity to interactive objects.
     * Shows hints on what key to press to interact with them.
     */
    private void checkProximities(Graphics g) {
        RoomData room = dungeon.getCurrentRoom();
        
        // Check door proximity
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press E", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
                break;
            }
        }

        // Check key proximity
        Key key = room.getKey();
        if (key != null && !key.isKeyCollected() && key.isPlayerNear(alex.getX(), alex.getY())) {
            Messages.drawSpeechBubble(g, "Press F", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
        }

        // Check chest proximity
        for (Chest chest : room.getChest()) {
            if (chest != null && !chest.isChestCollected() &&  chest.isPlayerNear(alex.getX(), alex.getY())) {
                Messages.drawSpeechBubble(g, "Press K to open the chest", (int)(WHOLE_SCREEN_W * (alex.getX() + DIALOG_PLUS_X)), (int)(alex.getY() * WHOLE_SCREEN_H));
            }
        }
    }

    /**
     * Sets chest message to display at bottom of screen.
     */
    public void setChestMessage(String message) {
        this.chestMessage = message;
        this.chestStartTime = System.currentTimeMillis();
    }
    
    /**
     * Sets warning message to display at top of screen.
     */
    public void setWarningMessage(String message) {
        this.warningMessage = message;
        this.warningStartTime = System.currentTimeMillis();
    }

    /**
     * Clears expired messages from display.
     */
    private void updateMessages() {
        long currentTime = System.currentTimeMillis();
        
        if (chestMessage != null && currentTime - chestStartTime > WARNING_DURATION) {
            chestMessage = null;
        }
        if (warningMessage != null && currentTime - warningStartTime > WARNING_DURATION) {
            warningMessage = null;
        }
    }

    /**
     * Displays active messages on screen.
     */
    public void showMessages(Graphics g) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int mode = 0; // 0 - top, 1 - bottom, 2 - special for winning game
        
        if (chestMessage != null) {
            if (chestMessage.equals("You have won the game!")) {
                mode = 2;
            } else {
                mode = 1;
            }
            Messages.showMessage(g, chestMessage, screenSize.width, screenSize.height, mode); // bottom message
        }
        if (warningMessage != null) {
            mode = 0;
            Messages.showMessage(g, warningMessage, screenSize.width, screenSize.height, mode); // top message
        }
    }

    /**
     * Handles keyboard input for movement and interactions.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent key) {
        int keyCode = key.getKeyCode();
        RoomData room = dungeon.getCurrentRoom();
        int roomID = dungeon.getCurrentRoomId();
        Key roomKey = room.getKey();
        List<Chest> roomChests = room.getChest();
        
        // Handle key press events
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
                    // Collect key if nearby
                    if (roomKey != null && roomKey.isPlayerNear(alex.getX(), alex.getY())) {
                        roomKey.collectKey();
                        if (roomID == 4) {
                            dungeon.makeTreasuryAvalilable();
                        }
                    }
                    break;
                case KeyEvent.VK_K:
                    // Open chest if nearby
                    for (Chest roomChest : roomChests) {
                        if (roomChest != null && roomChest.isPlayerNear(alex.getX(), alex.getY())) {
                            roomChest.collectChest(dungeon.getFrame());
                            dungeon.changeHP(roomChest);
                        }
                    }
                    break;
                case KeyEvent.VK_SHIFT:
                    alex.setRunningSpeed(0.00030); // speed changes when shift is pressed
                    break;
            }
        }
        
        // Handle key release events
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
    
    /**
     * Main game loop update method - called every frame.
     */
    void updateWorldPhysics() {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastFrameTime;
        
        alex.update(timeDifference);
        updateMessages();
        
        if (eKeyPressed) {
            checkRoomTransition();
        }
        
        lastFrameTime = currentTime;
    }
    
    /**
     * Handles room transitions with validation checks.
     */
    private void checkRoomTransition() {
        RoomData room = dungeon.getCurrentRoom();
        int roomId = dungeon.getCurrentRoomId();

        // Check all doors for proximity and transition conditions
        for (Door door : room.getDoors()) {
            if (door.isObjectNear(alex.getX(), alex.getY())) {
                int nextRoomId = door.getTargetRoomId();

                // Validate room transition conditions. If they're not met, warning message will be set.
                if (nextRoomId == 5 && !dungeon.isTreasuryAvailable()) {
                    setWarningMessage("Kill the boss to unlock treasury!");
                    return;
                } else if ((nextRoomId == 4 && roomId == 0) && !dungeon.getSpecificRoom(nextRoomId).getKey().isKeyCollected()) {
                    setWarningMessage("Win other lvl's to unlock boss fight!");
                    return;
                } else if ((nextRoomId > roomId || nextRoomId == 0 && roomId == 4) && !room.getKey().isKeyCollected()) { 
                    setWarningMessage("Grab the key!");
                    return;
                }

                // Make room transition
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