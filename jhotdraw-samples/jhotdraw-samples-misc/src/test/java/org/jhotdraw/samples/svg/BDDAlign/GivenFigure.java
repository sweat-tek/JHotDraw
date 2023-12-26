package org.jhotdraw.samples.svg.BDDAlign;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.LineFigure;
import org.junit.BeforeClass;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class GivenFigure extends Stage<GivenFigure> {
    public static Point2D.Double initialStart;
    public static Point2D.Double initialEnd;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @ProvidedScenarioState
    private FrameFixture window;


    public GivenFigure a_drawing_with_two_figures() {
        TestApplication frame = GuiActionRunner.execute(TestApplication::new);
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        // Create figures here
        List<Figure> figures = createFigures();
        LineFigure lf = new LineFigure();
        initialStart = new Point2D.Double(30, 20);
        initialEnd = new Point2D.Double(80, 30);
        lf.setBounds(initialStart, initialEnd);

        frame.setFiguresInView(figures);

        return self();
    }

    private List<Figure> createFigures() {
        LineFigure lf = new LineFigure();
        lf.setBounds(new Point2D.Double(30, 20), new Point2D.Double(80, 30));
        LineFigure lf2 = new LineFigure();
        lf2.setBounds(new Point2D.Double(20, 40), new Point2D.Double(110, 10));
        return Arrays.asList(lf, lf2);
    }
}
