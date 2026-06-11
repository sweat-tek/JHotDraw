/*
 * JUnit 4 tests for GroupAction and UngroupAction domain logic.
 * Swing dependencies (DrawingView, Drawing) are stubbed with Mockito
 * so each test exercises a single code path through a single method.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

public class GroupActionTest {

    // --- shared stubs ---
    private DrawingView view;
    private Drawing drawing;
    private GroupAction groupAction;
    private UngroupAction ungroupAction;

    @Before
    public void setUp() {
        view    = mock(DrawingView.class);
        drawing = mock(Drawing.class);
        when(view.getDrawing()).thenReturn(drawing);

        // Construct actions without a real DrawingEditor by passing null;
        // we only test the pure domain methods (groupFigures / ungroupFigures /
        // canGroup / canUngroup) which do not touch the editor.
        groupAction   = new GroupAction(null, new GroupFigure());
        ungroupAction = new UngroupAction(null, new GroupFigure());
    }

    // =========================================================
    // GroupAction – canGroup
    // =========================================================

    @Test
    public void canGroup_returnsFalse_whenViewIsNull() {
        // view == null → cannot group
        assertFalse(groupAction.canGroup(null));
    }

    @Test
    public void canGroup_returnsFalse_whenSelectionCountIsOne() {
        when(view.getSelectionCount()).thenReturn(1);
        assertFalse(groupAction.canGroup(view));
    }

    @Test
    public void canGroup_returnsTrue_whenSelectionCountIsTwo() {
        when(view.getSelectionCount()).thenReturn(2);
        assertTrue(groupAction.canGroup(view));
    }

    @Test
    public void canGroup_returnsTrue_whenSelectionCountIsMany() {
        // boundary: more than two figures selected
        when(view.getSelectionCount()).thenReturn(10);
        assertTrue(groupAction.canGroup(view));
    }

    // =========================================================
    // GroupAction – groupFigures
    // =========================================================

    @Test
    public void groupFigures_addsGroupToDrawingAtCorrectIndex() {
        Figure f1 = mock(Figure.class);
        Figure f2 = mock(Figure.class);
        List<Figure> figures = Arrays.asList(f1, f2);
        CompositeFigure group = mock(CompositeFigure.class);

        when(drawing.sort(figures)).thenReturn(figures);
        when(drawing.indexOf(f1)).thenReturn(3);

        groupAction.groupFigures(view, group, figures);

        verify(drawing).basicRemoveAll(figures);
        verify(drawing).add(3, group);
    }

    @Test
    public void groupFigures_addsEachFigureToGroup() {
        Figure f1 = mock(Figure.class);
        Figure f2 = mock(Figure.class);
        List<Figure> figures = Arrays.asList(f1, f2);
        CompositeFigure group = mock(CompositeFigure.class);

        when(drawing.sort(figures)).thenReturn(figures);
        when(drawing.indexOf(f1)).thenReturn(0);

        groupAction.groupFigures(view, group, figures);

        verify(group).basicAdd(f1);
        verify(group).basicAdd(f2);
    }

    @Test
    public void groupFigures_selectsGroupAfterGrouping() {
        Figure f1 = mock(Figure.class);
        List<Figure> figures = Collections.singletonList(f1);
        CompositeFigure group = mock(CompositeFigure.class);

        when(drawing.sort(figures)).thenReturn(figures);
        when(drawing.indexOf(f1)).thenReturn(0);

        groupAction.groupFigures(view, group, figures);

        verify(view).addToSelection(group);
    }

    @Test
    public void groupFigures_callsWillChangeAndChangedOnGroup() {
        Figure f1 = mock(Figure.class);
        List<Figure> figures = Collections.singletonList(f1);
        CompositeFigure group = mock(CompositeFigure.class);

        when(drawing.sort(figures)).thenReturn(figures);
        when(drawing.indexOf(f1)).thenReturn(0);

        groupAction.groupFigures(view, group, figures);

        verify(group).willChange();
        verify(group).changed();
    }

    // =========================================================
    // UngroupAction – canUngroup
    // =========================================================

    @Test
    public void canUngroup_returnsFalse_whenViewIsNull() {
        assertFalse(ungroupAction.canUngroup(null));
    }

    @Test
    public void canUngroup_returnsFalse_whenSelectionCountIsNotOne() {
        when(view.getSelectionCount()).thenReturn(2);
        assertFalse(ungroupAction.canUngroup(view));
    }

    @Test
    public void canUngroup_returnsFalse_whenSelectionCountIsZero() {
        // boundary: empty selection
        when(view.getSelectionCount()).thenReturn(0);
        assertFalse(ungroupAction.canUngroup(view));
    }

    @Test
    public void canUngroup_returnsFalse_whenSelectedFigureIsWrongType() {
        Figure plainFigure = mock(Figure.class);
        Set<Figure> sel = new HashSet<>(Collections.singletonList(plainFigure));
        when(view.getSelectionCount()).thenReturn(1);
        when(view.getSelectedFigures()).thenReturn(sel);
        assertFalse(ungroupAction.canUngroup(view));
    }

    @Test
    public void canUngroup_returnsTrue_whenExactlyOneGroupFigureSelected() {
        GroupFigure gf = new GroupFigure();
        Set<Figure> sel = new HashSet<>(Collections.<Figure>singletonList(gf));
        when(view.getSelectionCount()).thenReturn(1);
        when(view.getSelectedFigures()).thenReturn(sel);
        assertTrue(ungroupAction.canUngroup(view));
    }

    // =========================================================
    // UngroupAction – ungroupFigures
    // =========================================================

    @Test
    public void ungroupFigures_returnsChildrenOfGroup() {
        Figure child1 = mock(Figure.class);
        Figure child2 = mock(Figure.class);
        CompositeFigure group = mock(CompositeFigure.class);
        when(group.getChildren()).thenReturn(Arrays.asList(child1, child2));
        when(drawing.indexOf(group)).thenReturn(0);

        Collection<Figure> result = ungroupAction.ungroupFigures(view, group);

        assertTrue(result.contains(child1));
        assertTrue(result.contains(child2));
    }

    @Test
    public void ungroupFigures_removesGroupFromDrawing() {
        CompositeFigure group = mock(CompositeFigure.class);
        when(group.getChildren()).thenReturn(Collections.emptyList());
        when(drawing.indexOf(group)).thenReturn(2);

        ungroupAction.ungroupFigures(view, group);

        verify(drawing).remove(group);
    }

    @Test
    public void ungroupFigures_addsChildrenBackToDrawingAtGroupIndex() {
        Figure child = mock(Figure.class);
        CompositeFigure group = mock(CompositeFigure.class);
        List<Figure> children = Collections.singletonList(child);
        when(group.getChildren()).thenReturn(children);
        when(drawing.indexOf(group)).thenReturn(5);

        ungroupAction.ungroupFigures(view, group);

        verify(drawing).basicAddAll(5, children);
    }

    @Test
    public void ungroupFigures_clearsSelectionThenSelectsChildren() {
        Figure child = mock(Figure.class);
        CompositeFigure group = mock(CompositeFigure.class);
        when(group.getChildren()).thenReturn(Collections.singletonList(child));
        when(drawing.indexOf(group)).thenReturn(0);

        ungroupAction.ungroupFigures(view, group);

        // clearSelection must happen before addToSelection
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).clearSelection();
        inOrder.verify(view).addToSelection(anyCollection());
    }

    @Test
    public void ungroupFigures_returnsEmptyCollection_whenGroupHasNoChildren() {
        // boundary: empty group
        CompositeFigure group = mock(CompositeFigure.class);
        when(group.getChildren()).thenReturn(Collections.emptyList());
        when(drawing.indexOf(group)).thenReturn(0);

        Collection<Figure> result = ungroupAction.ungroupFigures(view, group);

        assertTrue(result.isEmpty());
    }

    // =========================================================
    // Java assertion (invariant) smoke test
    // =========================================================

    @Test
    public void groupFigures_invariant_groupIsInDrawingAfterGrouping() {
        Figure f1 = mock(Figure.class);
        List<Figure> figures = Collections.singletonList(f1);
        CompositeFigure group = mock(CompositeFigure.class);

        when(drawing.sort(figures)).thenReturn(figures);
        when(drawing.indexOf(f1)).thenReturn(0);
        // After add(0, group) the drawing "contains" group
        when(drawing.contains(group)).thenReturn(true);

        groupAction.groupFigures(view, group, figures);

        // Invariant: the group must exist in the drawing after grouping
        assert drawing.contains(group) : "Invariant violated: group must be in drawing after groupFigures";
    }
}
