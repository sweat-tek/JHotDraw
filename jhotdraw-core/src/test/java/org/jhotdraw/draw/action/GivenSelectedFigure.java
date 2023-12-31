package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.Figure;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.colorchooser.ColorSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.mock;

public class GivenSelectedFigure extends Stage<GivenSelectedFigure> {

    List<Figure> figures = new ArrayList<>();
    @ScenarioState
    List<Figure> selectedFigures = new ArrayList<>();

    public GivenSelectedFigure the_user_creates_a_figure() {
        Figure figure = mock(Figure.class);
        figures.add(figure);
        return this;
    }

    public GivenSelectedFigure the_user_selects_a_figure() {
        Figure figure = figures.get(0);
        selectedFigures.add(figure);
        Mockito.when(drawingView.getSelectedFigures()).thenReturn(new HashSet<>(selectedFigures));
        Mockito.when(drawingView.getSelectionCount()).thenReturn(1);
        return this;
    }


    // TEST SETUP
    /////////////

    @ScenarioState
    SelectionColorChooserHandler handler;

    @ScenarioState
    DrawingView drawingView;


    // TODO Report: Test setup very cumbersome
    @BeforeScenario
    public void setUp() {
        DrawingEditor editor = mock(DrawingEditor.class);
        AttributeKey<Color> key = AttributeKeys.FILL_COLOR;
        JColorChooser colorChooser = mock(JColorChooser.class);
        Mockito.when(colorChooser.getSelectionModel()).thenReturn(mock(ColorSelectionModel.class));
        JPopupMenu popupMenu = mock(JPopupMenu.class);
        handler = new SelectionColorChooserHandler(
                editor,
                key,
                colorChooser,
                popupMenu
        );
        drawingView = mock(DrawingView.class);
        Mockito.when(drawingView.getDrawing()).thenReturn(mock(Drawing.class));
        Mockito.when(editor.getActiveView()).thenReturn(drawingView);
    }

}
