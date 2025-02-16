package Game;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private JPanel currentPanel;

    public Frame(String title) {
        super(title);
        this.setLayout(new BorderLayout());
        currentPanel = null;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //switching the displayed panel
    public void switchPanel(JPanel newPanel) {
        if (currentPanel != null) {
            getContentPane().remove(currentPanel);
        }
        currentPanel = newPanel;
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
