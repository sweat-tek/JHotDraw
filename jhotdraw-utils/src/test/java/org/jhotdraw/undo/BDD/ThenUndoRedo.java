package org.jhotdraw.undo.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.undo.UndoRedoManager;
import static org.junit.Assert.*;

public class ThenUndoRedo extends Stage<ThenUndoRedo> {

    @ExpectedScenarioState
    UndoRedoManager undoRedoManager;

    public ThenUndoRedo the_undo_and_redo_history_is_preserved_in_chronological_order() {
        assertEquals("Undo Create Figure", undoRedoManager.getUndoPresentationName());
        undoRedoManager.undo();

        assertEquals("Undo Edit Figure", undoRedoManager.getUndoPresentationName());
        undoRedoManager.undo();

        assertEquals("Redo Edit Figure", undoRedoManager.getRedoPresentationName());
        undoRedoManager.redo();
        assertEquals("Redo Create Figure", undoRedoManager.getRedoPresentationName());
        undoRedoManager.redo();

        return self();
    }
}