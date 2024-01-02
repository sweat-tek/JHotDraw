package org.jhotdraw.samples.svg.undoredo.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;

public class GivenAnActionHasBeenDone extends Stage<GivenAnActionHasBeenDone> {

    @ExpectedScenarioState
    private UndoAction undoAction;

    public GivenAnActionHasBeenDone GivenTheUndoUIExists() {
        undoAction = new UndoAction(new );
        return self();
    }
}
