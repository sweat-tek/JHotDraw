package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.mockito.Mockito;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GivenGroupingStage extends Stage<GivenGroupingStage> {

    @ScenarioState
    DrawingView view;

    @ScenarioState
    GroupAction groupAction;

    @ScenarioState
    UngroupAction ungroupAction;

    @ScenarioState
    List<Figure> selectedFigures;

    @ScenarioState
    GroupFigure selectedGroup;

    /** Returns a stub rectangle used wherever getDrawingArea() is needed. */
    private Rectangle2D.Double rect() {
        return new Rectangle2D.Double(0, 0, 10, 10);
    }

    /** Stubs both getDrawingArea() overloads on a mock figure. */
    private void stubDrawingArea(Figure mock) {
        Mockito.when(mock.getDrawingArea()).thenReturn(rect());
        Mockito.when(mock.getDrawingArea(Mockito.anyDouble())).thenReturn(rect());
    }

    public GivenGroupingStage two_figures_are_selected() {
        Drawing drawing = Mockito.mock(Drawing.class);
        view = Mockito.mock(DrawingView.class);
        Mockito.when(view.getDrawing()).thenReturn(drawing);

        Figure f1 = Mockito.mock(Figure.class);
        Figure f2 = Mockito.mock(Figure.class);
        stubDrawingArea(f1);
        stubDrawingArea(f2);
        selectedFigures = Arrays.asList(f1, f2);

        Mockito.when(drawing.sort(selectedFigures)).thenReturn(selectedFigures);
        Mockito.when(drawing.indexOf(f1)).thenReturn(0);

        Set<Figure> selection = new HashSet<>(selectedFigures);
        Mockito.when(view.getSelectionCount()).thenReturn(2);
        Mockito.when(view.getSelectedFigures()).thenReturn(selection);

        groupAction = new GroupAction(null, new GroupFigure());
        return self();
    }

    public GivenGroupingStage one_GroupFigure_is_selected() {
        Drawing drawing = Mockito.mock(Drawing.class);
        view = Mockito.mock(DrawingView.class);
        Mockito.when(view.getDrawing()).thenReturn(drawing);

        selectedGroup = new GroupFigure();

        Figure child1 = Mockito.mock(Figure.class);
        Figure child2 = Mockito.mock(Figure.class);
        stubDrawingArea(child1);
        stubDrawingArea(child2);

        selectedGroup.willChange();
        selectedGroup.basicAdd(child1);
        selectedGroup.basicAdd(child2);
        selectedGroup.changed();

        Mockito.when(drawing.indexOf(selectedGroup)).thenReturn(0);

        Set<Figure> selection = new HashSet<>(Collections.<Figure>singletonList(selectedGroup));
        Mockito.when(view.getSelectionCount()).thenReturn(1);
        Mockito.when(view.getSelectedFigures()).thenReturn(selection);

        ungroupAction = new UngroupAction(null, new GroupFigure());
        return self();
    }

    public GivenGroupingStage one_plain_figure_is_selected() {
        Drawing drawing = Mockito.mock(Drawing.class);
        view = Mockito.mock(DrawingView.class);
        Mockito.when(view.getDrawing()).thenReturn(drawing);

        Figure f = Mockito.mock(Figure.class);
        stubDrawingArea(f);
        selectedFigures = Collections.singletonList(f);

        Set<Figure> selection = new HashSet<>(selectedFigures);
        Mockito.when(view.getSelectionCount()).thenReturn(1);
        Mockito.when(view.getSelectedFigures()).thenReturn(selection);

        groupAction = new GroupAction(null, new GroupFigure());
        return self();
    }
}
