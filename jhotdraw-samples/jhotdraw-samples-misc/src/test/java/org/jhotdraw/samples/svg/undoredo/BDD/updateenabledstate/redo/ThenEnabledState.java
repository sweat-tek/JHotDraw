package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

import static org.junit.Assert.*;

public class ThenEnabledState extends Stage<ThenEnabledState> {
    @ExpectedScenarioState
    private RedoAction redoAction;

    public ThenEnabledState isTrue() {
        assertTrue("Enabled state is '" + redoAction.isEnabled() + "'." , redoAction.isEnabled());
        return self();
    }

    public ThenEnabledState isFalse() {
        assertFalse("Enabled state is '" + redoAction.isEnabled() + "'.", redoAction.isEnabled());
        return self();
    }
}
