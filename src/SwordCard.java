package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class SwordCard extends Cards {

    public SwordCard(int strengthPlus) {
        ImageIcon imageIcon = new ImageIcon(PathFinder.findFile("misc/CardsGame/sword1.png"));
        this.setImage(imageIcon.getImage());
        this.setImage(this.getImage().getScaledInstance((int) (cardWeight * wholeScreenW),
             (int) (cardHeight * wholeScreenH), Image.SCALE_DEFAULT));
        Random random = new Random();
        this.setSword(random.nextInt(5) + 2 + strengthPlus);
        this.setString("ATK: " + this.getSword());
        
    }

    @Override
    boolean changeAlex(Cards alexCard) {
        alexCard.setSword(Math.max(alexCard.getSword(), this.getSword()));
        alexCard.renewString();
        return false;
    }
}