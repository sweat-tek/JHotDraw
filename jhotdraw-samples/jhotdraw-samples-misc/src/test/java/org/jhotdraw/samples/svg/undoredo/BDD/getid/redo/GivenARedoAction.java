package org.jhotdraw.samples.svg.undoredo.BDD.getid.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class GivenARedoAction extends Stage<GivenARedoAction> {

    @ExpectedScenarioState
    private RedoAction redoAction;
    private Application app;
    private View view;


    public GivenARedoAction aRedoAction() {
        app = Mockito.mock(Application.class);
        view = Mockito.mock(View.class);

        redoAction = new RedoAction(app, view);

        return self();
    }
}
