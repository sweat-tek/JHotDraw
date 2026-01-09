package org.jhotdraw.draw.text;

import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.FigureAdapter;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.figure.TextHolderFigure;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractEditableFloatingText {
    private DrawingView view;
    private TextHolderFigure editedFigure;

    public abstract JComponent getEditorComponent();
    protected abstract void updateWidget();
    protected abstract DrawingView getDrawingView();



    private FigureListener figureHandler = new FigureAdapter() {
        @Override
        public void attributeChanged(FigureEvent e) {
            updateWidget();
        }
    };


    /**
     * Removes the overlay.
     */
    public void endOverlay() {
        view = getDrawingView();
        view.getComponent().requestFocus();
        JComponent component = getEditorComponent();
        if (component != null) {
            component.setVisible(false);
            view.getComponent().remove(component);
            Rectangle bounds = component.getBounds();
            view.getComponent().repaint(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if (editedFigure != null) {
            editedFigure.removeFigureListener(figureHandler);
            editedFigure = null;
        }
    }

}
