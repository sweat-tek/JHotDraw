package org.jhotdraw.draw.tool;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public interface KeyListeningTool extends KeyListener, BaseTool {
    @Override
    default void keyTyped(KeyEvent e) {
    }

    @Override
    default void keyPressed(KeyEvent e) {
    }

    @Override
    default void keyReleased(KeyEvent e) {
    }

}