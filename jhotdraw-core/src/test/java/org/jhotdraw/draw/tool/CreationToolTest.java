package org.jhotdraw.draw.tool;

import org.jhotdraw.undo.UndoRedoManager;
import org.junit.*;
import javax.swing.undo.*;
import java.util.Locale;
import static org.junit.Assert.*;

public class CreationToolTest {

    private UndoRedoManager undoManager;

    @BeforeClass
    public static void setUpClass() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Before
    public void setUp() {
        undoManager = new UndoRedoManager();
    }


    @Test
    public void testEditAddedToHistory() {
        MockEdit edit = new MockEdit("Create Figure");
        undoManager.addEdit(edit);

        assertTrue("Should be able to undo after creation", undoManager.canUndo());
        assertEquals("Undo name should match", "Create Figure", edit.getPresentationName());
    }

    @Test
    public void testMultipleCreationsPreserveHistory() {
        MockEdit edit1 = new MockEdit("Create Rectangle");
        undoManager.addEdit(edit1);

        MockEdit edit2 = new MockEdit("Create Ellipse");
        undoManager.addEdit(edit2);

        assertTrue("Should be able to undo", undoManager.canUndo());

        undoManager.undo();
        assertTrue("Edit 2 should be undone", edit2.wasUndone());

        assertTrue("Should still be able to undo first edit", undoManager.canUndo());
        undoManager.undo();
        assertTrue("Edit 1 should be undone", edit1.wasUndone());
    }

    @Test
    public void testChronologicalUndoOrder() {
        MockEdit[] edits = new MockEdit[3];
        for (int i = 0; i < 3; i++) {
            edits[i] = new MockEdit("Edit " + (i + 1));
            undoManager.addEdit(edits[i]);
        }

        for (int i = 2; i >= 0; i--) {
            assertTrue("Should be able to undo edit " + (i + 1), undoManager.canUndo());
            undoManager.undo();
            assertTrue("Edit " + (i + 1) + " should be undone", edits[i].wasUndone());
        }

        assertFalse("Should not be able to undo anymore", undoManager.canUndo());
    }


    @Test
    public void testRedoAfterUndo() {
        MockEdit edit = new MockEdit("Test Edit");
        undoManager.addEdit(edit);

        undoManager.undo();
        assertTrue("Edit should be undone", edit.wasUndone());
        assertTrue("Should be able to redo", undoManager.canRedo());

        undoManager.redo();
        assertTrue("Edit should be redone", edit.wasRedone());
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