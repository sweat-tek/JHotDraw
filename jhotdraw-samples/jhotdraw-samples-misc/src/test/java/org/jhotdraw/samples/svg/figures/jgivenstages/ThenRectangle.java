package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ThenRectangle extends Stage<ThenRectangle> {

    @ScenarioState
    Drawing drawing;

    public ThenRectangle canvas_contains_rectangle() {
        List<Figure> figure = drawing.getFiguresFrontToBack();
        assertTrue(figure.stream().anyMatch(f -> f instanceof SVGRectFigure));
        return this;
    }
}
