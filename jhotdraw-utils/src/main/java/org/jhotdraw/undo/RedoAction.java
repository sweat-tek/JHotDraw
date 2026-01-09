package org.jhotdraw.undo;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import static org.jhotdraw.undo.UndoRedoManager.getLabels;
/**
 * Redo Action for use in a menu bar.
 */

public class RedoAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private final UndoRedoManager undoManager;

    private transient Logger logger = Logger.getLogger(RedoAction.class.getName());

    public RedoAction(UndoRedoManager undoManager) {
        this.undoManager = undoManager;
        getLabels().configureAction(this, "edit.redo");
        setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            undoManager.redo();
        } catch (CannotRedoException e) {
            logger.warning("Can't redo: " + e);
        }
    }
}
