package org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class GivenAnRedoActionTest extends ScenarioTest<GivenAnRedoAction, WhenInitialized, ThenExists> {
    @Test
    public void testScenario(){
        given().givenAnAction();
        when().whenInitialized();
        then().thenExists();
    }
}
