package org.jhotdraw.samples.svg.figures;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.Assert.*;

public class tsThenRectangleDrawn extends Stage<tsThenRectangleDrawn> {

    @ExpectedScenarioState
    Graphics2D g;

    @ExpectedScenarioState
    Shape shape;

    @ExpectedScenarioState
    SVGRectFigure rect;

    public tsThenRectangleDrawn rectangle_should_appear() {

        assertNotNull(g);
        assertNotNull(shape);
        assertNotNull(rect);

        assertEquals(100, rect.getWidth(), 0.01);
        assertEquals(100, rect.getHeight(), 0.01);
        Mockito.verify(g, Mockito.times(1)).fill(shape);
        Mockito.verify(g, Mockito.times(1)).draw(shape);

        return this;
    }
}