package org.jhotdraw.undo.actions;

import org.jhotdraw.undo.UndoRedoManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;


public class RedoActionTest {

    @Mock
    private UndoRedoManager undoRedoManager;

    private RedoAction redoAction;

    @Before
    public void setUp() {
        undoRedoManager = mock(UndoRedoManager.class);
        redoAction = new RedoAction(undoRedoManager);
    }

    @Test
    public void actionPerformed_RedoSuccessfully() {
        // Arrange
        ActionEvent mockEvent = mock(ActionEvent.class);

        // Act
        redoAction.actionPerformed(mockEvent);

        // Assert
        verify(undoRedoManager, times(1)).redo();
    }

    @Test
    public void actionPerformed_cannotRedoExceptionHandled() {
        // Arrange
        ActionEvent mockEvent = mock(ActionEvent.class);
        doThrow(new CannotRedoException()).when(undoRedoManager).redo();

        // Act
        redoAction.actionPerformed(mockEvent);

    }

    @Test
    public void constructor_setsEnabledToFalse() {
        // Assert
        assertFalse(redoAction.isEnabled());
    }
}