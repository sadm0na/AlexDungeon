package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class PoisonCard extends Cards {

    public PoisonCard() {
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/healthW.png"));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT));
        Random random = new Random();
        this.setHp(random.nextInt(5) + 2);
        this.setString("+" + this.getHp() + " to HP");
    }

    @Override
    boolean changeAlex(Cards alexCard) {
        alexCard.setHp(alexCard.getHp() + this.getHp());
        alexCard.renewString();
        return false;
    }
}
