package org.jhotdraw.undo.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.swing.fixture.FrameFixture;

public class WhenIPress extends Stage<WhenIPress> {
    @ExpectedScenarioState
    FrameFixture window;

    public WhenIPress I_press_the_undo_button() {
        window.button("undoButton").click();
        return self();
    }

    public WhenIPress I_press_the_redo_button() {
        window.button("redoButton").click();
        return self();
    }
}