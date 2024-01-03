package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThenEnabledState extends Stage<ThenEnabledState> {
    @ExpectedScenarioState
    private UndoAction undoAction;

    public ThenEnabledState isTrue() {
        assertTrue("Enabled state is '" + undoAction.isEnabled() + "'." , undoAction.isEnabled());
        return self();
    }

    public ThenEnabledState isFalse() {
        assertFalse("Enabled state is '" + undoAction.isEnabled() + "'.", undoAction.isEnabled());
        return self();
    }
}
