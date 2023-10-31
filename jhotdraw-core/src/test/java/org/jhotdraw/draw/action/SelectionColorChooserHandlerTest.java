package org.jhotdraw.draw.action;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SelectionColorChooserHandlerTest {

    DrawingEditor editor;
    SelectionColorChooserHandler handler;
    JColorChooser colorChooser;
    DrawingView drawingView;
    JPopupMenu popupMenu;

    // TODO Report: Test setup very cumbersome
    @Before
    public void setUp() {
        editor = mock(DrawingEditor.class);
        AttributeKey<Color> key = AttributeKeys.FILL_COLOR;
        colorChooser = mock(JColorChooser.class);
        when(colorChooser.getSelectionModel()).thenReturn(mock(ColorSelectionModel.class));
        popupMenu = mock(JPopupMenu.class);
        handler = new SelectionColorChooserHandler(
                editor,
                key,
                colorChooser,
                popupMenu
        );
        drawingView = mock(DrawingView.class);
        when(drawingView.getDrawing()).thenReturn(mock(Drawing.class));
        when(editor.getActiveView()).thenReturn(drawingView);
    }

    @Test
    public void testApplySelectedColorToFigures() {
        // given a selected figure and the color chooser set to the color red
        Figure figure = mock(Figure.class);
        when(drawingView.getSelectedFigures()).thenReturn(new HashSet<>(Collections.singletonList(figure)));
        when(drawingView.getSelectionCount()).thenReturn(1);
        when(colorChooser.getColor()).thenReturn(Color.red);

        // when the apply selected color to figures method is called
        handler.applySelectedColorToFigures();

        // then verify that the current color of the color chooser is fetched, and
        // that the fetched color is applied to the selected figure, and
        // that the default of the editor is set to the fetched color
        verify(colorChooser, times(1)).getColor();
        verify(figure, times(1)).willChange();
        verify(figure, times(1)).set(AttributeKeys.FILL_COLOR, Color.red);
        verify(figure, times(1)).changed();
        verify(editor, times(1)).setDefaultAttribute(AttributeKeys.FILL_COLOR, Color.red);
    }

    // TODO Report: This test shows that it might make sense to stop the method after fetching no color
    // SelectionColorChooserHandler:71
    @Test
    public void testApplySelectedColorToFiguresNoSelectedColor() {
        // given a selected figure and the color chooser set to no color
        Figure figure = mock(Figure.class);
        when(drawingView.getSelectedFigures()).thenReturn(new HashSet<>(Collections.singletonList(figure)));
        when(drawingView.getSelectionCount()).thenReturn(1);
        when(colorChooser.getColor()).thenReturn(null);

        // when the apply selected color to figures method is called
        handler.applySelectedColorToFigures();

        // then verify that no color of the color chooser is fetched, and
        // that no color is applied to the selected figure, and
        // that the default of the editor is set to no color
        verify(colorChooser, times(1)).getColor();
        // TODO The method could already stop here, i.e. after receiving no color from the chooser
        verify(figure, times(1)).willChange();
        verify(figure, times(1)).set(AttributeKeys.FILL_COLOR, null);
        verify(figure, times(1)).changed();
        verify(editor, times(1)).setDefaultAttribute(AttributeKeys.FILL_COLOR, null);
    }

    @Test
    public void testApplySelectedColorToFiguresNoFigureSelected() {
        // given a selected figure and the color chooser set to no color
        when(drawingView.getSelectedFigures()).thenReturn(Collections.emptySet());
        when(drawingView.getSelectionCount()).thenReturn(0);
        when(colorChooser.getColor()).thenReturn(Color.red);

        // when the apply selected color to figures method is called
        handler.applySelectedColorToFigures();

        // then verify that the current color of the color chooser is fetched, and
        // that no color is applied to the selected figure, and
        // that the default of the editor is set to the fetched color
        verify(colorChooser, times(1)).getColor();
        verify(editor, times(1)).setDefaultAttribute(AttributeKeys.FILL_COLOR, Color.red);
    }

    @Test
    public void testUpdateEnabledState() {
        // given a selected figure
        when(drawingView.getSelectedFigures()).thenReturn(new HashSet<>(Collections.singletonList(mock(Figure.class))));
        when(drawingView.getSelectionCount()).thenReturn(1);

        // when the update enabled state method is called
        handler.updateEnabledState();

        // then verify that color chooser and popup menu are updated to be enabled, and
        // that the color chooser is set to the color of the selected figure if any
        verify(colorChooser, times(1)).setEnabled(true);
        verify(popupMenu, times(1)).setEnabled(true);
        verify(colorChooser, times(1)).setColor(new Color(0, true));
        assertEquals(0, handler.isUpdating);
    }

    @Test
    public void testUpdateEnabledStateNoFigureSelected() {
        // given no figures selected
        when(drawingView.getSelectedFigures()).thenReturn(Collections.emptySet());
        when(drawingView.getSelectionCount()).thenReturn(0);

        // when the update enabled state method is called
        handler.updateEnabledState();

        // then verify that color chooser and popup menu are updated to be disabled, and
        // that the color chooser is not set to the color of the selected figure
        verify(colorChooser, times(1)).setEnabled(false);
        verify(popupMenu, times(1)).setEnabled(false);
        verify(colorChooser, times(0)).setColor(any());
        assertEquals(0, handler.isUpdating);
    }

    @Test
    public void testUpdateEnabledStateMultipleFiguresSelected() {
        // given a multiple selected figures, one colorless and one colorful
        Figure colorlessFigure = mock(Figure.class);
        Figure colorfulFigure = mock(Figure.class);
        when(colorfulFigure.get(AttributeKeys.FILL_COLOR)).thenReturn(Color.red);
        // TODO Weak point of current impl:
        // Based on the chosen set implementation it is somewhat random which figure is used for determining the color
        // There should be some clear rule in place, e.g. use the color of the lastly selected figure
        when(drawingView.getSelectedFigures())
                .thenReturn(new LinkedHashSet<>(Arrays.asList(colorlessFigure, colorfulFigure)));
        when(drawingView.getSelectionCount()).thenReturn(2);

        // when the update enabled state method is called
        handler.updateEnabledState();

        // then verify that color chooser and popup menu are updated to be enabled, and
        // that the color chooser is set to the color of first figure in the set
        verify(colorChooser, times(1)).setEnabled(true);
        verify(popupMenu, times(1)).setEnabled(true);
        verify(colorChooser, times(1)).setColor(new Color(0, true));
        // TODO Should be something like 'take value from last selected figure' after the refacoring
        // verify(colorChooser, times(1)).setColor(Color.red);
        assertEquals(0, handler.isUpdating);
    }

}