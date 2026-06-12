package org.jhotdraw.draw.event;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AttributeChangeEditTest {

    private Figure mockFigure;
    private AttributeKey<String> key;
    private AttributeChangeEdit<String> edit;

    @Before
    public void setUp() {
        mockFigure = mock(Figure.class);
        key = new AttributeKey<>("testAttribute", String.class, null);
        edit = new AttributeChangeEdit<>(mockFigure, key, "oldValue", "newValue");
    }

    // -----------------------------------------------------------------------
    // Best case scenarios
    // -----------------------------------------------------------------------

    @Test
    public void testUndo_restoresOldAttributeValue() {
        edit.undo();
        verify(mockFigure).set(key, "oldValue");
    }

    @Test
    public void testRedo_appliesNewAttributeValue() {
        edit.undo();
        edit.redo();
        verify(mockFigure).set(key, "newValue");
    }

    // -----------------------------------------------------------------------
    // Boundary cases — lifecycle callbacks
    // -----------------------------------------------------------------------

    @Test
    public void testUndo_callsWillChangeBeforeSet() {
        edit.undo();
        // willChange() must be called before the figure is mutated
        verify(mockFigure).willChange();
        verify(mockFigure).set(key, "oldValue");
    }

    @Test
    public void testUndo_callsChangedAfterSet() {
        edit.undo();
        verify(mockFigure).changed();
    }

    @Test
    public void testRedo_callsWillChangeAndChangedTwiceTotal() {
        edit.undo();
        edit.redo();
        // Both undo and redo call willChange()/changed()
        verify(mockFigure, times(2)).willChange();
        verify(mockFigure, times(2)).changed();
    }
}