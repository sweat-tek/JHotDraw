package org.jhotdraw.draw.figure.BDDEllipse;

import com.tngtech.jgiven.junit5.ScenarioTest;
import org.junit.jupiter.api.Test;


public class EllipseFigureBDDTest extends
        ScenarioTest<GivenEllipse, WhenSettingBounds, ThenEllipse> {

    @Test
    public void ellipse_resizes_when_bounds_are_set() {
        given().an_ellipse();
        when().bounds_are_set(0, 0, 100, 50);
        then().ellipse_should_have_size(100, 50);
    }
}
