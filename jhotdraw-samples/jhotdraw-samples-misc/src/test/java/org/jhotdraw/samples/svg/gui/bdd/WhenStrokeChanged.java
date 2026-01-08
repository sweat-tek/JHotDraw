package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.Stage;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.RectangleFigure;


public class WhenStrokeChanged extends Stage<WhenStrokeChanged> {

    @ExpectedScenarioState
    RectangleFigure figure;

    public WhenStrokeChanged the_stroke_width_is_changed_to(double width) {
        figure.set(STROKE_WIDTH, width);
        return self();
    }
}