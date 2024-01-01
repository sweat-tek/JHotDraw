package org.jhotdraw.undo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import static org.junit.Assert.*;

public class UndoRedoManagerTest {

    @Mock
    private UndoableEdit undoableEdit;

    @InjectMocks
    private UndoRedoManager undoRedoManager;

    @Before
    public void setUp() {
        this.undoRedoManager = new UndoRedoManager();
        undoableEdit = new UndoableEdit() {
            private boolean canUndo = true;
            private boolean canRedo = false;
            @Override
            public void undo() throws CannotUndoException {
                canUndo = false;
                canRedo = true;
            }

            @Override
            public boolean canUndo() {
                return canUndo;
            }

            @Override
            public void redo() throws CannotRedoException {
                canRedo = false;
                canUndo = true;
            }

            @Override
            public boolean canRedo() {
                return canRedo;
            }

            @Override
            public void die() {

            }

            @Override
            public boolean addEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean replaceEdit(UndoableEdit anEdit) {
                return false;
            }

            @Override
            public boolean isSignificant() {
                return true;
            }

            @Override
            public String getPresentationName() {
                return null;
            }

            @Override
            public String getUndoPresentationName() {
                return null;
            }

            @Override
            public String getRedoPresentationName() {
                return null;
            }
        };
    }

    @Test
    public void canAddEdit() {
        // Act
        boolean success = undoRedoManager.addEdit(undoableEdit);
        undoRedoManager.setHasSignificantEdits(true);

        // Assert
        assertTrue(success);
    }

    @Test
    public void throwsEcxeptionIfCanNotUndoButUndoIsRun() {
        // Assert
        assertFalse(undoRedoManager.canUndo());
        assertThrows(CannotUndoException.class, () -> undoRedoManager.undo());
    }

    @Test
    public void canUndoIfHasUndoableEditAndSignificantEdits() throws CannotUndoException {
        // Arrange
        undoRedoManager.addEdit(undoableEdit);
        undoRedoManager.setHasSignificantEdits(true);

        // Assert
        assert undoRedoManager.canUndo(): true;
    }

    @Test
    public void canNotUndoIfAlreadyUndid() throws CannotUndoException {
        // Arrange
        undoRedoManager.addEdit(undoableEdit);

        // Act
        undoRedoManager.undo();

        // Assert
        assertFalse(undoRedoManager.canUndo());
    }

    @Test
    public void throwsExceptionIfCanNotRedoButRedoIsRun() {
        // Assert
        assertFalse(undoRedoManager.canRedo());
        assertThrows(CannotRedoException.class, () -> undoRedoManager.redo());
    }

    @Test
    public void canRedoAfterUndo() throws CannotRedoException {
        // Arrange
        undoRedoManager.addEdit(undoableEdit);
        undoRedoManager.setHasSignificantEdits(true);
        undoRedoManager.undo();

        // Assert
        assertTrue(undoRedoManager.canRedo());
    }

    @Test
    public void canUndoAfterRedo() throws CannotRedoException {
        // Arrange
        undoRedoManager.addEdit(undoableEdit);
        undoRedoManager.setHasSignificantEdits(true);
        undoRedoManager.undo();

        // Act
        undoRedoManager.redo();

        // Assert
        assertTrue(undoRedoManager.canUndo());
    }

    @Test
    public void CanNotUndoOrRedoAfterDiscardAllEdits() {
        // Arrange
        undoRedoManager.addEdit(undoableEdit);
        undoRedoManager.setHasSignificantEdits(true);

        // Act
        undoRedoManager.discardAllEdits();

        // Assert
        assertFalse(undoRedoManager.hasSignificantEdits());
        assertThrows(CannotRedoException.class, () -> undoRedoManager.redo());
        assertThrows(CannotUndoException.class, () -> undoRedoManager.undo());
    }
}