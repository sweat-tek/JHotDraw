package org.jhotdraw.samples.svg.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGTextFigure;

import java.util.List;

import static com.tngtech.jgiven.impl.util.AssertionUtil.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhenTextFigureTest extends Stage<WhenTextFigureTest> {
    @ScenarioState
    Drawing drawing;

    public WhenTextFigureTest aTextToolIsDrawn() {
        assertNotNull(drawing);
        drawing.add(new SVGTextFigure());
        return this;
    }

    public WhenTextFigureTest theTextToolIsRotated() {
        assertNotNull(drawing);
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertEquals(figures.size(), 1);
        Figure figure = figures.get(0);
        assertTrue(figure instanceof SVGTextFigure);
        SVGTextFigure textFigure = (SVGTextFigure) figure;
        textFigure.setRotates(new double[]{90});
        return this;
    }
}
