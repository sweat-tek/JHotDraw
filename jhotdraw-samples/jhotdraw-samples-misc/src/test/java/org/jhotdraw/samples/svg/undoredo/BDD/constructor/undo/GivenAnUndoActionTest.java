package org.jhotdraw.samples.svg.undoredo.BDD.constructor.undo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo.ThenExists;
import org.jhotdraw.samples.svg.undoredo.BDD.constructor.redo.WhenInitialized;
import org.junit.Test;

public class GivenAnUndoActionTest extends ScenarioTest<GivenAnUndoAction, WhenInitialized, ThenExists> {
    @Test
    public void testScenario(){
        given().givenAnAction();
        when().whenInitialized();
        then().thenExists();
    }
}
