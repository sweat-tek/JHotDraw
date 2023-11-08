package org.jhotdraw.samples.svg.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGTextFigure;

import java.util.List;

import static com.tngtech.jgiven.impl.util.AssertionUtil.assertNotNull;
import static org.junit.Assert.*;

public class ThenTextFigureTest extends Stage<ThenTextFigureTest> {
    @ScenarioState
    Drawing drawing;

    public ThenTextFigureTest theDrawingContainsATextBox() {
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertTrue(figures.stream().anyMatch(figure -> figure instanceof SVGTextFigure));
        return this;
    }

    public ThenTextFigureTest theTextBoxHasTheCorrectRotation() {
        assertNotNull(drawing);
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertEquals(1, figures.size());
        Figure figure = figures.get(0);
        assertTrue(figure instanceof SVGTextFigure);
        SVGTextFigure textFigure = (SVGTextFigure) figure;
        double[] rotation = textFigure.getRotates();
        assertArrayEquals(new double[]{90}, rotation, 0.1);
        return this;
    }
}
