package src;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Card mith the poson, inherits from Cards.
 * Has a constructer with setting the image and hp.
 * 
 * @author Monika Khachatryan
 * @ID 2276380
 * @author Caroline Savchenko
 * @ID 2338793
 * 
 */

class PoisonCard extends Cards {

    public PoisonCard() {
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/healthW.png"));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (CARD_WIDTH * WHOLE_SCREEN_W),
             (int) (CARD_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT));

        Random random = new Random();

        this.setHp(random.nextInt(5) + 2); // From 2 to 5.
        this.setString("+" + this.getHp() + " to HP");
    }

    @Override
    boolean changeAlex(Cards alexCard) {
        alexCard.setHp(alexCard.getHp() + this.getHp());
        alexCard.renewString();
        return false;
    }
}
