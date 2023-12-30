package org.jhotdraw.undo.actions;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.undo.UndoRedoManager;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;

public class RedoAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private final UndoRedoManager undoRedoManager;

    public RedoAction(UndoRedoManager undoRedoManager) {
        this.undoRedoManager = undoRedoManager;
        UndoRedoManager.getLabels().configureAction(this, "edit.redo");
        setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    @Override
    @FeatureEntryPoint("Action - Redo action")
    public void actionPerformed(ActionEvent evt) {
        try {
            this.undoRedoManager.redo();
        } catch (CannotRedoException e) {
            System.out.println("Cannot redo: " + e);
        }
    }
}