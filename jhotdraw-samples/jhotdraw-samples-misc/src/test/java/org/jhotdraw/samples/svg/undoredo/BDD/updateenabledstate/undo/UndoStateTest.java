package org.jhotdraw.samples.svg.undoredo.BDD.updateenabledstate.undo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class UndoStateTest extends ScenarioTest<GivenUndoState, WhenStateUpdated, ThenEnabledState> {
    @Test
    public void testScenario1() {
        given().anUndoAction();
        when().actionIsNull();
        then().isFalse();
    }

    @Test
    public void testScenario2() {
        given().anUndoAction();
        when().actionIsThis();
        then().isFalse();
    }

    @Test
    public void testScenario3() {
        given().anUndoAction();
        when().actionIsOther();
        then().isTrue();
    }
}
