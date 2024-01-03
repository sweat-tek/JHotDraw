package org.jhotdraw.samples.svg.undoredo.BDD.updateview.redo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class UpdateRedoUpdateViewTest extends ScenarioTest<GivenAView, WhenViewIsUpdated, ThenViewIsUpdated> {
    @Test
    public void testScenario1() {
        given().aView();
        when().whenViewIsUpdatedNew();
        then().isNew();
    }

    @Test
    public void testScenario2() {
        given().aView();
        when().whenViewIsUpdatedOld();
        then().isOld();
    }
}
