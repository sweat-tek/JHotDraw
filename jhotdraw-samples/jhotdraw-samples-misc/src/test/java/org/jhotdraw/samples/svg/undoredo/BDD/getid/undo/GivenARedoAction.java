package org.jhotdraw.samples.svg.undoredo.BDD.getid.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class GivenARedoAction extends Stage<GivenARedoAction> {

    @ExpectedScenarioState
    private UndoAction undoAction;
    private Application app;
    private View view;


    public GivenARedoAction aUndoAction() {
        app = Mockito.mock(Application.class);
        view = Mockito.mock(View.class);

        undoAction = new UndoAction(app, view);

        return self();
    }
}
