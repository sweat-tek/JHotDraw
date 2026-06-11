/*
 * @(#)GroupAction.java
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
        if (getView() != null) {
            setEnabled(canGroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        return getView() != null && getView().getSelectionCount() > 1;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (canGroup()) {
            final DrawingView view = getView();
            final LinkedList<Figure> ungroupedFigures = new LinkedList<>(view.getSelectedFigures());
            final CompositeFigure group = (CompositeFigure) prototype.clone();
            
            UndoableEdit edit = new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

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
                    // Reversing a group action
                    LinkedList<Figure> figures = new LinkedList<>(group.getChildren());
                    view.clearSelection();
                    group.basicRemoveAllChildren();
                    view.getDrawing().basicAddAll(view.getDrawing().indexOf(group), figures);
                    view.getDrawing().remove(group);
                    view.addToSelection(figures);
                    super.undo();
                }
            };
            
            groupFigures(view, group, ungroupedFigures);
            fireUndoableEditHappened(edit);
        }
    }

    public void groupFigures(DrawingView view, CompositeFigure group, Collection<Figure> figures) {
        Collection<Figure> sorted = view.getDrawing().sort(figures);
        int index = view.getDrawing().indexOf(sorted.iterator().next());
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(index, group);
        group.willChange();
        for (Figure f : sorted) {
            f.willChange();
            group.basicAdd(f);
        }
        group.changed();
        view.addToSelection(group);
    }
}