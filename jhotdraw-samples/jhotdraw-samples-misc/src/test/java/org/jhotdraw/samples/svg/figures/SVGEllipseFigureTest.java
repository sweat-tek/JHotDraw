package org.jhotdraw.samples.svg.figures;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.junit.Assert.*;

public class SVGEllipseFigureTest {

    private SVGEllipseFigure ellipseFigure;

    @Before
    public void setUp() {
        // Initialize a new SVGEllipseFigure for each test method
        ellipseFigure = new SVGEllipseFigure(10, 10, 30, 20);
    }

    @After
    public void tearDown() {
        // Set the SVGEllipseFigure to null after each test method
        ellipseFigure = null;
    }

    @Test
    public void testTransformBestCase() {
        // Test the transform() method with a best-case scenario
        AffineTransform tx = AffineTransform.getTranslateInstance(5, 5);
        ellipseFigure.transform(tx);

        // Verify that the figure has been transformed correctly
        Rectangle2D.Double bounds = ellipseFigure.getBounds();
        assertEquals(15.0, bounds.getX(), 0.01);
        assertEquals(15.0, bounds.getY(), 0.01);

    }

    @Test
    public void testTransformNull() {
        // Test the transform() method with a null argument
        AffineTransform prevTransform = ellipseFigure.get(TRANSFORM);
        // Assert that the figure has no transform before the transform() method is called
        assertNull(prevTransform);

    }

    @Test
    public void testTransformStackable() {
        // Made another ellipseFigure to test if the transform() method is stackable
        SVGEllipseFigure ellipseFigure2 = new SVGEllipseFigure(10, 10, 35, 25);
        // Adds 5 to the x and y coordinates of both ellipseFigures
        AffineTransform tx = AffineTransform.getTranslateInstance(5, 5);

        // Transforms both ellipseFigures
        ellipseFigure.transform(tx);
        ellipseFigure2.transform(tx);
        // Verify that the figure has been transformed correctly
        Rectangle2D.Double bounds = ellipseFigure.getBounds();
        Rectangle2D.Double bounds2 = ellipseFigure2.getBounds();
        assertEquals(15.0, bounds.getX(), 0.01);
        assertEquals(15.0, bounds.getY(), 0.01);
        assertEquals(15.0, bounds2.getX(), 0.01);
        assertEquals(15.0, bounds2.getY(), 0.01);

    }
}