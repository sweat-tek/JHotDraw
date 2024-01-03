package org.jhotdraw.samples.svg.undoredo.BDD.updateview.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class GivenAView extends Stage<GivenAView> {

    @ExpectedScenarioState
    private UndoAction undoAction;
    @ExpectedScenarioState
    private View viewOld;
    @ExpectedScenarioState
    private View viewNew;
    private Application app;


    public GivenAView aView() {
        app = Mockito.mock(Application.class);
        viewOld = Mockito.mock(View.class);

        undoAction = new UndoAction(app, viewOld);

        return self();
    }
}
