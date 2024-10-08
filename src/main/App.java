package main;

import javax.swing.JFrame;

public class App {
        public static void main(String[] args) throws Exception {
                final String WINDOW_TITLE = "Chess Game Made With Java";
                JFrame window = new JFrame(WINDOW_TITLE);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);

                GamePanel gamePanel = new GamePanel();
                window.add(gamePanel);
                window.pack();

                window.setLocationRelativeTo(null);
                window.setVisible(true);

                gamePanel.startGame();
        }
}
