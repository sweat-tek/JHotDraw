package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.RectangleFigure;

import static org.assertj.core.api.Assertions.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

public class ThenStrokeResult extends Stage<ThenStrokeResult> {

    @ExpectedScenarioState
    RectangleFigure figure;

    public ThenStrokeResult the_figure_should_have_stroke_width(double width) {
        assertThat(figure.get(STROKE_WIDTH))
                .as("Stroke width should be updated")
                .isEqualTo(width);
        return self();
    }
}