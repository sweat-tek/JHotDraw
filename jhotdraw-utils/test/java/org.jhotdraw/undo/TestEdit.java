package org.jhotdraw.undo

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

class TestEdit extends AbstractUndoableEdit {
    private static final long serialVersionUID = 1L;
    private final StringBuilder model; private final String token;
    private final boolean significant;
    int undoCount = 0, redoCount = 0, dieCount = 0;

    TestEdit(StringBuilder model, String token) { this(model, token, true); }

    TestEdit(StringBuilder model, String token, boolean significant) {
        this.model = model;
        this.token = token;
        this.significant = significant;
        this.model.append(token); // simulate the original "do"
    }

    @Override public void undo() throws CannotUndoException {
        super.undo();
        undoCount++;
        int idx = model.lastIndexOf(token);
        if (idx >= 0) model.delete(idx, idx + token.length());
    }

    @Override public void redo() throws CannotRedoException {
        super.redo();
        redoCount++;
        model.append(token);
    }

    @Override public void die() { super.die(); dieCount++; }
    @Override public boolean isSignificant() { return significant; }
    @Override public String getPresentationName() { return token; }
}