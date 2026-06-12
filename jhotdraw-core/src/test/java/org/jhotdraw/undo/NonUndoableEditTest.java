package org.jhotdraw.undo;

import org.junit.Test;
import static org.junit.Assert.*;

public class NonUndoableEditTest {

    @Test
    public void testCanUndo_alwaysReturnsFalse() {
        NonUndoableEdit edit = new NonUndoableEdit();
        assertFalse(edit.canUndo());
    }

    @Test
    public void testCanRedo_alwaysReturnsFalse() {
        NonUndoableEdit edit = new NonUndoableEdit();
        assertFalse(edit.canRedo());
    }
}