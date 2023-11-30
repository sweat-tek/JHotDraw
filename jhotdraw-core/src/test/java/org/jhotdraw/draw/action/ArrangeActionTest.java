package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class ArrangeActionTest {
    private DrawingEditor editor;

    private DrawingView view;

    private Drawing drawing;

    private Figure figure1;

    private Figure figure2;

    private Figure figure3;

    private Set<Figure> selectedFigures = new HashSet<>();


    @org.junit.Before
    public void setUp() throws Exception {
        editor = mock(DrawingEditor.class);
        view = mock(DrawingView.class);
        drawing = mock(Drawing.class);
        figure1 = mock(Figure.class);
        figure2 = mock(Figure.class);
        figure3 = mock(Figure.class);

        when(view.getDrawing()).thenReturn(drawing);

        drawing.add(figure1);
        drawing.add(figure2);
        drawing.add(figure3);

        selectedFigures.add(figure1);
        selectedFigures.add(figure2);

        when(view.getSelectedFigures()).thenReturn(selectedFigures);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void actionPerformed() {
    }

    @org.junit.Test
    public void arrangeFigures_sendToBack() {
    }

    @org.junit.Test
    public void arrangeFigures_bringToFront() {
        //ArrangeAction.arrangeFigures(view, selectedFigures, ArrangeAction.BRING_TO_FRONT);

        verify(view, atLeastOnce()).getDrawing(); //virker
        // tests that the 'bringToFront' method is called on each figure from the drawing
        verify(drawing, atLeastOnce()).bringToFront(figure1); //virker ik
        for (Figure f:selectedFigures) {
            System.out.println(f);
        }

    }

    @org.junit.Test
    public void getResourceBundle() {
    }

}