package org.jhotdraw.draw.action;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.util.Set;

import static org.mockito.Mockito.*;

public class SelectionColorChooserHandlerTest {

    SelectionColorChooserHandler handler;
    DrawingEditor editor;
    AttributeKey<Color> key;
    JColorChooser selectionChooser;
    DrawingView drawingView;

    // TODO Report: Test setup very cumbersome
    @Before
    public void setUp() {
        editor = mock(DrawingEditor.class);
        key = AttributeKeys.FILL_COLOR;
        selectionChooser = mock(JColorChooser.class);
        ColorSelectionModel selectionModel = mock(ColorSelectionModel.class);
        when(selectionChooser.getSelectionModel()).thenReturn(selectionModel);
        JPopupMenu menu = mock(JPopupMenu.class);
        handler = new SelectionColorChooserHandler(
                editor,
                key,
                selectionChooser,
                menu
        );
        drawingView = mock(DrawingView.class);
        when(drawingView.getDrawing()).thenReturn(mock(Drawing.class));
        when(editor.getActiveView()).thenReturn(drawingView);
    }

    @After
    public void tearDown() {
    }


    // Mock that colorChooser returns red
    // Check that figure.set is called with "fillColor" and red
    // Mock response of figure.set to return red

    @Test
    public void testApplyChosenColorToSelectedFigure() {
        // given
        Figure figure = mock(Figure.class);
        when(drawingView.getSelectedFigures()).thenReturn(Set.of(figure));
        when(selectionChooser.getColor()).thenReturn(Color.red);

        // when
        handler.applySelectedColorToFigures();

        // then
        verify(selectionChooser, times(1)).getColor();
        verify(figure, times(1)).willChange();
        verify(figure, times(1)).set(AttributeKeys.FILL_COLOR, Color.red);
        verify(figure, times(1)).changed();
    }

}