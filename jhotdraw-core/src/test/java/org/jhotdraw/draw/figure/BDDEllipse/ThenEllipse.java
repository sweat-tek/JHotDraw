package org.jhotdraw.draw.figure.BDDEllipse;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.EllipseFigure;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenEllipse extends Stage<ThenEllipse> {

    @ExpectedScenarioState
    EllipseFigure ellipse;

    public ThenEllipse ellipse_should_have_size(double width, double height) {
        assertThat(ellipse.getBounds().width).isEqualTo(width);
        assertThat(ellipse.getBounds().height).isEqualTo(height);
        return self();
    }
}

