/*
 * @(#)UngroupAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.GroupFigure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

public class UngroupAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.ungroupSelection";
    private CompositeFigure prototype;

    public UngroupAction(DrawingEditor editor) {
        super(editor);
        this.prototype = new GroupFigure();
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
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
        if (getView() != null) {
            setEnabled(canUngroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canUngroup() {
        return getView() != null
                && getView().getSelectionCount() == 1
                && prototype != null
                && getView().getSelectedFigures().iterator().next().getClass().equals(
                        prototype.getClass());
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (canUngroup()) {
            final DrawingView view = getView();
            final CompositeFigure group = (CompositeFigure) getView().getSelectedFigures().iterator().next();
            final LinkedList<Figure> ungroupedFigures = new LinkedList<>();
            UndoableEdit edit = new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

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
                    // Note: This relies on groupFigures from the view, we will address this if needed
                    super.undo();
                }
            };
            ungroupedFigures.addAll(ungroupFigures(view, group));
            fireUndoableEditHappened(edit);
        }
    }

    public Collection<Figure> ungroupFigures(DrawingView view, CompositeFigure group) {
        LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
        view.getDrawing().remove(group);
        view.addToSelection(figures);
        return figures;
    }
}