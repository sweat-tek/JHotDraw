package org.jhotdraw.samples.svg.figures;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SVGRectFigureTest {
    SVGRectFigure svgRectFigure = new SVGRectFigure();
    Graphics2D mockGraphics2D = mock(Graphics2D.class);


    /**
     * Test the state of the figure after calling drawStroke().
     * Test for boundary value analysis, as the figure is a branch
     * if the arcwidth and archeight are 0 using assertEquals().
     */
    @Test
    public void drawStrokeCheckState() {
        int initialWidth = (int) svgRectFigure.roundrect.width;

        svgRectFigure.roundrect.arcwidth = 0;
        svgRectFigure.roundrect.archeight = 0;

        svgRectFigure.drawStroke(mockGraphics2D);

        int finalWidth = (int) svgRectFigure.roundrect.width;

        assertEquals(initialWidth, finalWidth);
    }

    /**
     * Test that drawStroke() calls Graphics2D.draw() if the figure is a branch.
     * Test for boundary value analysis, as the figure is a branch if the arcwidth and archeight are 0.
     */
    @Test
    public void drawStrokeIfBranchValue0() {
        svgRectFigure.roundrect.arcwidth = 0;
        svgRectFigure.roundrect.archeight = 0;

        svgRectFigure.drawStroke(mockGraphics2D);

        verify(mockGraphics2D, times(1)).draw(any());
    }

    /**
     * Test that drawStroke() calls Graphics2D.draw() if the figure is a branch.
     * Tests else branch of drawStroke(), where values are not 0.
     */
    @Test
    public void drawStrokeElseBranch() {
        svgRectFigure.roundrect.arcwidth = 1;
        svgRectFigure.roundrect.archeight = 1;

        svgRectFigure.drawStroke(mockGraphics2D);

        verify(mockGraphics2D, times(1)).draw(any());
    }
}