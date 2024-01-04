package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.figures.jgivenstages.GivenRectangle;
import org.jhotdraw.samples.svg.figures.jgivenstages.ThenRectangle;
import org.jhotdraw.samples.svg.figures.jgivenstages.WhenRectangle;
import org.junit.Test;

public class SVGRectFigureBDDTest extends ScenarioTest<GivenRectangle, WhenRectangle, ThenRectangle> {

    @Test
    public void drawRectangle() {
        given().empty_canvas();
        when().a_rectangle_is_drawn();
        then().canvas_contains_rectangle();
    }
}