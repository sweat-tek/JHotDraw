/*
 * @(#)AbstractZOrderAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Defines the common workflow for z-order actions on selected figures.
 */
public abstract class AbstractZOrderAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    private final String id;

    protected AbstractZOrderAction(DrawingEditor editor, String id) {
        super(editor);
        this.id = id;
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, id);
        updateEnabledState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final DrawingView view = Objects.requireNonNull(
                getView(),
                "A z-order action requires an active drawing view.");
        final Drawing drawing = Objects.requireNonNull(
                view.getDrawing(),
                "A z-order action requires an attached drawing.");
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        assert figures != null : "Selected figures collection must not be null.";
        final List<Figure> originalOrder = new ArrayList<>(drawing.getChildren());
        reorder(view, figures);
        final List<Figure> reorderedOrder = new ArrayList<>(drawing.getChildren());
        fireUndoableEditHappened(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels
                        = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getTextProperty(id);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                restoreOrder(drawing, reorderedOrder);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                restoreOrder(drawing, originalOrder);
            }
        });
    }

    private void restoreOrder(Drawing drawing, List<Figure> order) {
        drawing.basicRemoveAll(new ArrayList<>(drawing.getChildren()));
        drawing.basicAddAll(0, order);
    }

    protected abstract void reorder(DrawingView view, Collection<Figure> figures);

    protected abstract void reverseReorder(DrawingView view, Collection<Figure> figures);
}
