package src;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Messages {

    public static void drawSpeechBubble(Graphics g, String message, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        
        // оригинальные настройки
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        // шрифт красивый
        Font pixelFont = new Font("Monospaced", Font.BOLD, 12);
        g2d.setFont(pixelFont);
        
        FontMetrics fm = g2d.getFontMetrics(); // какая то невероятная штука со стэковерфлоу для рассчета длины текста
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        
        // рисуем облачко
        int padding = 8;
        int bubbleWidth = textWidth + padding * 2;
        int bubbleHeight = textHeight + padding;
        
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(x, y - bubbleHeight - 5, bubbleWidth, bubbleHeight, 10, 10);

        g2d.setColor(new Color(255, 255, 255, 180)); // фон
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(x, y - bubbleHeight - 5, bubbleWidth, bubbleHeight, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x + padding, y - 13);
        
        // Восстанавливаем оригы настройки
        g2d.setColor(originalColor);
        g2d.setFont(originalFont);
    }

    public static void showMessage(Graphics g, String message, int screenWidth, int screenHeight, int mode) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Сохраняем прошлые настройки
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        Font warningFont = new Font("Monospaced", Font.BOLD, 22);
        g2d.setFont(warningFont);
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        
        // позиция сверху посередине
        int x = (screenWidth - textWidth) / 2;
        int y;
        if (mode == 0) {
            y = 50;
        } else {
            y = screenHeight - 130;
        }
        
        // фон
        g2d.setColor(new Color(0, 0, 0, 230));
        g2d.fillRect(x - 15, y - textHeight, textWidth + 30, textHeight + 15);
        
        // обводка
        g2d.setStroke(new BasicStroke(3));
        if (mode == 0) {
            g2d.setColor(new Color(239, 225, 28, 220));
        }
        else {
            g2d.setColor(new Color(20, 85, 87, 200));
        }
        g2d.drawRect(x - 15, y - textHeight, textWidth + 30, textHeight + 15);
        
        // Белый текст
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x, y - 3);
        
        // Восстанавливаем прошлые настройки
        g2d.setColor(originalColor);
        g2d.setFont(originalFont);
    }


}
