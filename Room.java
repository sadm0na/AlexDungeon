import javax.swing.*;
import java.awt.BorderLayout;
import java.io.IOException;

public class Room {
    
    public static void main(String[] args) throws InterruptedException, IOException {
        MyPanel panel = new MyPanel(); // надо бы как то переименовать потом
        JFrame frame = new JFrame("Alex Dungeon");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // в центре

        frame.add(panel);
        frame.setVisible(true);

        while (true) {
            frame.repaint();
            panel.updateWorldPhysics();
            Thread.sleep(20);
        }

        // buttonsPanel.add(start);
        // buttonsPanel.add(stop);

        // frame.getContentPane().add(BorderLayout.NORTH, buttonsPanel);
        
    }
}
