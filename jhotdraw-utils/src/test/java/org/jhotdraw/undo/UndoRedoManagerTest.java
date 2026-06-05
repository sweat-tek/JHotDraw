/*
 * @(#)UndoRedoManagerTest.java
 *
 * Unit tests for the core domain logic of the Undo/Redo feature.
 */
package org.jhotdraw.undo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 unit tests for {@link UndoRedoManager} — the edit-stack engine that is
 * the core domain logic of the Undo/Redo feature.
 * <p>
 * Tests cover the best-case flow (add → undo → redo), the boundary cases
 * (empty stack, nothing to redo, re-entrant edits during undo), and use a
 * Mockito stub to isolate the manager from a concrete {@link UndoableEdit}.
 */
public class UndoRedoManagerTest {

    private UndoRedoManager manager;

    /**
     * A minimal test stub: a significant edit that records how many times it was
     * undone/redone. Used instead of mocking when we want real stack behaviour.
     */
    private static final class CountingEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
        int undoCount = 0;
        int redoCount = 0;

        @Override
        public void undo() {
            super.undo();
            undoCount++;
        }

        @Override
        public void redo() {
            super.redo();
            redoCount++;
        }
    }

    @Before
    public void setUp() {
        manager = new UndoRedoManager();
    }

    // ---------------------------------------------------------------- best case

    @Test
    public void newManagerHasNothingToUndoOrRedo() {
        assertFalse("a fresh manager cannot undo", manager.canUndo());
        assertFalse("a fresh manager cannot redo", manager.canRedo());
        assertFalse("a fresh manager has no significant edits", manager.hasSignificantEdits());
    }

    @Test
    public void addingSignificantEditEnablesUndo() {
        manager.addEdit(new CountingEdit());

        assertTrue("after adding an edit, undo is possible", manager.canUndo());
        assertTrue("a significant edit was recorded", manager.hasSignificantEdits());
        assertFalse("nothing has been undone yet, so redo is not possible", manager.canRedo());
    }

    @Test
    public void undoReversesLastEdit() {
        CountingEdit edit = new CountingEdit();
        manager.addEdit(edit);

        manager.undo();

        assertEquals("undo() is delegated to the edit exactly once", 1, edit.undoCount);
        assertFalse("nothing left to undo", manager.canUndo());
        assertTrue("the undone edit can now be redone", manager.canRedo());
    }

    @Test
    public void redoReappliesUndoneEdit() {
        CountingEdit edit = new CountingEdit();
        manager.addEdit(edit);
        manager.undo();

        manager.redo();

        assertEquals("redo() is delegated to the edit exactly once", 1, edit.redoCount);
        assertTrue("the redone edit can be undone again", manager.canUndo());
        assertFalse("nothing left to redo", manager.canRedo());
    }

    @Test
    public void discardAllEditsClearsHistory() {
        manager.addEdit(new CountingEdit());

        manager.discardAllEdits();

        assertFalse("history is empty after discardAllEdits", manager.canUndo());
        assertFalse("significant-edits flag is reset", manager.hasSignificantEdits());
    }

    // ----------------------------------------------------------- boundary cases

    @Test(expected = CannotUndoException.class)
    public void undoOnEmptyStackThrows() {
        manager.undo(); // nothing was added -> must throw
    }

    @Test(expected = CannotRedoException.class)
    public void redoWithNothingToRedoThrows() {
        manager.redo(); // nothing was undone -> must throw
    }

    @Test
    public void editsArrivingWhileUndoInProgressAreIgnored() {
        // Boundary: the manager must ignore edits fired *during* an undo
        // (undoOrRedoInProgress guard), otherwise the stack would corrupt.
        final CountingEdit latecomer = new CountingEdit();
        UndoableEdit reentrantEdit = new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public void undo() {
                super.undo();
                manager.addEdit(latecomer); // fired in the middle of an undo
            }
        };
        manager.addEdit(reentrantEdit);

        manager.undo();

        // If the latecomer had been accepted, canUndo() would be true.
        assertFalse("re-entrant edit must be ignored, leaving nothing to undo", manager.canUndo());
        assertEquals("the ignored edit is never undone", 0, latecomer.undoCount);
    }

    // -------------------------------------------------- mockito (isolation) test

    @Test
    public void undoDelegatesReversalToTheEdit() {
        // Stub a UndoableEdit so the manager is tested in isolation from any
        // concrete edit implementation (mockito.org). We only program the
        // collaborator's contract, then verify the manager calls undo() on it.
        UndoableEdit edit = mock(UndoableEdit.class);
        when(edit.isSignificant()).thenReturn(true);
        when(edit.canUndo()).thenReturn(true);

        manager.addEdit(edit);
        manager.undo();

        verify(edit).undo(); // the manager delegates the actual reversal to the edit
    }

    // ------------------------------------------------- java assertion (invariant)

    @Test
    public void undoRedoCountsStayBalanced() {
        CountingEdit edit = new CountingEdit();
        manager.addEdit(edit);
        manager.undo();
        manager.redo();

        // JAVA assertion: an invariant that must NEVER be violated. Surefire runs
        // tests with assertions enabled (-ea), so this is enforced; if the
        // manager ever redid more than it undid, the program would halt here.
        assert edit.redoCount <= edit.undoCount + 1
                : "invariant violated: redo count outran undo count";

        assertEquals(1, edit.undoCount);
        assertEquals(1, edit.redoCount);
    }
}
