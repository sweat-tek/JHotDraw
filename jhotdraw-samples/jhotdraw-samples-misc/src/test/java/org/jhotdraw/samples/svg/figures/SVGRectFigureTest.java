package org.jhotdraw.samples.svg.figures;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.handle.AbstractHandle;
import org.jhotdraw.draw.handle.Handle;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;



public class SVGRectFigureTest {
    private SVGRectFigure svgRectFigure;

    @Before
    public void setup(){
        svgRectFigure = new SVGRectFigure(0, 0, 50, 25);
    }
    @Test
    public void testCreateHandlesZeroDetailLevel() {
        Collection<Handle> handles = svgRectFigure.createHandles(0);
        assertNotNull("not null", handles);
    }
    @Test
    public void testCreateHandlesOneDetailLevel() {
        Collection<Handle> handles = svgRectFigure.createHandles(1);
        assertNotNull("not null", handles);
    }
    @Test
    public void testMocking() {
        SVGRectFigure rectFigure = mock(SVGRectFigure.class);
        HandleCreator mockMouseHoverCreator = mock(MouseHoverHandleCreator.class);
        when(mockMouseHoverCreator.createHandles(rectFigure)).thenReturn(Collections.singletonList(mock(Handle.class)));

        HandleCreator mockOneDetailLevelCreator = mock(OneDetailLevelHandleCreator.class);
        when(mockOneDetailLevelCreator.createHandles(rectFigure)).thenReturn(Collections.singletonList(mock(Handle.class)));

        HandleCreator mockZeroDetailLevelCreator = mock(ZeroDetailLevelHandleCreator.class);
        when(mockZeroDetailLevelCreator.createHandles(rectFigure)).thenReturn(Collections.singletonList(mock(Handle.class)));

        Collection<Handle> handlesForMouseHoverDetailLevel = mockMouseHoverCreator.createHandles(rectFigure);
        Collection<Handle> handlesForZeroDetailLevel = mockZeroDetailLevelCreator.createHandles(rectFigure);
        Collection<Handle> handlesForOneDetailLevel = mockOneDetailLevelCreator.createHandles(rectFigure);

        assertEquals(1, handlesForMouseHoverDetailLevel.size());
        assertEquals(1, handlesForZeroDetailLevel.size());
        assertEquals(1, handlesForOneDetailLevel.size());
    }

}