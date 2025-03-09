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
    public void switchPanel(JPanel newPanel, boolean isFullScreen) {
        if (currentPanel != null) {
            getContentPane().remove(currentPanel);
        }
        currentPanel = newPanel;
        getContentPane().add(currentPanel, BorderLayout.CENTER);
        setFullscreen(isFullScreen);
        setVisible(true);
    }

    //setting the frame to full screen mode
    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(JFrame.NORMAL);
            changeSettings();
        }
        setVisible(true);
    }

    //private basic settings to change a screen
    private void changeSettings() {
        revalidate();
        repaint();
        pack();
        setLocationRelativeTo(null);
    }
}
