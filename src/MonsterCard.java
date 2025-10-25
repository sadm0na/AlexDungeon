package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class MonsterCard extends Cards {

    public MonsterCard(int level) {
        String imagePathString = "misc/CardsGame/monster" + level + ".png";
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile(imagePathString));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT));
        Random random = new Random();
        this.setHp(random.nextInt(5) + level * 4);
        this.setMoney(random.nextInt(5) + level * 2);
        this.setString("HP: " + this.getHp() + "          LOOT: " + this.getMoney() + "$");
    }
    
    @Override
    boolean changeAlex(Cards alexCard) {
        if (alexCard.getSword() > 0) {
            int alexStrength = 0 + alexCard.getSword(); // Strength of Alex before change
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