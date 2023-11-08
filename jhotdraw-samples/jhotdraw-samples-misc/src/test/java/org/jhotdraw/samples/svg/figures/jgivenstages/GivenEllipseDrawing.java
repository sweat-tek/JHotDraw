package org.jhotdraw.samples.svg.figures.jgivenstages;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.QuadTreeDrawing;

public class GivenEllipseDrawing extends Stage<GivenEllipseDrawing> {

    @ProvidedScenarioState
    Drawing drawing;

    public void the_user_has_selected_the_ellipse_drawing_tool() {
        this.drawing = new QuadTreeDrawing();
    }
}
