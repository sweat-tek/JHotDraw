package org.jhotdraw.undo.actions;

import org.jhotdraw.undo.UndoRedoManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;


public class UndoActionTest {

    @Mock
    private UndoRedoManager undoRedoManager;

    private UndoAction undoAction;

    @Before
    public void setUp() {
        undoRedoManager = mock(UndoRedoManager.class);
        undoAction = new UndoAction(undoRedoManager);
    }

    @Test
    public void actionPerformed_undoSuccessfully() {
        // Arrange
        ActionEvent mockEvent = mock(ActionEvent.class);

        // Act
        undoAction.actionPerformed(mockEvent);

        // Assert
        verify(undoRedoManager, times(1)).undo();
    }

    @Test
    public void actionPerformed_cannotRedoExceptionHandled() {
        // Arrange
        ActionEvent mockEvent = mock(ActionEvent.class);
        doThrow(new CannotRedoException()).when(undoRedoManager).undo();

        // Act
        undoAction.actionPerformed(mockEvent);

    }

    @Test
    public void constructor_setsEnabledToFalse() {
        // Assert
        assertFalse(undoAction.isEnabled());
    }
}