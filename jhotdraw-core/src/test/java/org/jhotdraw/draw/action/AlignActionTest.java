package org.jhotdraw.draw.action;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.action.AlignAction.AlignmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlignActionTest {

    @Mock
    private DrawingEditor editor;

    @Mock
    private DrawingView view;

    @Mock
    private Figure figure;

    @Mock
    private Drawing drawing;

    private AlignAction alignAction;

    private Set<Figure> figuresSet;

    @BeforeEach
    public void setUp() {
        figuresSet = new HashSet<>(Arrays.asList(figure, figure));
        alignAction = new AlignAction(editor, AlignmentType.NORTH); // or any other alignment type
        lenient().when(editor.getActiveView()).thenReturn(view);
        lenient().when(view.getDrawing()).thenReturn(drawing);
        lenient().when(view.getSelectedFigures()).thenReturn(figuresSet); // For multiple figures
        lenient().when(figure.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 100, 100));
        lenient().when(view.getDrawing()).thenReturn(drawing);
        lenient().when(figure.isTransformable()).thenReturn(true);
    }

    @Test
    public void testConstructor() {
        AlignAction action = new AlignAction(editor, AlignmentType.NORTH);
        assertNotNull(action);
        // Additional assertions for constructor behavior
    }

    @Test
    public void testActionPerformed() {
        AlignAction action = new AlignAction(editor, AlignmentType.NORTH);
        action.actionPerformed(null);
        // Additional assertions to verify behavior
    }

    @Test
    public void testCalculateAlignmentDelta() {
        AlignAction action = new AlignAction(editor, AlignmentType.NORTH);
        Rectangle2D.Double figureBounds = new Rectangle2D.Double(10, 10, 100, 100);
        Rectangle2D.Double selectionBounds = new Rectangle2D.Double(0, 0, 200, 200);
        Point2D.Double delta = action.calculateAlignmentDelta(figureBounds, selectionBounds);
        assertEquals(0, delta.x);
        assertEquals(-10, delta.y);
        // Additional assertions for other alignment types
    }

    @Test
    public void testTransformFigure() {
        AffineTransform transform = new AffineTransform();
        alignAction.transformFigure(figure, transform);
        verify(figure, times(1)).willChange();
        verify(figure, times(1)).transform(transform);
        verify(figure, times(1)).changed();
        // Additional assertions to verify correct transformation
    }

    @Test
    public void testGetSelectionBounds() {
        when(view.getSelectedFigures()).thenReturn(figuresSet); // For a single figure
        Rectangle2D.Double bounds = alignAction.getSelectionBounds();
        assertNotNull(bounds);
        // Assertions to verify correct bounds
    }

    @Test
    public void testUpdateEnabledState() {
        when(view.isEnabled()).thenReturn(true); // Ensure view is enabled
        when(view.getSelectionCount()).thenReturn(2); // Ensure selection count is more than 1

        alignAction.updateEnabledState();

        verify(view, times(1)).isEnabled();
        verify(view, times(1)).getSelectionCount();
        // Additional assertions to verify state update
    }
}


