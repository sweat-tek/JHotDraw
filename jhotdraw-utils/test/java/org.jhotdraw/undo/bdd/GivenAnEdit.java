package org.jhotdraw.undo.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;

import static org.assertj.swing.edt.GuiActionRunner.execute;

public class GivenAnEdit extends Stage<GivenAnEdit> {

    @ProvidedScenarioState
    UndoRedoTestFrame frame;

    @ProvidedScenarioState
    FrameFixture window;

    public GivenAnEdit the_application_is_running() {
        frame = GuiActionRunner.execute(UndoRedoTestFrame::new);
        window = new FrameFixture(frame);
        window.show();
        return self();
    }

    public GivenAnEdit I_have_made_an_edit() {
        the_application_is_running();
        execute(() -> frame.performEdit("Hello"));
        return self();
    }

    public GivenAnEdit I_have_undone_an_edit() {
        I_have_made_an_edit();
        // press undo once so there is something to redo
        window.button("undoButton").click();
        return self();
    }
}