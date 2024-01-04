package org.jhotdraw.samples.svg.figures;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

public class SVGTextAreaFigureTest {

    private SVGTextAreaFigure figure;

    @Before
    public void setUp() {
        this.figure = new SVGTextAreaFigure("Test text");
    }


    @Test
    public void testGetText() {
        assertEquals("Test text", figure.getText());
    }

    @Test
    public void testSetFont() {
        figure.setFontSize(20);
        assertEquals(20, figure.getFontSize(), 0.1);
    }

    @Test
    public void testDefaultFont() {
        Font font = figure.getFont();
        assertEquals("VERDANA", font.getName());
    }

    @Test
    public void testSetBounds() {
        Point2D.Double anchor = new Point2D.Double(10,10);
        Point2D.Double lead = new Point2D.Double(100, 100);
        figure.setBounds(anchor, lead);
        Rectangle2D bounds = figure.getBounds();
        assertEquals(10, bounds.getX(), 0.1);
        assertEquals(10, bounds.getY(), 0.1);
        assertEquals(90, bounds.getHeight(), 0.1);
        assertEquals(90, bounds.getWidth(), 0.1);
    }
}
