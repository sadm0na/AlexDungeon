package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Class of mini-game.
 * Checks if it is possible to do to the button ones it is hold.
 * Checks if game must be ended (if Alex died or has enought money to win this level).
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */
class Game implements ActionListener, Sizes {
    private Coordinates alexCoor; // Coordinates of Alex Card.
    private ArrayList<Cards> arrayListCards = new ArrayList<Cards>();
    private ArrayList<JButton> arrayList = new ArrayList<JButton>();
    private JFrame frame = new JFrame("minigame");
    private JButton button;
    private int level;
    private int levelMoney; // Money required to pass the level.
    private int maxHp;
    private int strengthPlus; // bonus from the chests in the game
    private volatile int status = 0; // -1 if level losed, 1 if won and 0 if is still played.


    /**
     * Checks if it is possible to do to the button ones it is hold.
     * If yes - goes to it, changes Alex and creates a new random Card if needed.
     * In other case do nothing.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int buttonPressed = Integer.parseInt(
            e.getActionCommand()); // Number of the button which was prassed.
        int x = buttonPressed / 3; // X in two-dimentional space.
        int y = buttonPressed - x * 3; // Y X in two-dimentional space.
        int alexSword = 0 + arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getSword();

        if (alexCoor.getX() == x && Math.abs(y - alexCoor.getY()) == 1 
                || alexCoor.getY() == y && Math.abs(x - alexCoor.getX()) 
                == 1) { // If the botton is connected by the edge to Alex
            
            boolean alexDied = arrayListCards.get(x * 3 + y).changeAlex(arrayListCards.get(
                    alexCoor.getX() * 3 + alexCoor.getY())); // Is true if Alex hp is <= 0.

            arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).setHp(
                Math.min(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getHp(), 
                maxHp)); // Change hp level of Alex according to the given card.
            arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY())
                .renewString(); // Renew displayed string in the Alex button.

            button = arrayList.get(x * 3 + y); // Changes the button whitch was hold.
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).getImage()));
            button.setText(arrayListCards.get(x * 3 + y).getString());

            button = arrayList.get(alexCoor.getX() * 3 + alexCoor.getY()); // Changes Alex button.
            button.setIcon(new ImageIcon(arrayListCards.get(
                alexCoor.getX() * 3 + alexCoor.getY()).getImage()));
            button.setText(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getString());
            
            if (alexDied) { // If hp of Alex is now <= 0.
                JOptionPane.showMessageDialog(frame.getComponent(0), "You lose");
                status = -1;
            }
            if (arrayListCards.get(x * 3 + y).getHp() > 0 && alexSword 
                > arrayListCards.get(alexCoor.getX() * 3
                + alexCoor.getY()).getSword()) { // If it is not needed to move Alex.
                return;
            }
            arrayListCards.set(x * 3 + y, arrayListCards.get(alexCoor.getX() * 3 
                + alexCoor.getY())); // Changes the coordinates of Alex to a button that was hold.
            arrayListCards.set(alexCoor.getX() * 3 + alexCoor.getY(), 
                new Cards().randomCards(level, 
                strengthPlus)); // Creates random card in place where Alex was.

            button = arrayList.get(x * 3 + y);  // Changes Alex button.
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).getImage()));
            button.setText(arrayListCards.get(x * 3 + y).getString());

            button = arrayList.get(alexCoor.getX() * 3 
                + alexCoor.getY());  // Changes button that was in Alex place.
            button.setIcon(new ImageIcon(arrayListCards.get(alexCoor.getX() * 3 
                + alexCoor.getY()).getImage()));
            button.setText(arrayListCards.get(alexCoor.getX() * 3 
                + alexCoor.getY()).getString());

            alexCoor.setX(x); // Gives Alex coordinates of holded button.
            alexCoor.setY(y);
            
            if (arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getMoney()
                 >= levelMoney) { // If player has enought money.
                JOptionPane.showMessageDialog(frame.getComponent(0), "You win");
                status = 1;
            }
        }

    }

    public Game(int level, int maxHp, int strengthPlus) {
        this.level = level;
        this.maxHp = maxHp;
        this.strengthPlus = strengthPlus;
        levelMoney = 0; // Money that are required to win.
        switch (level) {
            case 1:
                levelMoney = 10;
                break;
            case 2:
                levelMoney = 15;
                break;
            case 3:
                levelMoney = 18;
                break;
            default:
                levelMoney = 24;
        }
        alexCoor = new Coordinates(1, 1); // Creates Alex in the center.
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                arrayListCards.add(new AlexCard(maxHp)); // Alex is in the center of the board
            } else {
                arrayListCards.add(new Cards().randomCards(level, 
                    strengthPlus)); // Other cards are random
            }
        }
        frame.setContentPane(new JLabel(new ImageIcon(PathFinder.findFile(
            "misc/CardsGame/darkbackgrond.jpg"))));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayList.add(new JButton(arrayListCards.get(i * 3 + j).getString(),
                     new ImageIcon(arrayListCards.get(i * 3 + j).getImage())));
                JButton button2 = arrayList.get(i * 3 + j);; // Creates button.
                
                button2.setBounds((int) ((BUTTON_BORDER + j * BUTTON_SIZE) * WHOLE_SCREEN_W),
                     (int) (BUTTON_BORDER_UP * WHOLE_SCREEN_H + (i * BUTTON_SIZE) 
                     * WHOLE_SCREEN_W), (int) (BUTTON_SIZE * WHOLE_SCREEN_W),
                     (int) (BUTTON_SIZE * WHOLE_SCREEN_W));
                button2.setVerticalTextPosition(SwingConstants.TOP);
                button2.setHorizontalTextPosition(SwingConstants.CENTER);
                button2.addActionListener(this);

                button2.setFont(new Font("Monospace", Font.PLAIN, 12));
                button2.setForeground(new Color(255, 128, 64));

                button2.setFocusPainted(false); // Not to have a line in a button.

                button2.setOpaque(false); // Changes button to transparent.
                button2.setContentAreaFilled(true); 
                                
                button2.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 215), 3));
                
                button2.setBackground(new Color(0, 0, 0, 200));  // Gives translucent background.
        
                button2.setActionCommand("" + (int) 
                    (i * 3 + j)); // To determine later which button was hold.

                frame.add(button2);
            }
        }

        Color backgroundColor = new Color(214, 138, 242);
        JLabel textArea = new JLabel("Collect " + levelMoney + " coins to win, warrior...");

        textArea.setFont(new Font("Monospace", Font.BOLD, 22));
        textArea.setForeground(backgroundColor);
        textArea.setBounds((int) (BUTTON_BORDER * WHOLE_SCREEN_W), 5, 700, 40);
        frame.add(textArea);

        JPanel panel = new JPanel(); 

        frame.add(panel);
        panel.setBackground(backgroundColor);
        SCREEN_SIZE.getWidth();
        frame.setSize((int) SCREEN_SIZE.getWidth(), (int) SCREEN_SIZE.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Checks if game must be ended (if Alex died or has enought money).
     * Return true if player won, else return false.
     */
    public boolean gameControl() {
        while (true) {
            if (status != 0) {
                break;
            }
        }
        frame.setVisible(false);
        if (status == -1) { // If player lose.
            return false;
        }
        return true;
    }

    public ArrayList<Cards> getArrayList() {
        return arrayListCards;
    }
}