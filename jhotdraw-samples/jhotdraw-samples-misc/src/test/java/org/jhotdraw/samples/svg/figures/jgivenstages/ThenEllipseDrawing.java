package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ThenEllipseDrawing extends Stage<ThenEllipseDrawing> {

    @ProvidedScenarioState
    Drawing drawing;

    public void an_ellipse_shape_should_be_created_with_the_specified_dimensions() {
        List<Figure> figures = drawing.getFiguresFrontToBack();
        assertTrue(figures.stream().anyMatch(figure -> figure instanceof SVGEllipseFigure));
    }
}
