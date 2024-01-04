package org.jhotdraw.samples.svg.figures;

import com.tngtech.jgiven.junit.ScenarioTest;

import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.samples.svg.figures.BDD.GivenFigure;
import org.jhotdraw.samples.svg.figures.BDD.ThenTextHolder;
import org.jhotdraw.samples.svg.figures.BDD.WhenChangingText;

public abstract class ChangeTextTest<T extends TextHolderFigure> extends ScenarioTest<GivenFigure, WhenChangingText, ThenTextHolder> {
    public void moveFigure(T t) {
        String testString = "Random test string!";
        given().provideFigure(t);
        when().changingTextTo(testString);
        then().textHolderTextHasChangedTo(testString);
    }
}
