package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.FloatingTextArea;
import org.jhotdraw.draw.text.FloatingTextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TextEditingToolTest {
    private TextEditingTool textEditingTool;
    private DrawingEditor editor;
    private DrawingView view;
    private Drawing drawing;
    private TextHolderFigure textFigure;


    @Before
    public void setUp() throws Exception {
        view = mock(DefaultDrawingView.class);
        editor = mock(DrawingEditor.class);
        drawing = mock(Drawing.class);

        textFigure = new TextFigure("OG Text");

        textEditingTool = new TextEditingTool(textFigure);

        when(editor.getActiveView()).thenReturn(view);
        when(editor.findView(any(Container.class))).thenReturn(view);
        when(view.getDrawing()).thenReturn(drawing);
        when(view.getComponent()).thenReturn((JComponent) view);

    }

    @Test
    public void testMouseClickStartsEditing() {
        Point2D.Double drawingPoint = new Point2D.Double(10, 10);
        when(view.viewToDrawing(any(Point.class))).thenReturn(drawingPoint);
        when(drawing.findFigureInside(drawingPoint)).thenReturn(textFigure);
        when(view.getComponent()).thenReturn(new JPanel());


        MouseEvent event = mock(MouseEvent.class);
        when(event.getSource()).thenReturn(view);
        when(event.getPoint()).thenReturn(new Point(10,10));
        when(event.getClickCount()).thenReturn(1);
        when(view.drawingToView(any(Point2D.Double.class))).thenReturn(new Point(10,10));
        when(view.drawingToView(any(Rectangle2D.Double.class))).thenReturn(new Rectangle(10, 10, 100, 20));

        FloatingTextArea mockEditor = mock(FloatingTextArea.class);
        when(mockEditor.getText()).thenReturn("New Text");

        textEditingTool.activate(editor);
        textEditingTool.beginEdit(textFigure);

        assertNotNull("Internal textfield should not be null", textEditingTool.textField);

        FloatingTextField spyField =  spy(textEditingTool.textField);

        doReturn("New Text").when(spyField).getText();

        assertEquals("New Text", textFigure.getText());





    }

    @After
    public void tearDown() throws Exception {
    }
}