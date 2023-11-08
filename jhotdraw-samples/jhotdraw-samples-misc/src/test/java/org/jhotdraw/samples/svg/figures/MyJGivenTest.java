package org.jhotdraw.samples.svg.figures;

import org.junit.Test;
import com.tngtech.jgiven.junit.ScenarioTest;

public class MyJGivenTest extends ScenarioTest<GivenInitializedRectangle, WhenDrawRectangle, ThenRectangleDrawn> {

    @Test
    public void rectangle_should_be_drawn() {

        given().user_wants_to_draw_rectangle();
        when().user_draws_rectangle();
        then().rectangle_should_appear();
    }
}
