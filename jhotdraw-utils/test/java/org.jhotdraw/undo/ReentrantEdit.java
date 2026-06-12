package org.jhotdraw.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;

/** Edit that tries to register another edit while being undone (BR7). */
class ReentrantEdit extends AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;
    private final UndoRedoManager manager;
    private final TestEdit nested;

    ReentrantEdit(UndoRedoManager manager, TestEdit nested) {
        this.manager = manager;
        this.nested = nested;
    }

    @Override public void undo() throws CannotUndoException {
        super.undo();
        manager.addEdit(nested); // must be ignored & killed by the manager
    }
    @Override public boolean isSignificant() { return true; }
}