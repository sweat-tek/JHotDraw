package org.jhotdraw.draw.action;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class FillSelectionWithColorAcceptanceTest
        extends ScenarioTest<GivenSelectedFigure, WhenColorChooser, ThenFigureColored> {

    @Test
    public void applyColorToSelectedFigure() {
        given()
                .the_user_creates_a_figure()
                .the_user_selects_a_figure();
        when()
                .the_user_chooses_a_color();
        then()
                .the_color_is_applied_to_the_selected_figure();
    }

}
