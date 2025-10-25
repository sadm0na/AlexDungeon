package src;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Card mith the sword, inherits from Cards.
 * Has a constructer with setting the image and sword.
 * 
 */

class SwordCard extends Cards {

    public SwordCard(
            int strengthPlus) { // strengthPlus - the bonus to the strengs from the dungeon.
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/sword1.png"));

        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (CARD_WIDTH * WHOLE_SCREEN_W),
             (int) (CARD_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT));

        Random random = new Random();

        this.setSword(random.nextInt(5) + 2 + strengthPlus); // From 2 to 7 + bonus.
        this.setString("ATK: " + this.getSword());
    }

    @Override
    boolean changeAlex(Cards alexCard) {
        alexCard.setSword(Math.max(alexCard.getSword(), this.getSword()));
        alexCard.renewString();
        return false;
    }
}