package org.jhotdraw.samples.svg.figures;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import static org.junit.Assert.*;

/**
 *
 * @author Lorenzo Fagiani
 */
public class SVGEllipseFigureTest {
    
    private SVGEllipseFigure ellipse;
     
    @Before
    public void initialize() {
        // Initialize a new SVGEllipseFigure
        ellipse = new SVGEllipseFigure(20, 20, 30, 10);
    }
    
    @After
    public void close() {
        // Set the SVGEllipseFigure to null after the end of the test method
        ellipse = null;
    }
    
    @Test
    public void testTransform() {
        // Test the transform() method
        AffineTransform transformation = AffineTransform.getTranslateInstance(10, 10);
        ellipse.transform(transformation);

        // Verify that the figure has been transformed
        Rectangle2D.Double actualBounds = ellipse.getBounds();
        assertEquals(30.0, actualBounds.getX(), 0.01);
        assertEquals(30.0, actualBounds.getY(), 0.01);

    }

    @Test
    public void testTransformNull() {
        // Test the transform() method with a null argument
        AffineTransform nullTransform = ellipse.get(TRANSFORM);
        // Assert that the figure has no transform before the transform() method is called
        assertNull(nullTransform);

    }
    
}
