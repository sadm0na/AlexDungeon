import java.util.*;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.swing.*;
import java.awt.event.*;

class ActionEventPanel extends JPanel implements ActionListener {

    //Color backgrounfColor = new Color(128, 100, 100); // creates helper object:
    // Create two buttons
    private JButton jbtOk = new JButton("OK");
    private JButton jbtCancel = new JButton("Cancel");
    /** Default constructor. */
    public ActionEventPanel() {
        // Add buttons to the frame
        this.add(jbtOk);
        this.add(jbtCancel);
        // Register at the sources
        jbtOk.addActionListener(this);
        jbtCancel.addActionListener(this);
    }

    /* This method will be invoked when a button is clicked */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            System.out.println("The OK button is clicked");
            this.setBackground(java.awt.Color.RED);
        } else if (e.getActionCommand().equals("Cancel")) {
            System.out.println("The Cancel button is clicked");
            this.setBackground(java.awt.Color.BLUE);
        }
    }
    void buildGUI() {
        JFrame frame = new JFrame("ActionEventPanel");
        ActionEventPanel aep = new ActionEventPanel();
        frame.add(aep);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(120, 100);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("ActionEventPanel");
        JPanel panel = new JPanel(); // creates another component
        frame.add(panel); // put panel in frame
        ImageIcon img = new ImageIcon("backgrond.jpg");
             
        frame.setContentPane(new JLabel(img));
        
       

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
         //JOptionPane.showMessageDialog(frame.getComponent(0), "Hello World");
        //SwingUtilities.invokeLater(() -> {
            //new ActionEventPanel().buildGUI();
        //});
    }
}
