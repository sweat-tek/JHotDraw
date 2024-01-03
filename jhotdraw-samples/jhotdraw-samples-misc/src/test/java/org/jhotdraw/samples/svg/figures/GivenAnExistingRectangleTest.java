package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.figures.BDD.GivenAnExistingRectangle;
import org.jhotdraw.samples.svg.figures.BDD.ThenResize;
import org.jhotdraw.samples.svg.figures.BDD.WhenUserSelectsRectangleCorner;
import org.junit.Test;


public class GivenAnExistingRectangleTest extends ScenarioTest<GivenAnExistingRectangle, WhenUserSelectsRectangleCorner, ThenResize> {
    @Test
    public void testScenario(){
        given().givenAnExistingRectangle();
        when().whenUserSelectsRectangleCorner();
        when().whenMouseIsMoved();
        then().thenResize();
    }

}