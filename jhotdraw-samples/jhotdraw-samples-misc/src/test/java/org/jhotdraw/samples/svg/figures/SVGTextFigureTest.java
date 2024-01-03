package org.jhotdraw.samples.svg.figures;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class SVGTextFigureTest {
    private SVGTextFigure figure;

    @Before
    public void create(){
        this.figure = new SVGTextFigure();
    }

    @Test
    public void testSetText(){
        figure.setText("Check this");
        assertEquals("Check this", figure.getText());
    }

    @Test
    public void testDefaultFontSize() {
        assertEquals(12, figure.getFontSize(), 0.01);
    }

    @Test
    public void testSetEditability(){
        figure.setEditable(false);
        assertEquals(false, figure.isEditable());
    }

    @Test
    public void testFont() {
        Font font = figure.getFont();
        assertEquals("VERDANA", font.getName());
    }
}
