package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

public abstract class AbstractZOrderAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    private final String actionId;

    protected AbstractZOrderAction(DrawingEditor editor, String actionId) {
        super(editor);
        this.actionId = actionId;
        ResourceBundleUtil labels
                = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        labels.configureAction(this, actionId);
        updateEnabledState();
    }

    protected abstract void apply(DrawingView view, Collection<Figure> figures);

    protected abstract void revert(DrawingView view, Collection<Figure> figures);

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        apply(view, figures);
        fireUndoableEditHappened(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels
                        = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
                return labels.getTextProperty(actionId);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                apply(view, figures);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                revert(view, figures);
            }
        });
    }
}
