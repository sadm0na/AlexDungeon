package src;

import java.awt.*;

/**
 * Handles drawing of UI messages and speech bubbles in the game.
 */
public class Messages implements Sizes {

    /**
     * Draws a speech bubble with text at specified coordinates.
     */
    public static void drawSpeechBubble(Graphics g, String message, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Save original settings
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        Font pixelFont = new Font("Monospaced", Font.BOLD, 12);
        g2d.setFont(pixelFont);
        
        // Calculate text size
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        
        // Create bubble (RoundRect) around text
        int padding = 8;
        int bubbleWidth = textWidth + padding * 2;
        int bubbleHeight = textHeight + padding;
        
        // Fill background
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(x, y - bubbleHeight - 5, bubbleWidth, bubbleHeight, 10, 10);

        // Sets bubble border
        g2d.setColor(new Color(255, 255, 255, 180));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(x, y - bubbleHeight - 5, bubbleWidth, bubbleHeight, 10, 10);
        
        // Add text
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x + padding, y - 13);
        
        // Restore original settings
        g2d.setColor(originalColor);
        g2d.setFont(originalFont);
    }

    /**
     * Displays a message on screen at top or bottom position.
     * Mode 0 for top, mode 1 for bottom placement.
     */
    public static void showMessage(Graphics g, String message, int screenWidth, int screenHeight, int mode) {
        Graphics2D g2d = (Graphics2D) g;
        
        Color originalColor = g2d.getColor();
        Font originalFont = g2d.getFont();
        
        Font warningFont = new Font("Monospaced", Font.BOLD, 22);
        g2d.setFont(warningFont);
        
        // Center the message
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int textHeight = fm.getHeight();
        
        int x = (screenWidth - textWidth) / 2;
        int y;
        if (mode == 0) {
            y = 70; // Top position
        } else if (mode == 1) {
            y = screenHeight - 80; // Bottom position
        } else {
            y = screenHeight / 2;
        }
        
        // Message background
        g2d.setColor(new Color(0, 0, 0, 230));
        g2d.fillRect(x - 15, y - textHeight, textWidth + 30, textHeight + 15);
        
        // Border color depends on mode
        g2d.setStroke(new BasicStroke(3));
        if (mode == 0) {
            g2d.setColor(new Color(227, 63, 37, 220)); // orange for top
        } else if (mode == 1) {
            g2d.setColor(new Color(20, 85, 87, 200)); // Blue-green for bottom
        } else {
            g2d.setColor(new Color(255, 197, 15)); // yellow for winning game
        }
        g2d.drawRect(x - 15, y - textHeight, textWidth + 30, textHeight + 15);
        
        // Draw message text
        g2d.setColor(Color.WHITE);
        g2d.drawString(message, x, y - 3);
        
        g2d.setColor(originalColor);
        g2d.setFont(originalFont);
    }
}