package org.jhotdraw.samples.svg.figures;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.*;


import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;




public class WhenDrawRectangle extends Stage<WhenDrawRectangle> {

    @ExpectedScenarioState
    int width;

    @ExpectedScenarioState
    SVGRectFigure rect;

    @ExpectedScenarioState
    Graphics2D g;

    @ScenarioState
    Shape shape;

    public WhenDrawRectangle user_draws_rectangle() {
        assertNotEquals(0, width);
        assertNotNull(rect);
        assertNotNull(g);

        shape = new Rectangle2D.Double(0,0,width,width);

        rect.drawFill(g);
        rect.drawStroke(g);

        return this;
    }
}