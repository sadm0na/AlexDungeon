package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


class Game implements ActionListener, Sizes {
    private Coordinants alexCoor; // coordinates of Alex Card
    private ArrayList<Cards> arrayListCards = new ArrayList<Cards>();
    private ArrayList<JButton> arrayList = new ArrayList<JButton>();
    private JFrame frame = new JFrame("minigame");
    private JButton button;
    private int level;
    private int levelMoney; // money required to pass the level
    private int maxHp;
    private int strengthPlus; // bonus from the chests in the game
    private volatile int status = 0; // -1 if level losed, 1 if won and 0 if is still played

    @Override
    public void actionPerformed(ActionEvent e) {
        int buttonPressed = Integer.parseInt(e.getActionCommand());
        int x = buttonPressed / 3;
        int y = buttonPressed - x * 3;
        int alexSword = 0 + arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getSword();

        if (alexCoor.getX() == x && Math.abs(y - alexCoor.getY()) == 1 
                || alexCoor.getY() == y && Math.abs(x - alexCoor.getX()) == 1) {
            
            boolean alexDied = arrayListCards.get(x * 3 + y).changeAlex(
                arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY())); // is true if Alex hp is <= 0

            arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).setHp(
                Math.min(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getHp(), maxHp));
            arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).renewString();
            button = arrayList.get(x * 3 + y);
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).getImage()));
            button.setText(arrayListCards.get(x * 3 + y).getString());
            button = arrayList.get(alexCoor.getX() * 3 + alexCoor.getY());
            button.setIcon(new ImageIcon(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getImage()));
            button.setText(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getString());
            // END MINI-GAME
            if (alexDied) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "You lose");
                status = -1;
            }
            if (arrayListCards.get(x * 3 + y).getHp() > 0 && alexSword 
                > arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getSword()) {
                return;
            }
            arrayListCards.set(x * 3 + y, arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()));
            arrayListCards.set(alexCoor.getX() * 3 + alexCoor.getY(), new Cards().randomCards(level, 
                strengthPlus));
            button = arrayList.get(x * 3 + y);
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).getImage()));
            button.setText(arrayListCards.get(x * 3 + y).getString());
            button = arrayList.get(alexCoor.getX() * 3 + alexCoor.getY());
            button.setIcon(new ImageIcon(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getImage()));
            button.setText(arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getString());
            alexCoor.setX(x);
            alexCoor.setY(y);
            // END MINI-GAME
            if (arrayListCards.get(alexCoor.getX() * 3 + alexCoor.getY()).getMoney() >= levelMoney) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "You win");
                status = 1;
            }
        }

    }

    public Game(int level, int maxHp, int strengthPlus) {
        this.level = level;
        this.maxHp = maxHp;
        this.strengthPlus = strengthPlus;
        levelMoney = 0;
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
        alexCoor = new Coordinants(1, 1);
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                arrayListCards.add(new AlexCard(maxHp)); // Alex is in the center of the board
            } else {
                arrayListCards.add(new Cards().randomCards(level, strengthPlus));
            }
        }
        frame.setContentPane(new JLabel(new ImageIcon(PathFinder.findFile(
            "misc/CardsGame/darkbackgrond.jpg"))));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayList.add(new JButton(arrayListCards.get(i * 3 + j).getString(),
                     new ImageIcon(arrayListCards.get(i * 3 + j).getImage())));
                JButton button2 = arrayList.get(i * 3 + j);; // creates button
                
                button2.setBounds((int) ((buttonBorder + j * buttonSize) * wholeScreenW),
                     (int) (buttonBordeUp * wholeScreenH + (i * buttonSize) * wholeScreenW), 
                     (int) (buttonSize * wholeScreenW),
                     (int) (buttonSize * wholeScreenW));
                button2.setVerticalTextPosition(SwingConstants.TOP);
                button2.setHorizontalTextPosition(SwingConstants.CENTER);
                button2.addActionListener(this);

                button2.setFont(new Font("Monospace", Font.PLAIN, 12));
                button2.setForeground(new Color(255, 128, 64));

                button2.setFocusPainted(false); // not to have a line in a button

                button2.setOpaque(false); // change button to transparent
                button2.setContentAreaFilled(true); 
                                
                button2.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 215), 3));
                
                button2.setBackground(new Color(0, 0, 0, 200));  // translucent background
        
                button2.setActionCommand("" + (int) 
                    (i * 3 + j)); // to determine which button was hold

                frame.add(button2);
            }
        }

        Color backgrounfColor = new Color(214, 138, 242);
        JLabel textArea = new JLabel("Collect " + levelMoney + " coins to win, warrior...");

        textArea.setFont(new Font("Monospace", Font.BOLD, 22));
        textArea.setForeground(backgrounfColor);
        textArea.setBounds((int) (buttonBorder * wholeScreenW), 5, 700, 40);
        frame.add(textArea);

        JPanel panel = new JPanel(); 

        frame.add(panel);
        panel.setBackground(backgrounfColor);
        screenSize.getWidth();
        frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public boolean GameControl(){
        while (true) {
            if (status != 0) {
                break;
            }
        }
        frame.setVisible(false);
        if (status == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Cards> getArrayList() {
        return arrayListCards;
    }
}

class CardsMAin {
    public static void main(String[] args) {
         
        Game game = new Game(2, 10, 2);
        boolean b = game.GameControl();
        System.out.println(b);
    }
} 