package main;

import javax.swing.JFrame;
public class App {
        public static void main(String[] args) throws Exception {
                final String WINDOW_TITLE = "Chess Game Made With Java";
                JFrame window = new JFrame(WINDOW_TITLE);
                GamePanel gamePanel = new GamePanel();
                window.setResizable(false);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.add(gamePanel);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                gamePanel.startGame();
        }
}
