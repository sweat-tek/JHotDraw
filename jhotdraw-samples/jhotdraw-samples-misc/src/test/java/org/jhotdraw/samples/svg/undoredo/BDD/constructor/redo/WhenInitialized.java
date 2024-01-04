package org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.action.edit.RedoAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.mockito.Mockito;

public class WhenInitialized extends Stage<WhenInitialized> {
    @ExpectedScenarioState
    private RedoAction redoAction;
    private Application app;
    private View view;

    public WhenInitialized whenInitialized() {
        // Initialize the undo action
        app = Mockito.mock(Application.class);
        view = Mockito.mock(View.class);
        redoAction = new RedoAction(app, view);
        return self();
    }
}
