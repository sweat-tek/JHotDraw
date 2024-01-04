package org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;

public class GivenAnRedoAction extends Stage<GivenAnRedoAction> {

    @ExpectedScenarioState
    private RedoAction redoAction;

    public GivenAnRedoAction givenAnAction() {
        return self();
    }
}
