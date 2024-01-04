package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;

public class SVGEllipseFigureTest {
    @Test
    public void testNewDefaultSVGEllipseFigure() {
        SVGEllipseFigure ellipse = new SVGEllipseFigure();

        assertEquals(0, ellipse.getX(), 1e-10);
        assertEquals(0, ellipse.getY(), 1e-10);
        assertEquals(0, ellipse.getWidth(), 1e-10);
        assertEquals(0, ellipse.getHeight(), 1e-10);
    }

    @Test
    public void testNewSVGEllipseFigure() {
        double x = 10, y = 20, radius = 4;
        SVGEllipseFigure ellipse = new SVGEllipseFigure(x, y, radius * 2, radius * 2);

        assertEquals(x, ellipse.getX(), 1e-10);
        assertEquals(y, ellipse.getY(), 1e-10);
        assertEquals(radius * 2, ellipse.getWidth(), 1e-10);
        assertEquals(radius * 2, ellipse.getHeight(), 1e-10);

    }

    @Test
    public void testGetDrawingArea() {
        SVGEllipseFigure ellipse = new SVGEllipseFigure(10, 20, 8, 8);

        Rectangle2D.Double drawingArea = ellipse.getDrawingArea();
        Rectangle2D.Double expected = new Rectangle2D.Double(9, 19, 10, 10);
        assertEquals(expected, drawingArea);

        // it should return the same result as transformedShape is cached
        drawingArea = ellipse.getDrawingArea();
        assertEquals(expected, drawingArea);
    }

    @Test
    public void testContainsInside() {
        SVGEllipseFigure ellipse = new SVGEllipseFigure(10, 20, 8, 8);

        Point2D.Double point = new Point2D.Double(11, 21);
        assertTrue(ellipse.contains(point));
    }

    @Test
    public void testContainsOn() {
        SVGEllipseFigure ellipse = new SVGEllipseFigure(10, 20, 8, 8);

        Point2D.Double point = new Point2D.Double(14, 20);
        assertTrue(ellipse.contains(point));
    }

    @Test
    public void testContainsOutside() {
        SVGEllipseFigure ellipse = new SVGEllipseFigure(10, 20, 8, 8);

        Point2D.Double point = new Point2D.Double(35, 40);
        assertFalse(ellipse.contains(point));
    }
}
