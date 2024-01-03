package org.jhotdraw.samples.svg.undoredo.BDD.getid.undo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class GetIDUndoTest extends ScenarioTest<GivenARedoAction, WhenGetIDIsCalled, ThenTheInsertedIDIsReturned> {
    @Test
    public void testScenario1() {
        given().aUndoAction();
        when().whenGetIDIsCalled();
        then().thenTheInsertedIDIsReturned();
    }
}
