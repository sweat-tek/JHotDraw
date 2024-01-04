package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class GivenUndoState extends Stage<GivenUndoState> {

    @ExpectedScenarioState
    private UndoAction undoAction;

    private Application app;
    private View view;

    public GivenUndoState anUndoAction() {
        app = Mockito.mock(Application.class);
        view = Mockito.mock(View.class);

        undoAction = new UndoAction(app, view);
        undoAction.getActiveView().getActionMap().put(undoAction.ID, undoAction);
        undoAction.getActiveView().getActionMap().put("edit.undo", new UndoAction(app, view));

        return self();
    }
}
