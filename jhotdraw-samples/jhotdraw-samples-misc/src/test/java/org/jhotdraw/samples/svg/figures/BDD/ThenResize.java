package org.jhotdraw.samples.svg.figures.BDD;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ThenResize extends Stage<ThenResize> {
    @ExpectedScenarioState
    private SVGRectFigure svgRectFigure;
    private Collection<Handle> handles;
    public ThenResize thenResize(){
        Rectangle2D.Double initialBounds = svgRectFigure.getBounds();
        svgRectFigure.setBounds(svgRectFigure.getStartPoint(), new Point2D.Double(initialBounds.width + 10, initialBounds.height + 10));
        Rectangle2D.Double actualBounds = svgRectFigure.getBounds();

        assertEquals("Rectangle is resized", initialBounds.width + 10, actualBounds.width, 0.01);
        assertEquals("Rectangle is resized", initialBounds.height + 10, actualBounds.height, 0.01);
        return self();
    }
}
