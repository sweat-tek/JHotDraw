package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.bdd.GivenTextFigureTest;
import org.jhotdraw.samples.svg.bdd.ThenTextFigureTest;
import org.jhotdraw.samples.svg.bdd.WhenTextFigureTest;
import org.junit.Test;

public class TextToolBDDTest extends ScenarioTest<GivenTextFigureTest, WhenTextFigureTest, ThenTextFigureTest> {
    @Test
    public void drawTexTool() {
        given().anEmptyDrawing();
        when().aTextToolIsDrawn();
        then().theDrawingContainsATextBox();
    }

    @Test
    public void rotateTextTool() {
        given().aDrawing().and().aText();
        when().theTextToolIsRotated();
        then().theTextBoxHasTheCorrectRotation();
    }
}
