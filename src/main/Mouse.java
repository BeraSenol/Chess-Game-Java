package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {

        public int x;
        public int y;
        protected boolean pressed;

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
                pressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
                pressed = false;
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
                x = mouseEvent.getX();
                y = mouseEvent.getY();
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
                x = mouseEvent.getX();
                y = mouseEvent.getY();
        }
}
