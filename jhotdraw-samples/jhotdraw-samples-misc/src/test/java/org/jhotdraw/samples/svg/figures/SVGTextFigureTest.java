package org.jhotdraw.samples.svg.figures;

import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class SVGTextFigureTest {
    SVGTextFigure textFigure;

    @Before
    public void setUp() throws Exception {
        textFigure = new SVGTextFigure();
    }

    @Test
    public void setCoordinatesTest() {
        // Arrange
        Point2D.Double[] point = new Point2D.Double[]{
                new Point2D.Double(1, 2),
                new Point2D.Double(3, 4),
                new Point2D.Double(5, 6),
                new Point2D.Double(7, 8)
        };

        // Act
        textFigure.setCoordinates(point);

        //Assert
        assertArrayEquals(textFigure.getCoordinates(), point);
    }

    @Test
    public void containsTest() {
        
    }
}