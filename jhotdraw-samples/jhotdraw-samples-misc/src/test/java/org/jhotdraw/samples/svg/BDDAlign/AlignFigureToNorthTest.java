package org.jhotdraw.samples.svg.BDDAlign;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class AlignFigureToNorthTest extends ScenarioTest<GivenFigure, WhenAligned, ThenAligned> {

    @Test
    public void figures_are_aligned_to_north_when_align_north_is_clicked() {
        given().a_drawing_with_two_figures();
        when().align_north_button_is_clicked();
        then().line_has_moved_north();
    }
}

