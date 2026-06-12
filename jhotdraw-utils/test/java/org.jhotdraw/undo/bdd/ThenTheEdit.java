package org.jhotdraw.undo.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Quoted;
import org.assertj.swing.fixture.FrameFixture;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenTheEdit extends Stage<ThenTheEdit> {
    @ExpectedScenarioState
    UndoRedoTestFrame frame;

    @ExpectedScenarioState
    FrameFixture window;

    public ThenTheEdit my_edit_should_be_undone() {
        // The "Hello" edit must have been removed from the document.
        assertThat(frame.documentText())
                .as("document after undo")
                .isEmpty();
        return self();
    }

    public ThenTheEdit my_edit_should_be_redone() {
        // The previously-undone "Hello" edit must be re-applied.
        assertThat(frame.documentText())
                .as("document after redo")
                .isEqualTo("Hello");
        return self();
    }

    public ThenTheEdit the_document_should_contain(@Quoted String expected) {
        assertThat(frame.documentText()).isEqualTo(expected);
        return self();
    }

    @AfterScenario
    public void tearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }
}