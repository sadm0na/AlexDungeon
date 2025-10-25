package src;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Card mith the monster, inherits from Cards.
 * Has a constructer with setting the image, hp and sword.
 * 
 */

class MonsterCard extends Cards {

    public MonsterCard(
            int level) { // Money from killing the monster and its hp depend on level.
        String imagePathString = "misc/CardsGame/monster" + level + ".png";
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile(imagePathString));

        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (CARD_WIDTH * WHOLE_SCREEN_W),
             (int) (CARD_HEIGHT * WHOLE_SCREEN_H), Image.SCALE_DEFAULT));

        Random random = new Random();

        this.setHp(random.nextInt(5) + level * 4); // From level*4 to level *4 + 5.
        this.setMoney(random.nextInt(5) + level * 2); // From level*4 to level *4 + 5.
        this.setString("HP: " + this.getHp() + "          LOOT: " + this.getMoney() + "$");
    }
    
    @Override
    boolean changeAlex(Cards alexCard) {
        if (alexCard.getSword() > 0) {
            int alexStrength = 0 + alexCard.getSword(); // Strength of Alex before change.

            alexCard.setSword(Math.max(0, alexStrength - this.getHp()));
            alexCard.renewString();
            this.setHp(Math.max(0, this.getHp() - alexStrength));
            this.renewString();
            if (this.getHp() > 0) {
                return false;
            }
        }
        alexCard.setHp(alexCard.getHp() - this.getHp());
        if (alexCard.getHp() <= 0) {
            alexCard.renewString();
            return true;
        }
        alexCard.setMoney(alexCard.getMoney() + this.getMoney());
        alexCard.renewString();
        return false;
    }

    @Override
    void renewString() {
        this.setString("HP: " + this.getHp() + "          LOOT: " + this.getMoney() + "$");
    }
}