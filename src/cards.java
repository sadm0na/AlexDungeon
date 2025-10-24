package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class Cards implements Sizes{
    int hp; // health
    int money; 
    int sword; // strength of the sword
    Image image;
    String string;

    /**
     * For overloading later.
     **/
    void actionCard() {
    }

    public Image getImageIcon() {
        return image;
    }

    public String getString() {
        return string;
    }

    /**
     * Returns a random card.
     **/
    Cards randomCards(int level, int strengthPlus) {
        Random random = new Random();
        int r = random.nextInt(1, 4);
        if (r == 1) {
            return new monster(level);
        } else { 
            if (r == 2) {
                return (new sword(strengthPlus));
            } 
        }
        return new poison();
    }

    boolean changeAlex(Cards alex) {
        return false; // its ok. if true - game stop
    }

    void renewString() {
        string = "";
    }
}

class AlexCards extends Cards {
    
    public AlexCards(int hp) {
        ImageIcon imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/alexW.png"));
        image = imageI.getImage();
        image = image.getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT);
        money = 0;
        this.hp = hp;
        sword = 0;
        string = "hp: " + hp + "        $: " + money + "          sword strength: " + sword;
        
    }

    @Override
    void renewString() {
        string = "hp: " + hp + "        $: " + money + "          sword strength: " + sword;
    }

    
}

class monster extends Cards {

    public monster(int level) {
        ImageIcon imageI;
        imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/monster6.png"));
        if (level == 2) {
            imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/monster2.png"));
        }
        if (level == 3) {
            imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/monster3.png"));
        }
        if (level == 4) {
            imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/monster4.png"));
        }
        if (level == 5) {
            imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/monster5.png"));
        }
        image = imageI.getImage();
        image = image.getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT);
        Random random = new Random();
        hp = random.nextInt(5) + level * 4;
        money = random.nextInt(5) + level * 2;
        string = "hp: " + hp + "             reward for killing: " + money + "$";
    }
    
    @Override
    boolean changeAlex(Cards AlexCards) {
        if (AlexCards.sword > 0) {
            int s = 0 + AlexCards.sword;
            AlexCards.sword = Math.max(0, s - hp);
            AlexCards.renewString();
            hp = Math.max(0, hp - s);
            this.renewString();
            if (hp > 0) {
                return false;
            }
        }

        AlexCards.hp -= hp;
        if (AlexCards.hp <= 0) {
            AlexCards.renewString();
            return true;
        }
        AlexCards.money += money;
        AlexCards.renewString();
        return false;
    }

    @Override
    void renewString() {
        string = "hp: " + hp + "             reward for killing: " + money + "$";
    }
}

class poison extends Cards {

    public poison() {
        ImageIcon imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/healthW.png"));
        image = imageI.getImage();
        image = image.getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT);
        Random random = new Random();
        hp = random.nextInt(5) + 2;
        string = "+" + hp + " to hp";
    }

    @Override
    boolean changeAlex(Cards AlexCards) {
        AlexCards.hp += hp;
        AlexCards.renewString();
        return false;
    }
}

class sword extends Cards {

    public sword(int strengthPlus) {
        ImageIcon imageI = new ImageIcon(PathFinder.findFile("misc/CardsGame/sword1.png"));
        image = imageI.getImage();
        image = image.getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT);
        Random random = new Random();
        sword = random.nextInt(5) + 2 + strengthPlus;
        string = "Sword strength: " + sword;
        
    }

    @Override
    boolean changeAlex(Cards AlexCards) {
        AlexCards.sword = Math.max(AlexCards.sword, sword);
        AlexCards.renewString();
        return false;
    }
}

class coordinants {
    int x;
    int y;
    public coordinants(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getOne() {
        return x * 3 + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class Game implements ActionListener, Sizes {
    coordinants alexCoor;
    ArrayList<Cards> arrayListCards = new ArrayList<Cards>();
    ArrayList<JButton> arrayList = new ArrayList<JButton>();
    //public int x = 0;
    JFrame frame = new JFrame("minigame");
    JButton button;
    int level;
    int levelMoney;
    int maxHp;
    int strengthPlus;
    volatile int status = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        
        int h = Integer.parseInt(e.getActionCommand());
        int x = h / 3;
        int y = h - x * 3;
        int alex_sword = 0 + arrayListCards.get(alexCoor.x * 3 + alexCoor.y).sword;
        if (alexCoor.x == x && Math.abs(y - alexCoor.y) == 1 
                || alexCoor.y == y && Math.abs(x - alexCoor.x) == 1) {
            
            boolean ind = arrayListCards.get(x * 3 + y).changeAlex(
                arrayListCards.get(alexCoor.x * 3 + alexCoor.y));
            arrayListCards.get(alexCoor.x * 3 + alexCoor.y).hp = Math.min( arrayListCards.get(alexCoor.x * 3 + alexCoor.y).hp, maxHp);
            arrayListCards.get(alexCoor.x * 3 + alexCoor.y).renewString();
            button = arrayList.get(x * 3 + y);
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).image));
            button.setText(arrayListCards.get(x * 3 + y).string);

            button = arrayList.get(alexCoor.x * 3 + alexCoor.y);
            button.setIcon(new ImageIcon(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).image));
            button.setText(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).string);

            // END MINI-GAME
            if (ind) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "You lose");
                
                status = -1;
                //this.GameControl();
            }

            if (arrayListCards.get(x * 3 + y).hp > 0 && alex_sword > arrayListCards.get(alexCoor.x * 3 + alexCoor.y).sword) {
                return;
            }
            arrayListCards.set(x * 3 + y, arrayListCards.get(alexCoor.x * 3 + alexCoor.y));
            arrayListCards.set(alexCoor.x * 3 + alexCoor.y, new Cards().randomCards(level, strengthPlus));
            button = arrayList.get(x * 3 + y);
            button.setIcon(new ImageIcon(arrayListCards.get(x * 3 + y).image));
            button.setText(arrayListCards.get(x * 3 + y).string);

            button = arrayList.get(alexCoor.x * 3 + alexCoor.y);
            button.setIcon(new ImageIcon(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).image));
            button.setText(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).string);

            alexCoor.x = x;
            alexCoor.y = y;

             // END MINI-GAME
            if (arrayListCards.get(alexCoor.x * 3 + alexCoor.y).money >= levelMoney) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "You win");
                status = 1;
                //this.GameControl();
            }


            //button.setBounds(40 + x, 100, 260, 260); 
        }

    }

    public Game( int level, int maxHp, int strengthPlus) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.level = level;
        this.maxHp = maxHp;
        this.strengthPlus = strengthPlus;
        levelMoney = 0;
        if (level == 1) {
            levelMoney = 10;
        } else {
            if (level == 2) {
                levelMoney = 15;
            } else {
                levelMoney = 20;
            }
        }
        alexCoor = new coordinants(1, 1);
        for (int i = 0; i < 9; i++) {
            if (i == 4) {

                arrayListCards.add(new AlexCards(maxHp));
            } else {
                arrayListCards.add(new Cards().randomCards(level, strengthPlus));
            }
        }
        // creates window not visible yet
        ImageIcon image = new ImageIcon(PathFinder.findFile("misc/CardsGame/alexW.png"));
        
        Color backgroundButton = new Color(137, 158, 140); // creates helper object:

       
        
        frame.setContentPane(new JLabel(new ImageIcon(PathFinder.findFile("misc/CardsGame/darkbackgrond.jpg"))));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayList.add(new JButton(arrayListCards.get(i * 3 + j).getString(),
                     new ImageIcon(arrayListCards.get(i * 3 + j).getImageIcon())));
                JButton button2 = arrayList.get(i * 3 + j);; // creates button


                button2.setBounds((int) ((buttonBorder + j * buttonSize) * wholeScreenW),
                     (int) (buttonBordeUp * wholeScreenH + (i * buttonSize) * wholeScreenW), (int) (buttonSize * wholeScreenW),
                     (int) (buttonSize * wholeScreenW));
                button2.setVerticalTextPosition(SwingConstants.TOP);
                button2.setHorizontalTextPosition(SwingConstants.CENTER);
                button2.addActionListener(this);

                
                button2.setFont(new Font("Algerian", Font.PLAIN, 12));
                button2.setForeground(new Color(255, 128, 64));

                // надо сделать кнопку непрозрачной но с прозрачным фоном
                button2.setOpaque(false); // делаем прозрачной
                button2.setContentAreaFilled(true); 
                                
                // бордеры у кнопок
                button2.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 215), 3));
                
                // фон кнопки полупрозрачный
                button2.setBackground(new Color(0, 0, 0, 200)); 
        
                button2.setActionCommand("" + (int)(i * 3 + j));

                
                frame.add(button2);
            }
        }

        Color backgrounfColor = new Color(214, 138, 242); // creates helper object:
        JLabel textArea = new JLabel("You need " + levelMoney + " coins to win.");
        textArea.setFont(new Font("Algerian", Font.BOLD, 22));
        textArea.setForeground(backgrounfColor);
        textArea.setBounds((int) (buttonBorder * wholeScreenW),5,700,40);
        frame.add(textArea);

        JPanel panel = new JPanel(); // creates another component
        frame.add(panel); // put panel in frame

        panel.setBackground( backgrounfColor ); // colors background panel

        screenSize.getWidth();
        frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight()); // size of window in pixels
        frame.setVisible(true); // make frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // make close button (X) behave as expected
    }

    public boolean GameControl(){
        while (true) {
            if (status != 0) {
                break;
            }
        }
        frame.setVisible(false);
        if (status == -1)
            return false;
        return true;
    }

    public ArrayList<Cards> getArrayList() {
        return arrayListCards;
    }

    void actionGame() {
    }
}


class ClickReporter extends CardsMAin implements ActionListener {
    // this method will be called when a button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

class CardsMAin {
    public static void main(String[] args) {
         
        Game game = new Game( 1, 10, 2);
        boolean b = game.GameControl();
        System.out.println(b);
    }
} 


