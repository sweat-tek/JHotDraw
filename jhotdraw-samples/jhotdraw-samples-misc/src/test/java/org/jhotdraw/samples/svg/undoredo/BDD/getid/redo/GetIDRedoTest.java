package org.jhotdraw.samples.svg.undoredo.BDD.getid.redo;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class GetIDRedoTest extends ScenarioTest<GivenARedoAction, WhenGetIDIsCalled, ThenTheInsertedIDIsReturned> {
    @Test
    public void testScenario1() {
        given().aRedoAction();
        when().whenGetIDIsCalled();
        then().thenTheInsertedIDIsReturned();
    }
}
