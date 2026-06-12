package org.jhotdraw.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UndoRedoManagerTest {

    private UndoRedoManager manager;

    @Before
    public void setUp() {
        manager = new UndoRedoManager();
    }

    // -----------------------------------------------------------------------
    // Best case scenarios
    // -----------------------------------------------------------------------

    @Test
    public void testAddEdit_canUndoBecomesTrue() {
        manager.addEdit(new SimpleEdit());
        assertTrue(manager.canUndo());
    }

    @Test
    public void testUndo_canRedoBecomesTrue() throws CannotUndoException {
        manager.addEdit(new SimpleEdit());
        manager.undo();
        assertTrue(manager.canRedo());
    }

    @Test
    public void testUndo_canUndoBecomesFalse() throws CannotUndoException {
        manager.addEdit(new SimpleEdit());
        manager.undo();
        assertFalse(manager.canUndo());
    }

    @Test
    public void testRedo_afterUndo_canUndoAgain() throws CannotUndoException, CannotRedoException {
        manager.addEdit(new SimpleEdit());
        manager.undo();
        manager.redo();
        assertTrue(manager.canUndo());
    }

    @Test
    public void testHasSignificantEdits_afterSignificantEdit_isTrue() {
        manager.addEdit(new SimpleEdit());
        assertTrue(manager.hasSignificantEdits());
    }

    @Test
    public void testUndoAction_enablesAfterAddEdit() {
        assertFalse(manager.getUndoAction().isEnabled());
        manager.addEdit(new SimpleEdit());
        assertTrue(manager.getUndoAction().isEnabled());
    }

    @Test
    public void testRedoAction_enablesAfterUndoEdit() throws CannotUndoException {
        assertFalse(manager.getRedoAction().isEnabled());
        manager.addEdit(new SimpleEdit());
        manager.undo();
        assertTrue(manager.getRedoAction().isEnabled());
    }

    // -----------------------------------------------------------------------
    // Boundary cases
    // -----------------------------------------------------------------------

    @Test(expected = CannotUndoException.class)
    public void testUndo_emptyStack_throwsCannotUndoException() {
        manager.undo();
    }

    @Test(expected = CannotRedoException.class)
    public void testRedo_nothingUndone_throwsCannotRedoException() {
        manager.redo();
    }

    @Test
    public void testDiscardAllEdits_canUndo_becomesFalse() {
        manager.addEdit(new SimpleEdit());
        manager.discardAllEdits();
        assertFalse(manager.canUndo());
    }

    @Test
    public void testDiscardAllEdits_canRedo_becomesFalse() throws CannotUndoException {
        manager.addEdit(new SimpleEdit());
        manager.undo();
        manager.discardAllEdits();
        assertFalse(manager.canRedo());
    }

    @Test
    public void testDiscardAllEdits_hasSignificantEdits_becomesFalse() {
        manager.addEdit(new SimpleEdit());
        manager.discardAllEdits();
        assertFalse(manager.hasSignificantEdits());
    }

    @Test
    public void testHasSignificantEdits_nonSignificantEdit_staysFalse() {
        manager.addEdit(new NonSignificantEdit());
        assertFalse(manager.hasSignificantEdits());
    }

    @Test
    public void testAddDiscardAllEdits_disablesUndoAndRedo() {
        manager.addEdit(new SimpleEdit());
        manager.addEdit(UndoRedoManager.DISCARD_ALL_EDITS);
        assertFalse(manager.canUndo());
        assertFalse(manager.canRedo());
    }

    // -----------------------------------------------------------------------
    // Helper stubs
    // -----------------------------------------------------------------------

    private static class SimpleEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;

        @Override
        public String getPresentationName() {
            return "Simple Edit";
        }
    }

    private static class NonSignificantEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isSignificant() {
            return false;
        }
    }
}