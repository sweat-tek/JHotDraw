package org.jhotdraw.samples.svg.figures;


import org.junit.After;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

import org.mockito.Mockito;


public class tsSVGAttributedFigureTest {

    private final float delta = 0.0F; //allowed difference between two compared floats

    @Test
    public void shouldDrawRectangle() {

        //given
        Graphics2D g = Mockito.mock(Graphics2D.class);
        SVGRectFigure dummyRect = new SVGRectFigure();
        Rectangle2D.Double shape = new  Rectangle2D.Double();


        //when
        dummyRect.drawFill(g);
        dummyRect.drawStroke(g);


        //then
        Mockito.verify(g, Mockito.times(1)).fill(shape);
        Mockito.verify(g, Mockito.times(1)).draw(shape);
    }

    @Test
    public void shouldWorkWhenDrawingRectangleOutsideViewport() {

        //given
        SVGRectFigure dummyRect = new SVGRectFigure();

        Point2D.Double anchor = new Point2D.Double();
        anchor.x = -50.0;
        anchor.y = 50.0;

        //lead is a point where you stop with cursor when drawing a rectangle
        Point2D.Double lead = new Point2D.Double();
        lead.x = 500.0;
        lead.y = 50.0;


        //when
        dummyRect.setBounds(anchor, lead);
        Rectangle2D.Double bounds =  dummyRect.getBounds();


        //then
        assertEquals(abs(lead.x-anchor.x), bounds.width, delta);
    }

    @Test
    public void shouldFailWhenDrawingZeroWidthRectangle() {

        //given
        SVGRectFigure dummyRect = new SVGRectFigure();

        Point2D.Double anchor = new Point2D.Double();
        anchor.x = -50.0;
        anchor.y = 50.0;

        //lead is a point where you stop with cursor when drawing a rectangle
        Point2D.Double lead = new Point2D.Double();
        lead.x = -50.0;
        lead.y = 50.0;


        //when
        dummyRect.setBounds(anchor, lead);
        Rectangle2D.Double bounds =  dummyRect.getBounds();


        //then
        assertNotEquals(0.0, bounds.width, delta); //it fails because the setter does not allow width to be 0
    }

    @After
    public void tearDown() {
    }
}