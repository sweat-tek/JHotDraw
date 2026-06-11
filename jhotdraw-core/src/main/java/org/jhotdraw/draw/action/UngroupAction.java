/*
 * @(#)UngroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.jhotdraw.util.ResourceBundleUtil;

public class UngroupAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.ungroupSelection";
    private CompositeFigure prototype;

    // Constructor chaining eliminates the duplicate setup code smell
    public UngroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public UngroupAction(DrawingEditor editor, CompositeFigure prototype) {
        super(editor);
        this.prototype = prototype;
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(canUngroup(getView()));
    }

    protected boolean canUngroup() {
        return canUngroup(getView());
    }

    protected boolean canUngroup(DrawingView v) {
        return v != null
                && v.getSelectionCount() == 1
                && prototype != null
                && v.getSelectedFigures().iterator().next().getClass().equals(
                        prototype.getClass());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canUngroup()) {
            final DrawingView view = getView();
            final CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
            final LinkedList<Figure> ungroupedFigures = new LinkedList<>();

            UndoableEdit edit = new UngroupUndoableEdit(view, group);
            ungroupedFigures.addAll(ungroupFigures(view, group));
            fireUndoableEditHappened(edit);
        }
    }

    public Collection<Figure> ungroupFigures(DrawingView view, CompositeFigure group) {
        Drawing drawing = view.getDrawing();
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        drawing.basicAddAll(drawing.indexOf(group), figures);
        drawing.remove(group);
        view.addToSelection(figures);
        return figures;
    }

    // Extracted from anonymous inner class to eliminate Anonymous Inner Class smell
    private class UngroupUndoableEdit extends AbstractUndoableEdit {

        private static final long serialVersionUID = 1L;
        private final DrawingView view;
        private final CompositeFigure group;

        UngroupUndoableEdit(DrawingView view, CompositeFigure group) {
            this.view = view;
            this.group = group;
        }

        @Override
        public String getPresentationName() {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            return labels.getString("edit.ungroupSelection.text");
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            ungroupFigures(view, group);
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
        }
    }
}
