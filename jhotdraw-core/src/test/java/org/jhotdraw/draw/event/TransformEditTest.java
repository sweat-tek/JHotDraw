package org.jhotdraw.draw.event;

import org.jhotdraw.draw.figure.Figure;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransformEditTest {

    private Figure mockFigure;
    private AffineTransform translation;
    private TransformEdit edit;

    @Before
    public void setUp() {
        mockFigure = mock(Figure.class);
        // A pure translation is lossless and invertible — the correct use case for TransformEdit
        translation = AffineTransform.getTranslateInstance(10, 20);
        edit = new TransformEdit(mockFigure, translation);
    }

    // -----------------------------------------------------------------------
    // Best case scenarios
    // -----------------------------------------------------------------------

    @Test
    public void testUndo_callsTransformOnFigure() {
        edit.undo();
        verify(mockFigure).transform(any(AffineTransform.class));
    }

    @Test
    public void testRedo_afterUndo_callsTransformTwice() {
        edit.undo();
        edit.redo();
        verify(mockFigure, times(2)).transform(any(AffineTransform.class));
    }

    @Test
    public void testUndo_callsWillChangeAndChanged() {
        edit.undo();
        verify(mockFigure).willChange();
        verify(mockFigure).changed();
    }

    // -----------------------------------------------------------------------
    // Boundary cases
    // -----------------------------------------------------------------------

    @Test
    public void testAddEdit_sameFigureCollection_mergesTransforms() {
        // Both edits must share the same Collection reference to merge
        List<Figure> sharedFigures = new LinkedList<>();
        sharedFigures.add(mockFigure);

        AffineTransform tx1 = AffineTransform.getTranslateInstance(10, 0);
        AffineTransform tx2 = AffineTransform.getTranslateInstance(5, 0);

        TransformEdit edit1 = new TransformEdit(sharedFigures, tx1);
        TransformEdit edit2 = new TransformEdit(sharedFigures, tx2);

        boolean merged = edit1.addEdit(edit2);
        assertTrue(merged);
    }

    @Test
    public void testAddEdit_differentFigureCollection_doesNotMerge() {
        // Different collection references — merging must be refused
        List<Figure> figures1 = new LinkedList<>();
        figures1.add(mockFigure);
        List<Figure> figures2 = new LinkedList<>();
        figures2.add(mockFigure);

        TransformEdit edit1 = new TransformEdit(figures1, translation);
        TransformEdit edit2 = new TransformEdit(figures2, translation);

        boolean merged = edit1.addEdit(edit2);
        assertFalse(merged);
    }
}