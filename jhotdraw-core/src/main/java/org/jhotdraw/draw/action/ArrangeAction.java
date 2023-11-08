package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import java.util.Collection;
import java.util.LinkedList;

public class ArrangeAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;

    public static final String BRING_TO_FRONT = "edit.bringToFront";
    public static final String SEND_TO_BACK = "edit.sendToBack";

    private final String orderType;

    public ArrangeAction(DrawingEditor editor, String orderType) {
        super(editor);
        this.orderType = orderType;
        ResourceBundleUtil labels = getResourceBundle();
        //ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, orderType);
        updateEnabledState();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        arrangeFigures(view, figures, orderType);

        fireUndoableEditHappened(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                return getResourceBundle().getTextProperty(orderType);
                /*if (BRING_TO_FRONT.equals(orderType)) {
                    resourceLabels (BRING_TO_FRONT);
                } else if (SEND_TO_BACK.equals(orderType)) {
                    resourceLabels (SEND_TO_BACK);
                }
                return null;*/
            }

            @Override
            public void redo() throws CannotRedoException {
                //super.redo();
                arrangeFigures(view, figures, orderType);
            }

            @Override
            public void undo() throws CannotUndoException {
                //super.undo();
                // Reverse the action by calling the opposite order type
                if (BRING_TO_FRONT.equals(orderType)) {
                    arrangeFigures(view, figures, SEND_TO_BACK);
                } else if (SEND_TO_BACK.equals(orderType)) {
                    arrangeFigures(view, figures, BRING_TO_FRONT);
                }
            }
        });
    }

    /*public static void sendToBack(DrawingView view, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        for (Figure figure : figures) { // XXX Shouldn't the figures be sorted here back to front?
            drawing.sendToBack(figure);
        }
    }*/
    public static void arrangeFigures(DrawingView view, Collection<Figure> figures, String orderType) {
        Drawing drawing = view.getDrawing();
        if (BRING_TO_FRONT.equals(orderType)) {
            for (Figure figure : drawing.sort(figures)) {
                drawing.bringToFront(figure);
            }
        } else if (SEND_TO_BACK.equals(orderType)) {
            for (Figure figure : drawing.sort(figures)) {
                drawing.sendToBack(figure);
            }
            /*for (Figure figure : figures) {
                drawing.sendToBack(figure);
            }*/

        }
    }
    public static ResourceBundleUtil getResourceBundle(){
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels;
    }

}