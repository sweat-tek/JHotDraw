/*
 * Copyright (C) 2023 JHotDraw.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
