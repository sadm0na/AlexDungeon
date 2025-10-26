package src;

import java.awt.*;
import java.util.*;

/**
 * All other cards inherit from Cards.
 * Has hp, money, sword, image and string for every card.
 * Can create a random card, has two methods that overades later:
 * changeAlex that changes the main character and renewString.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */

class Cards implements Sizes{
    private int hp; // Health of the character in the card.
    private int money; // The amount of coins.
    private int sword; // Strength of the sword.
    private Image image;
    private String string; // Is displayed in the button as well as an image.

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
     */
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
     * Returns a boolean variable of the stage of Alex after this applying them.
     * If true - alex hp is <= 0, so he is dead. Otherwise returns 1.
     */
    boolean changeAlex(Cards alex) {
        return false;
    }

    /**
     * For overloading later. 
     * Renews a string when it changes.
     */
    void renewString() {
        string = "";
    }
}



