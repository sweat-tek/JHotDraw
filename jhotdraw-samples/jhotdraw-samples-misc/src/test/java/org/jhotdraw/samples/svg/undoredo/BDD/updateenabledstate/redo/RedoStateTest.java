package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.redo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class RedoStateTest extends ScenarioTest<GivenRedoState, WhenStateUpdated, ThenEnabledState> {
    @Test
    public void testScenario1() {
        given().anRedoAction();
        when().actionIsNull();
        then().isFalse();
    }

    @Test
    public void testScenario2() {
        given().anRedoAction();
        when().actionIsThis();
        then().isFalse();
    }

    @Test
    public void testScenario3() {
        given().anRedoAction();
        when().actionIsOther();
        then().isTrue();
    }
}
