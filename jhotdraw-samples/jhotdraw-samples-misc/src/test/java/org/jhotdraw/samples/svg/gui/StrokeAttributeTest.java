package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.figure.RectangleFigure;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

public class StrokeAttributeTest {

    @Test
    public void testStrokeWidthBestCase() {
        RectangleFigure figure = new RectangleFigure();
        double width = 5.0;

        figure.set(STROKE_WIDTH, width);

        assertEquals(width, figure.get(STROKE_WIDTH), 0.0001);
    }

    @Test
    public void testStrokeColorBestCase() {
        RectangleFigure figure = new RectangleFigure();
        Color color = Color.BLUE;

        figure.set(STROKE_COLOR, color);

        assertEquals(color, figure.get(STROKE_COLOR));
    }

    @Test
    public void testStrokeWidthZeroBoundary() {
        RectangleFigure figure = new RectangleFigure();
        double width = 0.0;

        figure.set(STROKE_WIDTH, width);

        assertEquals(width, figure.get(STROKE_WIDTH), 0.0001);
    }

    @Test
    public void testStrokeOpacityUpperBoundary() {
        RectangleFigure figure = new RectangleFigure();
        double opacity = 1.0;

        figure.set(STROKE_OPACITY, opacity);

        assertEquals(opacity, figure.get(STROKE_OPACITY), 0.0001);
    }

    @Test
    public void testStrokeWidthInvariant() {
        RectangleFigure figure = new RectangleFigure();
        double width = 3.0;

        figure.set(STROKE_WIDTH, width);

        assert figure.get(STROKE_WIDTH) >= 0 : "Stroke width must never be negative";
    }
}
