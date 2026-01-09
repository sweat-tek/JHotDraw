package org.jhotdraw.draw.tool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public interface ClickListeningTool extends MouseListener, BaseTool {
    @Override default void mousePressed(MouseEvent e) {}
    @Override default void mouseReleased(MouseEvent e) {}
    @Override default void mouseClicked(MouseEvent e) {}
    @Override default void mouseEntered(MouseEvent e) {}
    @Override default void mouseExited(MouseEvent e) {}
}
