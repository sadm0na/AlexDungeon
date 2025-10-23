package src;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Messages {

    // public void displayMessage(Graphics g, String message, int x, int y, int mode) {
    //     if (mode == 0) {
    //         drawSpeechBubble(g, message, x, y);
    //         return;
    //     }

    //     double countdown = 3;

    //     while (countdown >= 0) {

    //     }
    // }


    public static void drawSpeechBubble(Graphics g, String message, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        
        // оригинальные настройки
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        // шрифт красивый
        Font pixelFont = new Font("Monospaced", Font.BOLD, 12);
        g2d.setFont(pixelFont);
        
        FontMetrics fm = g2d.getFontMetrics(); // какая то невероятная штука со стэковерфлоу
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        
        // рисуем облачко
        int padding = 8;
        int bubbleWidth = textWidth + padding * 2;
        int bubbleHeight = textHeight + padding;
        
        g2d.setColor(new Color(255, 255, 255, 180)); // фон
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y - bubbleHeight - 5, bubbleWidth, bubbleHeight, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x + padding, y - 10);
        
        // Восстанавливаем настройки
        g2d.setColor(originalColor);
        g2d.setFont(originalFont);
    }
}
