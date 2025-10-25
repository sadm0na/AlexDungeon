package src;

import java.awt.*;
import javax.swing.*;

/**
 * Card mith the main character.
 * 
 */

class AlexCard extends Cards {

    public AlexCard(int hp) {
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/alexW.png"));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT));
        this.setMoney(0);
        //money = 0;
        this.setHp(hp);
        //this.hp = hp;
        this.setSword(0);
        //sword = 0;
        this.setString("HP: " + this.getHp() + "      $: " + this.getMoney()
            + "        ATK: " + this.getSword());
        
    }

    @Override
    void renewString() {
        this.setString("HP: " + this.getHp() + "      $: " + this.getMoney()
            + "        ATK: " + this.getSword());
    }
    
}