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
    public void setRotationTest() {
        // Arrange
        double[] rotation = new double[]{90.0};

        // Act
        textFigure.setRotates(rotation);

        //Assert
        assertArrayEquals(textFigure.getRotates(), rotation, 0.1);
    }

    @Test
    public void moveTest() {
        // Arrange
        Point2D.Double[] coordinates = new Point2D.Double[]{
                new Point2D.Double(100, 100)
        };

        // Act
        textFigure.setCoordinates(coordinates);

        //Assert
        assertArrayEquals(textFigure.getCoordinates(), coordinates);
    }
}