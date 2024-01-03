package org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

import static org.junit.Assert.*;

public class ThenExists extends Stage<ThenExists> {
    @ExpectedScenarioState
    private RedoAction redoAction;

    public ThenExists thenExists() {
        // Not null:
        assertNotNull("The redoAction is null.", redoAction);

        // Check ID:
        assertEquals("The id is '" + redoAction.ID + "', not 'edit.redo'.", "edit.redo", "" + redoAction.ID);

        return self();
    }
}
