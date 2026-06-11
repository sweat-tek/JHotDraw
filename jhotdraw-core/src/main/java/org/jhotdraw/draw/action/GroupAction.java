/*
 * @(#)GroupAction.java
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

public class GroupAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.groupSelection";
    private CompositeFigure prototype;

    public GroupAction(DrawingEditor editor) {
        this(editor, new GroupFigure());
    }

    public GroupAction(DrawingEditor editor, CompositeFigure prototype) {
        super(editor);
        this.prototype = prototype;
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(canGroup(getView()));
    }

    protected boolean canGroup() {
        return canGroup(getView());
    }

    protected boolean canGroup(DrawingView v) {
        return v != null && v.getSelectionCount() > 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canGroup()) {
            final DrawingView view = getView();
            final LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
            assert prototype.clone() instanceof CompositeFigure : "prototype.clone() must be a CompositeFigure";
            final CompositeFigure group = (CompositeFigure) prototype.clone();

            UndoableEdit edit = new GroupUndoableEdit(view, group, ungroupedFigures);
            groupFigures(view, group, ungroupedFigures);
            fireUndoableEditHappened(edit);
        }
    }

    public void groupFigures(DrawingView view, CompositeFigure group, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        Collection<Figure> sorted = drawing.sort(figures);
        int index = drawing.indexOf(sorted.iterator().next());
        drawing.basicRemoveAll(figures);
        view.clearSelection();
        drawing.add(index, group);
        group.willChange();
        for (Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }

    // Extracted from anonymous inner class to eliminate Anonymous Inner Class smell
    private class GroupUndoableEdit extends AbstractUndoableEdit {

        private static final long serialVersionUID = 1L;
        private final DrawingView view;
        private final CompositeFigure group;
        private final LinkedList<Figure> ungroupedFigures;

        GroupUndoableEdit(DrawingView view, CompositeFigure group, LinkedList<Figure> ungroupedFigures) {
            this.view = view;
            this.group = group;
            this.ungroupedFigures = ungroupedFigures;
        }

        @Override
        public String getPresentationName() {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            return labels.getString("edit.groupSelection.text");
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            groupFigures(view, group, ungroupedFigures);
        }

        @Override
        public void undo() throws CannotUndoException {
            Drawing drawing = view.getDrawing();
            LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
            view.clearSelection();
            group.basicRemoveAllChildren();
            drawing.basicAddAll(drawing.indexOf(group), figures);
            drawing.remove(group);
            view.addToSelection(figures);
            super.undo();
        }
    }
}
