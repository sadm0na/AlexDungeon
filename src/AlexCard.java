package src;

import java.awt.*;
import javax.swing.*;

/**
 * Card mith the main character, inherits from Cards.
 * Has a constructer with setting the image, money, sword and hp.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */

class AlexCard extends Cards {

    public AlexCard(int hp) {
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/alexW.png"));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (CARD_WIDTH * WHOLE_SCREEN_W),
             (int) (CARD_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT));
        this.setMoney(0);
        this.setHp(hp); // The maximal hp can be changed during the dungeon.
        this.setSword(0);
        this.setString("HP: " + this.getHp() + "      $: " + this.getMoney()
            + "        ATK: " + this.getSword());
        
    }

    @Override
    void renewString() {
        this.setString("HP: " + this.getHp() + "      $: " + this.getMoney()
            + "        ATK: " + this.getSword());
    }
    
}