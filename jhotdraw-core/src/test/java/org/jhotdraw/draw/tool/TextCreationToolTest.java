package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockMakers;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TextCreationToolTest {
    private TextCreationTool tool;
    private DrawingEditor editor;
    private TextHolderFigure figure;
    private Drawing drawing;
    DrawingView view;

    @Before
    public void setUp() throws Exception {
        // Mockito cannot mock this under JDK 25 for whatever reason without this.
        figure = mock(TextFigure.class);
        tool = new TextCreationTool(figure);
        view = mock(DefaultDrawingView.class);
        editor = mock(DrawingEditor.class);
        drawing = mock(Drawing.class);
        when(editor.getActiveView()).thenReturn(view);
        when(view.getDrawing()).thenReturn(drawing);
        when(view.viewToDrawing(any(Point.class))).thenReturn(new Point2D.Double(10,10));
    }

    @Test
    public void testImplementsNecessaryContracts() {
        // If this is not true, something has gone terribly wrong.
        assertNotNull(tool);
        assertTrue(tool instanceof MouseListener);
        assertTrue(tool instanceof KeyListener);
        assertFalse(tool instanceof MouseMotionListener);
    }

    @Test
    public void testHandleMouseClicks() {
        tool.activate(editor);

        MouseEvent event = mock(MouseEvent.class);

        tool.mouseClicked(event);

        verify(editor, atLeastOnce()).getActiveView();
    }


    @Test
    public void testToolUsesCorrectPrototype() {
        TextFigure figure = mock(TextFigure.class);
        when(figure.getText()).thenReturn("Test Text");
        TextCreationTool tool = new TextCreationTool(figure);

        Figure prototype = tool.getPrototype();

        assertTrue(prototype instanceof TextHolderFigure);
        assertEquals("Test Text", ((TextHolderFigure) prototype).getText());
    }

    @Test
    public void testMouseClickedCreatesAndAddsFigure() {

        // One could argue whether this would have been better with a real instance
        // rather than mocking this much surface area, but it would also introduce involving Swing a lot more.
        // The focus is on testing the text creation tool, not on accounting for Swing.
        MouseEvent event = mock(MouseEvent.class);
        when(event.getPoint()).thenReturn(new Point(10,10));
        when(figure.clone()).thenReturn(mock(TextFigure.class));
        when(event.getClickCount()).thenReturn(1);
        when(event.getSource()).thenReturn(view);
        when(view.getComponent()).thenReturn((JComponent) view);
        when(view.viewToDrawing(any(Point.class))).thenReturn(new Point2D.Double(10,10));
        when(view.isEnabled()).thenReturn(true);
        when(editor.findView(any(Container.class))).thenReturn(view);

        TextCreationTool spyTool = spy(tool);
        // This integration test is about proving the figure is being created and added. Not whether it begins editing.
        doNothing().when(spyTool).beginEdit(any(TextHolderFigure.class));



        spyTool.activate(editor);

        spyTool.mouseClicked(event);

        verify(figure, atLeastOnce()).clone();
        verify(drawing).add(any(TextHolderFigure.class));

        spyTool.deactivate(editor);



    }


    @After
    public void tearDown() throws Exception {
    }
}