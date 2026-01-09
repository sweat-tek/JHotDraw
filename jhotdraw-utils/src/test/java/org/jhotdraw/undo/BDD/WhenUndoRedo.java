package org.jhotdraw.undo.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.undo.UndoRedoManager;
import javax.swing.undo.AbstractUndoableEdit;

public class WhenUndoRedo extends Stage<WhenUndoRedo> {
    @ExpectedScenarioState
    private UndoRedoManager undoRedoManager;

    public WhenUndoRedo i_create_a_new_figure() {
        // Perform the next chronological action
        undoRedoManager.addEdit(new AbstractUndoableEdit() {
            @Override public String getPresentationName() { return "Create Figure"; }
        });
        return self();
    }
}