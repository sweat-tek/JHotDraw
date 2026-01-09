package org.jhotdraw.undo.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.undo.UndoRedoManager;
import javax.swing.undo.AbstractUndoableEdit;

public class GivenUndoRedo extends Stage<GivenUndoRedo> {
    @ProvidedScenarioState
    private UndoRedoManager undoRedoManager;

    public GivenUndoRedo i_have_edited_a_figure() {
        undoRedoManager = new UndoRedoManager();
        undoRedoManager.addEdit(new AbstractUndoableEdit() {
            @Override public String getPresentationName() { return "Edit Figure"; }
        });
        return self();
    }
}