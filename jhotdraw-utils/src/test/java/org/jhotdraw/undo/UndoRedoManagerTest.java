package org.jhotdraw.undo;

import org.junit.*;
import javax.swing.undo.*;
import static org.junit.Assert.*;

/**
 * Minimal tests for UndoRedoManager functionality
 * These tests verify the core undo/redo mechanism works correctly
 */
public class UndoRedoManagerTest {

    @Test
    public void testUndoManagerBasicFunctionality() {
        UndoManager manager = new UndoManager();

        MockEdit edit = new MockEdit("Test Edit");
        manager.addEdit(edit);

        assertTrue("Should be able to undo", manager.canUndo());
        assertFalse("Should not be able to redo", manager.canRedo());

        manager.undo();
        assertTrue("Edit should be undone", edit.wasUndone());
        assertFalse("Should not be able to undo anymore", manager.canUndo());
        assertTrue("Should be able to redo", manager.canRedo());
    }

    @Test
    public void testMultipleEditsChronologicalOrder() {
        UndoManager manager = new UndoManager();

        MockEdit edit1 = new MockEdit("Edit 1");
        MockEdit edit2 = new MockEdit("Edit 2");
        MockEdit edit3 = new MockEdit("Edit 3");

        manager.addEdit(edit1);
        manager.addEdit(edit2);
        manager.addEdit(edit3);


        manager.undo();
        assertTrue("Edit 3 should be undone", edit3.wasUndone());
        assertFalse("Edit 2 should not be undone", edit2.wasUndone());

        manager.undo();
        assertTrue("Edit 2 should be undone", edit2.wasUndone());
        assertFalse("Edit 1 should not be undone", edit1.wasUndone());

        manager.undo();
        assertTrue("Edit 1 should be undone", edit1.wasUndone());
    }

    @Test
    public void testRedoFunctionality() {
        UndoManager manager = new UndoManager();

        MockEdit edit = new MockEdit("Test Edit");
        manager.addEdit(edit);
        manager.undo();

        assertTrue("Should be able to redo", manager.canRedo());
        manager.redo();
        assertTrue("Edit should be redone", edit.wasRedone());
        assertFalse("Should not be able to redo anymore", manager.canRedo());
    }

    @Test
    public void testDiscardAllEdits() {
        UndoManager manager = new UndoManager();

        manager.addEdit(new MockEdit("Edit 1"));
        manager.addEdit(new MockEdit("Edit 2"));

        assertTrue("Should be able to undo", manager.canUndo());

        manager.discardAllEdits();

        assertFalse("Should not be able to undo after discard", manager.canUndo());
        assertFalse("Should not be able to redo after discard", manager.canRedo());
    }

    @Test(expected = CannotUndoException.class)
    public void testUndoWhenEmpty() {
        UndoManager manager = new UndoManager();
        manager.undo();
    }

    @Test(expected = CannotRedoException.class)
    public void testRedoWhenEmpty() {
        UndoManager manager = new UndoManager();
        manager.redo();
    }

    private static class MockEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
        private final String name;
        private boolean undone = false;
        private boolean redone = false;

        public MockEdit(String name) {
            this.name = name;
        }

        @Override
        public String getPresentationName() {
            return name;
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            undone = true;
            redone = false;
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            redone = true;
            undone = false;
        }

        public boolean wasUndone() {
            return undone;
        }

        public boolean wasRedone() {
            return redone;
        }
    }
}