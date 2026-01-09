package org.jhotdraw.draw.tool;

import org.jhotdraw.undo.UndoRedoManager;
import org.junit.*;
import javax.swing.undo.*;
import java.util.Locale;
import static org.junit.Assert.*;

public class ConnectionToolTest {

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
        MockEdit edit = new MockEdit("Create Connection");
        undoManager.addEdit(edit);

        assertTrue(undoManager.canUndo());
        assertEquals("Create Connection", edit.getPresentationName());
    }

    @Test
    public void testMultipleCreationsPreserveHistory() {
        MockEdit edit1 = new MockEdit("Create Connection 1");
        undoManager.addEdit(edit1);

        MockEdit edit2 = new MockEdit("Create Connection 2");
        undoManager.addEdit(edit2);

        assertTrue(undoManager.canUndo());

        undoManager.undo();
        assertTrue(edit2.wasUndone());

        assertTrue(undoManager.canUndo());
        undoManager.undo();
        assertTrue(edit1.wasUndone());
    }

    @Test
    public void testChronologicalUndoOrder() {
        MockEdit[] edits = new MockEdit[3];
        for (int i = 0; i < 3; i++) {
            edits[i] = new MockEdit("Connection " + (i + 1));
            undoManager.addEdit(edits[i]);
        }

        for (int i = 2; i >= 0; i--) {
            assertTrue(undoManager.canUndo());
            undoManager.undo();
            assertTrue(edits[i].wasUndone());
        }

        assertFalse(undoManager.canUndo());
    }

    @Test
    public void testRedoAfterUndo() {
        MockEdit edit = new MockEdit("Test Connection");
        undoManager.addEdit(edit);

        undoManager.undo();
        assertTrue(edit.wasUndone());
        assertTrue(undoManager.canRedo());

        undoManager.redo();
        assertTrue(edit.wasRedone());
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