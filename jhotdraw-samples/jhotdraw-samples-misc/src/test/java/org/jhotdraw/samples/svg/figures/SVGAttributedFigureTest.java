package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import static java.lang.Math.abs;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.Mockito;


public class SVGAttributedFigureTest {

    private SVGRectFigure dummyRect;
    private final float delta = 0.0F; //allowed difference between two compared floats

    @Before
    public void setUp() {
        dummyRect = new SVGRectFigure();//todo better constructor

        Point2D.Double anchor = new Point2D.Double();
        anchor.x = 541.0;
        anchor.y = 408.5;

        //lead is a point where you stop with cursor when drawing a rectangle
        Point2D.Double lead = new Point2D.Double();
        lead.x = 1026.5;
        lead.y = 541.0;

        dummyRect.setBounds(anchor, lead);
    }

    //todo: JAVA assertions tests, think if current tests stand for best scenario, boundary cases

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
    public void shouldCreateRectangleOldRepairOrDelete() {

        /*Graphics2D g = Mockito.mock(Graphics2D.class);
        g.foregroundColor = Color.cyan;

        Mockito.when(g.setColor(Color.cyan)).thenCallRealMethod();
        g.setColor(Color.cyan);

        //foregroundcolor or paint
        //g.fill(rect.getBounds());
        //rect.drawFill(g);

        assertEquals(g.getColor(), Color.cyan);*/


        //musím najít jiný test protože musím instanciovat SVGRectFigure abych mohl zavolat drawFill a drawStroke ale zároveň mockito neumí zachitit komunikaci mezi stejnou class. Mockito zachicuje komunikaci mezi rozdílnými třídami


        //given
        Graphics2D g = Mockito.mock(Graphics2D.class);
        Paint paint = new Color(0, 255, 255);
        SVGRectFigure dummyRect = Mockito.mock(SVGRectFigure.class);


        /*Figure figure = Mockito.mock(Figure.class);
        figure.set(AttributeKeys.FILL_COLOR, Color.cyan);

        g.setPaint(paint);

        Mockito.when(figure.get(SVGAttributeKeys.FILL_OPACITY)).thenReturn(1d);
        Mockito.when(figure.get(AttributeKeys.FILL_COLOR)).thenReturn(Color.cyan);
        Mockito.when(SVGAttributeKeys.getFillPaint(figure)).thenReturn(paint); //simulate the return of paint specified by me
*/
        //when
        dummyRect.drawFill(g);
        //dummyRect.drawStroke(g);
        dummyRect.drawFigure(g);




        //then
        //Mockito.verify(g, Mockito.times(2)).setPaint(paint); //test it has been called once
        Mockito.verify(dummyRect, Mockito.times(1)).drawFill(g);
        Mockito.verify(dummyRect, Mockito.times(1)).drawStroke(g);



        //test that setPaint is called with specific parameter color as henrik does

        //test with drawFigure of SVGAttributedKeys class method (cause SVGRectFigure is boring)

        //Mockito.verify(g, Mockito.times(1)).fill(dummyRect.getBounds());


        //todo: test else part of drawFill()

        //foregroundcolor or paint
    }

    @Test
    public void shouldWorkWhenDrawingRectangleOutsideViewport() {

        //given
        dummyRect = new SVGRectFigure();

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
        dummyRect = new SVGRectFigure();

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
        assertNotEquals(0.0, bounds.width, delta); //it fails because the setter doesnt allow width to be 0 and makes it 0.1
    }

    @After
    public void tearDown() {
    }
}