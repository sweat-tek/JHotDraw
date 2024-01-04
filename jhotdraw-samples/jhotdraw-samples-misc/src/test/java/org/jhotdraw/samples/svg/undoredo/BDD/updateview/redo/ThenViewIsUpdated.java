package org.jhotdraw.samples.svg.undoredo.BDD.updateview.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.api.app.View;

import static org.junit.Assert.*;

public class ThenViewIsUpdated extends Stage<ThenViewIsUpdated> {
    @ExpectedScenarioState
    private RedoAction redoAction;
    @ExpectedScenarioState
    private View viewOld;
    @ExpectedScenarioState
    private View viewNew;

    public ThenViewIsUpdated isNew() {
        assertEquals(redoAction.getActiveView(), viewNew);
        assertNotEquals(redoAction.getActiveView(), viewOld);
        return self();
    }

    public ThenViewIsUpdated isOld() {
        assertEquals(redoAction.getActiveView(), viewOld);
        assertNotEquals(redoAction.getActiveView(), viewNew);
        return self();
    }
}
