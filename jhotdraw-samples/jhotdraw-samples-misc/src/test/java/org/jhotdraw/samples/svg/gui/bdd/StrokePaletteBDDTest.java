package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;


public class StrokePaletteBDDTest
        extends ScenarioTest<GivenStrokeFigure, WhenStrokeChanged, ThenStrokeResult> {

    @Test
    public void changing_stroke_width_updates_selected_figure() {
        given().a_selected_rectangle_figure();
        when().the_stroke_width_is_changed_to(4.0);
        then().the_figure_should_have_stroke_width(4.0);
    }
}