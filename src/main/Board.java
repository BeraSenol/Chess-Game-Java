package main;

import java.awt.Color;
import java.awt.Graphics2D;
public class Board {
        
        private final int MAX_COL = 8;
        private final int MAX_ROW = 8;
        public static final int TILE_SIZE = 100;
        public static final int TILE_SIZE_HALF = TILE_SIZE / 2;
        private final Color WHITE_TILE_COLOR = new Color(210, 165, 125);
        private final Color BLACK_TILE_COLOR = new Color(175, 115, 75);

        public void drawBoard(Graphics2D graphics2D) {
                for (int i = 0; i < MAX_ROW; i++) {
                        for (int j = 0; j < MAX_COL; j++) {
                                if ((i + j) % 2 == 0) {
                                        graphics2D.setColor(WHITE_TILE_COLOR);
                                } else {
                                        graphics2D.setColor(BLACK_TILE_COLOR);
                                }
                                graphics2D.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        }
                }
        }
}
