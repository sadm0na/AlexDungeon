package src;

import java.awt.*;
import java.util.*;

/**
 * 
 */

class Cards implements Sizes{
    private int hp; // health
    private int money; 
    private int sword; // strength of the sword
    private Image image;
    private String string;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getSword() {
        return sword;
    }

    public void setSword(int sword) {
        this.sword = sword;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    /**
     * Returns a random card.
     **/
    Cards randomCards(int level, int strengthPlus) {
        Random random = new Random();
        int r = random.nextInt(1, 4);
        if (r == 1) {
            return new MonsterCard(level);
        } else { 
            if (r == 2) {
                return (new SwordCard(strengthPlus));
            } 
        }
        return new PoisonCard();
    }

    /**
     * For overloading later.
     * Applyes changes to Alex.
     */
    boolean changeAlex(Cards alex) {
        return false; // its ok. if true - game stops
    }

    /**
     * For overloading later. 
     * Renews a string when it changes.
     **/
    void renewString() {
        string = "";
    }
}



