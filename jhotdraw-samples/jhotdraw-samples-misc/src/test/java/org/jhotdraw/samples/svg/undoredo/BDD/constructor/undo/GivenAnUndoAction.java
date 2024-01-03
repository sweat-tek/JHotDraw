package org.jhotdraw.samples.svg.undoredo.BDD.constructor.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

public class GivenAnUndoAction extends Stage<GivenAnUndoAction> {

    @ExpectedScenarioState
    private UndoAction undoAction;

    public GivenAnUndoAction givenAnAction() {
        return self();
    }
}
