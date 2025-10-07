import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.event.*;

class cards {
    int hp;
    int money;
    int sword;
    ImageIcon img;
    String string;

    void actionCard() {

    }

    public ImageIcon getImageIcon() {
        return img;
    }

    public String getString() {
        return string;
    }

    cards randomCards() {
        Random random = new Random();
        int r = random.nextInt(1, 4);
        if (r == 1) {
           return new monster();
        } else { 
            if (r == 2) {
                return (new sword());
            } 
        }
        return(new poison());
    }

    boolean changeAlex(cards alex) {
        return false; // its ok. if true - game stop
    }

    void renewString() {
        string = "";
    }
}

class Alex extends cards {
    
    public Alex(int hp) {
        img  = new ImageIcon("alex.jpeg");
        money = 0;
        this.hp = hp;
        sword = 0;
        string = "hp: " + hp + "        $: " + money + "        sw " + sword;
        
    }

    @Override
    void renewString() {
        string = "hp: " + hp + "        $: " + money + "        sw " + sword;
    }

    
}

class monster extends cards {

    public monster() {
        img  = new ImageIcon("monster.jpg");
        Random random = new Random();
        hp = random.nextInt(5) + 2;
        money = random.nextInt(5) + 2;
        string = "hp: " + hp + " $: " + money;
    }
    
    @Override
    boolean changeAlex(cards alex) {
        if (alex.sword > 0) {
            int s = 0 + alex.sword;
            alex.sword = Math.max(0, s - hp);
            alex.renewString();
            hp = Math.max(0, hp - s);
            this.renewString();
            if (hp > 0) {
                return false;
            }
        }
        alex.hp -= hp;
        if (alex.hp <= 0) {
            alex.renewString();
            return true;
        }
        alex.money += money;
        alex.renewString();
        return false;
    }

    @Override
    void renewString() {
        string = "hp: " + hp + " $: " + money;
    }
}

class poison extends cards {

    public poison() {
        img  = new ImageIcon("poison.png");
        Random random = new Random();
        hp = random.nextInt(5) + 2;
        string = "+" + hp;
    }

    @Override
    boolean changeAlex(cards alex) {
        alex.hp += hp;
        alex.renewString();
        return false;
    }
}

class sword extends cards {

    public sword() {
        img  = new ImageIcon("sword.png");
        Random random = new Random();
        sword = random.nextInt(5) + 2;
        string = "" + sword;
        
    }

    @Override
    boolean changeAlex(cards alex) {
        alex.sword = Math.max(alex.sword, sword);
        alex.renewString();
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

class Game implements ActionListener {
    coordinants alexCoor;
    ArrayList<cards> arrayListCards = new ArrayList<cards>();
    ArrayList<JButton> arrayList = new ArrayList<JButton>();
    //public int x = 0;
    JFrame frame = new JFrame("minigame");
    JButton button;

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

            button = arrayList.get(x * 3 + y);
            button.setIcon(arrayListCards.get(x * 3 + y).img);
            button.setText(arrayListCards.get(x * 3 + y).string);

            button = arrayList.get(alexCoor.x * 3 + alexCoor.y);
            button.setIcon(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).img);
            button.setText(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).string);

            // END MINI-GAME
            if (ind) {
                JOptionPane.showMessageDialog(frame.getComponent(0), "You lose");
            }

            if (arrayListCards.get(x * 3 + y).hp > 0 && alex_sword > arrayListCards.get(alexCoor.x * 3 + alexCoor.y).sword) {
                return;
            }
            arrayListCards.set(x * 3 + y, arrayListCards.get(alexCoor.x * 3 + alexCoor.y));
            arrayListCards.set(alexCoor.x * 3 + alexCoor.y, new cards().randomCards());
            button = arrayList.get(x * 3 + y);
            button.setIcon(arrayListCards.get(x * 3 + y).img);
            button.setText(arrayListCards.get(x * 3 + y).string);

            button = arrayList.get(alexCoor.x * 3 + alexCoor.y);
            button.setIcon(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).img);
            button.setText(arrayListCards.get(alexCoor.x * 3 + alexCoor.y).string);

            alexCoor.x = x;
            alexCoor.y = y;

            //button.setBounds(40 + x, 100, 260, 260); 
        }

    }

    public Game() {
        alexCoor = new coordinants(1, 1);
        Random random = new Random();
        int r = random.nextInt(1, 4);
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                arrayListCards.add(new Alex(3));
            }
            else {
                if (r == 1) {
                    arrayListCards.add(new monster());
                } else {
                    if (r == 2) {
                        arrayListCards.add(new sword());
                    } else {
                        arrayListCards.add(new poison());
                    }
                }
                r = random.nextInt(1, 4);
            }
        }

         // creates window not visible yet
        ImageIcon img = new ImageIcon("alex.jpeg");
        button = new JButton("+6 hp", img); // creates button
        button.setActionCommand("1");

        //button.setIcon(img);

        //ClickReporter clickReporter;

        button.setBounds(40, 100, 260, 260);
        button.setVerticalTextPosition(SwingConstants.TOP);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        //frame.add(button, BorderLayout.SOUTH);

        //frame.add(button);
        //clickReporter = new ClickReporter(); //3 creates listener
        button.addActionListener(this);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrayList.add(new JButton(arrayListCards.get(i * 3 + j).getString(),
                     arrayListCards.get(i * 3 + j).getImageIcon()));
                JButton button2 = arrayList.get(i * 3 + j);; // creates button

                //button.setIcon(img);
                button2.setBounds(10 + j * 260, 10 + i * 260, 260, 260);
                button2.setVerticalTextPosition(SwingConstants.TOP);
                button2.setHorizontalTextPosition(SwingConstants.CENTER);
                button2.addActionListener(this);
                button2.setActionCommand("" + (int)(i * 3 + j));
                //frame.add(button, BorderLayout.SOUTH);
                frame.add(button2);
            }
        }

        //.setPreferredSize(new Dimension(60,60));
        //button.setPreferredSize(60,60);
        // put component in frame: button in frame

        JPanel panel = new JPanel(); // creates another component
        frame.add(panel); // put panel in frame
        Color mauve = new Color(128, 100, 100); // creates helper object:
        // Color
        panel.setBackground( mauve ); // colors background panel
        // standard code for frames
        frame.setSize(800, 800); // size of window in pixels
        frame.setVisible(true); // make frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // make close button (X) behave as expected
    }

    public ArrayList<cards> getArrayList() {
        return arrayListCards;
    }

    void actionGame() {
    }
}


class ClickReporter extends cardsMAin implements ActionListener {
    // this method will be called when a button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
    //reaction to button click
        //x += 5;
        //button.setBounds(x, 100, 260, 260);
    }
}

class cardsMAin {
    public static void main(String[] args) {
        Game game = new Game();
    }
}


