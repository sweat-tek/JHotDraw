package org.jhotdraw.samples.svg.BDDAlign;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.swing.fixture.FrameFixture;

public class WhenAligned extends Stage<WhenAligned> {

    @ExpectedScenarioState
    private FrameFixture window;

    public WhenAligned align_north_button_is_clicked() {
        window.button("alignWestButton").click();
        return self();
    }
}
