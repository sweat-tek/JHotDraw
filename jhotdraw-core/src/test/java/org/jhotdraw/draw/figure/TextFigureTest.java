package org.jhotdraw.draw.figure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.font.TextLayout;
import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class TextFigureTest {
    private TextFigure textFigure;

    @Before
    public void setUp() throws Exception {
        textFigure = new TextFigure();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testDuplicationFactory() {
        TextFigure original = new TextFigure();
        original.setText("Tell me. For whom do you fight?");

        TextLayout layout = original.getTextLayout();

        TextFigure duplicate = TextFigure.createFrom(original);

        assertEquals("Text should be the same","Tell me. For whom do you fight?", duplicate.getText());
        assertSame("Layout should be the same", duplicate.getTextLayout(), layout);
    }


    @Test
    public void testTypesafeRestoreTransformTo() {

        Point2D.Double point = new Point2D.Double(3760, 3760);

        textFigure.restoreTransformTo(point);

        assertEquals(3760, textFigure.origin.x, 0.00001);
        assertEquals(3760, textFigure.origin.y, 0.00001);

    }




}