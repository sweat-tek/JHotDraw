package org.jhotdraw.samples.svg.undoredo.BDD.updateview.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ThenViewIsUpdated extends Stage<ThenViewIsUpdated> {
    @ExpectedScenarioState
    private UndoAction undoAction;
    @ExpectedScenarioState
    private View viewOld;
    @ExpectedScenarioState
    private View viewNew;

    public ThenViewIsUpdated isNew() {
        assertEquals(undoAction.getActiveView(), viewNew);
        assertNotEquals(undoAction.getActiveView(), viewOld);
        return self();
    }

    public ThenViewIsUpdated isOld() {
        assertEquals(undoAction.getActiveView(), viewOld);
        assertNotEquals(undoAction.getActiveView(), viewNew);
        return self();
    }
}
