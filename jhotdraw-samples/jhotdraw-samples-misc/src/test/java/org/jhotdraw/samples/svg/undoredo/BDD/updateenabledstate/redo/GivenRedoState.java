package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.action.edit.UndoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class GivenRedoState extends Stage<GivenRedoState> {

    @ExpectedScenarioState
    private RedoAction redoAction;

    private Application app;
    private View view;

    public GivenRedoState anRedoAction() {
        app = Mockito.mock(Application.class);
        view = Mockito.mock(View.class);

        redoAction = new RedoAction(app, view);
        redoAction.getActiveView().getActionMap().put(redoAction.ID, redoAction);
        redoAction.getActiveView().getActionMap().put("edit.undo", new UndoAction(app, view));

        return self();
    }
}
