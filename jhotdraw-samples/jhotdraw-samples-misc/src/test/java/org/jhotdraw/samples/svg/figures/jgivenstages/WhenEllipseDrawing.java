package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;

import static org.junit.Assert.assertNotNull;

public class WhenEllipseDrawing extends Stage<WhenEllipseDrawing> {
    @ProvidedScenarioState
    Drawing drawing;

    public void the_user_clicks_and_drags_the_mouse_to_create_an_ellipse_on_the_drawing_canvas() {
        assertNotNull(drawing);
        drawing.add(new SVGEllipseFigure());
    }
}
