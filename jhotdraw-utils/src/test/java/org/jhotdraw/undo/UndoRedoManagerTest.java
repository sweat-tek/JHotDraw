package org.jhotdraw.undo;

import org.junit.*;
import javax.swing.undo.*;
import static org.junit.Assert.*;


public class UndoRedoManagerTest {

    private UndoRedoManager manager;

  
    private static class SimpleEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
       
    }

    @Before
    public void setUp() {
        manager = new UndoRedoManager();
    }

    @After
    public void tearDown() {
        manager.discardAllEdits();
    }

    
    // Test 1 — BEST CASE
    // Adding a significant edit should make undo available.
    
    @Test
    public void testAddEdit_EnablesUndo() {
        // Initially nothing to undo
        assertFalse("Nothing to undo on a fresh manager", manager.canUndo());

        manager.addEdit(new SimpleEdit());

        // After adding an edit, undo must be possible
        assertTrue("Undo should be available after adding an edit", manager.canUndo());
        // And the undo action button should be enabled
        assertTrue("UndoAction should be enabled", manager.getUndoAction().isEnabled());
        // Redo has nothing to replay yet
        assertFalse("Redo should NOT be available before any undo", manager.canRedo());
    }

    
    // Test 2 — BEST CASE
    // After undo, redo becomes available; after redo, undo is back.
    
    @Test
    public void testUndoThenRedo_TogglesAvailability() {
        manager.addEdit(new SimpleEdit());

        // --- undo ---
        manager.undo();
        assertFalse("Undo should NOT be available after undoing the only edit",
                manager.canUndo());
        assertTrue("Redo should be available after undo", manager.canRedo());
        assertTrue("RedoAction should be enabled", manager.getRedoAction().isEnabled());

        // --- redo ---
        manager.redo();
        assertTrue("Undo should be available again after redo", manager.canUndo());
        assertFalse("Redo should NOT be available after redo", manager.canRedo());
    }

    // Test 3 — BOUNDARY CASE
    // Calling undo on an empty manager must throw CannotUndoException.
     
    @Test(expected = CannotUndoException.class)
    public void testUndo_OnEmptyManager_ThrowsException() {
        // No edits added — undo must always throw here
        manager.undo();
    }

    // 
    // Test 4 — BOUNDARY CASE
    // discardAllEdits resets everything: no undo, no redo, and hasSignificantEdits goes back to false.
    
    @Test
    public void testDiscardAllEdits_ResetsState() {
        manager.addEdit(new SimpleEdit());
        assertTrue("Sanity check: should have significant edits", manager.hasSignificantEdits());

        manager.discardAllEdits();

        // All three invariants must hold after discard
        assertFalse("canUndo must be false after discard", manager.canUndo());
        assertFalse("canRedo must be false after discard", manager.canRedo());
        assertFalse("hasSignificantEdits must be false after discard",
                manager.hasSignificantEdits());
        assertFalse("UndoAction must be disabled after discard",
                manager.getUndoAction().isEnabled());
        assertFalse("RedoAction must be disabled after discard",
                manager.getRedoAction().isEnabled());
    }
}