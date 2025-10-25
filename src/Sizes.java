package src;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import java.awt.*;

import java.util.List;

public interface Sizes {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double wholeScreenW = screenSize.getWidth();
    double wholeScreenH = screenSize.getHeight() - 30; // не совсем вмещается в экран по высоте из-за этой зелёной штуки с верху - не точно величена экрана

    double border =  0.15; // border of the inner room
    double innerBorder = 0.08;
    double backW = 1 - border * 2; // with of the room
    double backH = 1 - border * 2; // height og the room

    double miniMapSize = 0.18;
    double miniMapW = miniMapSize * 1.2; 
    double miniMapH = miniMapSize;  
        
    double miniMapX = 1 - miniMapW;
    double miniMapY = 1 - miniMapH; 
    
    double doorSize = 0.05;
    double doorWidth = doorSize;
    double doorHeight = doorSize * 2.3;

    double borderLeft = border;  
    double borderLeftInner = borderLeft + innerBorder;              
    double borderRight = border + backW;
    double borderRightInner = borderRight - innerBorder;
    double borderUp = border;
    double borderUpInner = border + innerBorder;
    double borderDown = border + backH;    
    double borderDownInner = borderDown - innerBorder; 

    double centerHorizontal = border + backW / 2;
    double centerVertical = border + backH / 2;
        
    double oneThirdHorizontal = border + backW / 3;
    double secondThirdHorizontal = border + 2 * backW / 3;
    double oneThirdVertical = border + backH / 3;
    double secondThirdVertical = border+ 2 * backH / 3 ;

    double keySize = 0.02;
    double keyWidth = keySize;
    double keyHeight = keyWidth * 1.5;

    double chestSize[] = {0.06, 0.06472, 0.06472, 0.0694};
    double chestHeight[] = chestSize;
    double chestWeight[] = {chestSize[0] * 0.75, chestSize[1] * 0.6, chestSize[2] * 0.6, chestSize[3] * 0.75};

    double alexSize = 0.0625;
    double alexWeigth = alexSize;
    double alexHeight = alexWeigth * 2;

    double doorNearArea = 0.1;
    double keyNearArea = innerBorder + alexWeigth / 2 + 0.1;
    double chestNearArea = 0.04472;

    double alexStartX = border + innerBorder + alexWeigth;
    double alexStartY = border + innerBorder + alexHeight ;

    double dialogPlusX = 0.03;

    double cardSize = 0.14;
    double cardWeight = cardSize;
    double cardHeight = cardWeight * 1.8;

    double buttonSize = 0.17;

    double buttonBorder = 1.0 / 2.0 - buttonSize - (buttonSize) / 2.0 ;
    double buttonBordeUp = 0.0666;


}