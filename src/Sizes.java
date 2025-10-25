package src;

import java.awt.*;

/**
 * Class of all sizes in the game.
 * All sizes are from 0 to 1 and then are multiplied to the dimention of the screen.
 */

public interface Sizes {
    final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
    final double WHOLE_SCREEN_W = SCREEN_SIZE.getWidth();
    final double WHOLE_SCREEN_H = SCREEN_SIZE.getHeight() - 30; 

    final double BORDER =  0.15; // Border of the inner room.
    final double INNER_BORDER = 0.08;
    final double BACK_WIDTH = 1 - BORDER * 2; // Width of the room.
    final double BACK_HEIGHT = 1 - BORDER * 2; // Height of the room.

    final double MINI_MAP_SIZE = 0.18;
    final double MINI_MAP_WIDTH = MINI_MAP_SIZE * 1.2; 
    final double MINI_MAP_HEIGHT = MINI_MAP_SIZE;  
        
    final double MINI_MAP_X = 1 - MINI_MAP_WIDTH; // Location of the mini map in the frame.
    final double MINI_MAP_Y = 1 - MINI_MAP_HEIGHT; 
    
    final double DOOR_SIZE = 0.05;
    final double DOOR_WIDTH = DOOR_SIZE;
    final double DOOR_HEIGHT = DOOR_SIZE * 2.3;

    final double BORDER_LEFT = BORDER;  
    final double BORDER_LEFT_INNER = BORDER_LEFT 
        + INNER_BORDER; // Inside the room without walls.           
    final double BORDER_REIGHT = BORDER + BACK_WIDTH;
    final double BORDER_REIGHT_INNER = BORDER_REIGHT - INNER_BORDER;
    final double BORDER_UP = BORDER;
    final double BORDER_UP_INNER = BORDER + INNER_BORDER;
    final double BORDER_DOWN = BORDER + BACK_HEIGHT;    
    final double BORDER_DOWN_INNER = BORDER_DOWN - INNER_BORDER; 

    final double CENTER_HORIZONTAL = BORDER + BACK_WIDTH / 2;
    final double CENTER_VERTICAL = BORDER + BACK_HEIGHT / 2;
        
    final double ONE_THIRD_HORIZONTAL = BORDER + BACK_WIDTH / 3;
    final double SECOND_THIRD_HORIZONTAL = BORDER + 2 * BACK_WIDTH / 3;
    final double ONE_THIRD_VERTICAL = BORDER + BACK_HEIGHT / 3;
    final double SECOND_THIRD_VERTICAL = BORDER + 2 * BACK_HEIGHT / 3;

    final double KEY_SIZE = 0.02;
    final double KEY_WIDTH = KEY_SIZE;
    final double KEY_HEIGHT = KEY_WIDTH * 1.5;

    final double CHEST_SIZE[] = {0.06, 0.06472, 0.06472, 0.0694};
    final double CHEST_HEIGHT[] = CHEST_SIZE;
    final double CHEST_WIDTH[] = {CHEST_SIZE[0] * 0.75, CHEST_SIZE[1] 
        * 0.6, CHEST_SIZE[2] * 0.6, CHEST_SIZE[3] * 0.75};

    final double ALEX_SIZE = 0.0625;
    final double ALEX_WIDTH = ALEX_SIZE;
    final double ALEX_HEIGHT = ALEX_WIDTH * 2;

    final double DOOR_NEAR_AREA = 0.1;
    final double KEY_NEAR_AREA = INNER_BORDER + ALEX_WIDTH / 2 + 0.1;
    final double CHEST_NEAR_AREA = 0.04472;

    final double ALEX_START_X = BORDER + INNER_BORDER + ALEX_WIDTH;
    final double ALEX_START_Y = BORDER + INNER_BORDER + ALEX_HEIGHT;

    final double DIALOG_PLUS_X = 0.03;

    final double CARD_SIZE = 0.14;
    final double CARD_WIDTH = CARD_SIZE;
    final double CARD_HEIGHT = CARD_WIDTH * 1.8;

    final double BUTTON_SIZE = 0.17;

    final double BUTTON_BORDER = 1.0 / 2.0 - BUTTON_SIZE - (BUTTON_SIZE) / 2.0 ;
    final double BUTTON_BORDER_UP = 0.0666;


}