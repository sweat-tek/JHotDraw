package org.jhotdraw.samples.svg.undoredo.BDD.constructor.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

import static org.junit.Assert.*;

public class ThenExists extends Stage<ThenExists> {
    @ExpectedScenarioState
    private UndoAction undoAction;

    public ThenExists thenExists() {
        // Not null:
        assertNotNull("The undoAction is null.", undoAction);

        // Check ID:
        assertEquals("The id is '" + undoAction.ID + "', not 'edit.undo'.", "edit.undo", "" + undoAction.ID);

        return self();
    }
}
