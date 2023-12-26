package org.jhotdraw.samples.svg.BDDAlign;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.assertj.swing.assertions.Assertions;
import org.assertj.swing.fixture.FrameFixture;
import org.jhotdraw.draw.figure.LineFigure;

import java.awt.geom.Point2D;

public class ThenAligned extends Stage<ThenAligned> {
    @ExpectedScenarioState
    private FrameFixture window;

    public ThenAligned line_has_moved_north() {
        TestApplication frame = (TestApplication) window.target();
        LineFigure lineFigure = (LineFigure) frame.getDrawing().getChildren().get(0);

        Point2D.Double newStart = lineFigure.getStartPoint();
        Point2D.Double newEnd = lineFigure.getEndPoint();

        Assertions.assertThat(newStart).isNotEqualTo(GivenFigure.initialStart);
        Assertions.assertThat(newEnd).isNotEqualTo(GivenFigure.initialEnd);
        return self();
    }
}
