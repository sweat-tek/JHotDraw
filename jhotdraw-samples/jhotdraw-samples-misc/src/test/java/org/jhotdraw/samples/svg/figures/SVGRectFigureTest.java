package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.handle.Handle;
import org.junit.Test;
import org.junit.runner.OrderWith;

import java.util.Collection;

import static org.junit.Assert.*;


public class SVGRectFigureTest {
    @Test
    public void testCreateHandlesBestCase() {
        SVGRectFigure rectFigure = new SVGRectFigure(0, 0, 50, 25);
        Collection<Handle> handles = rectFigure.createHandles(0);
        assertNotNull("not null", handles);
        assertEquals("Number of handles", 4, handles.size());
    }
    @Test
    public void testCreateHandlesMouseHover() {
        SVGRectFigure rectFigure = new SVGRectFigure(0, 0, 100, 50);
        Collection<Handle> handles = rectFigure.createHandles(-1);
        int expectedCountOfHandlesForMouseHover = 1;
        assertNotNull("not null", handles);
        assertEquals("Number of handles", expectedCountOfHandlesForMouseHover, handles.size());
    }
    @Test
    public void testCreateHandlesZeroDetailLevel() {
        SVGRectFigure rectFigure = new SVGRectFigure(0, 0, 100, 50);
        Collection<Handle> handles = rectFigure.createHandles(0);
        int expectedCountOfHandlesForZeroDetailLevel = 2;
        assertNotNull("not null", handles);
        assertEquals("Number of handles", expectedCountOfHandlesForZeroDetailLevel, handles.size());
    }
    @Test
    public void testCreateHandlesOneDetailLevel() {
        SVGRectFigure rectFigure = new SVGRectFigure(0, 0, 100, 50);
        Collection<Handle> handles = rectFigure.createHandles(1);
        int expectedCountOfHandlesForOneDetailLevel = 1;
        assertNotNull("not null", handles);
        assertEquals("Number of handles", expectedCountOfHandlesForOneDetailLevel, handles.size());
    }
}