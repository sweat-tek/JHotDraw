package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.BasicEdit.GivenDelete;
import org.jhotdraw.samples.svg.BasicEdit.WhenDelete;
import org.jhotdraw.samples.svg.BasicEdit.ThenDelete;
import org.junit.Test;

public class DeleteDrawingBDDTest extends ScenarioTest<GivenDelete, WhenDelete, ThenDelete> {
        @Test
        public void deleteDrawing() {
            given().EmptyDrawing();
            when().deletingTheDrawing();
            then().theDrawingIsDeleted();
        }




}
