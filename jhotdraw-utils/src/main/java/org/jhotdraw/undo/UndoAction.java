package org.jhotdraw.undo;

import javax.swing.*;
import javax.swing.undo.CannotUndoException;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import static org.jhotdraw.undo.UndoRedoManager.getLabels;

/**
 * Undo Action for use in a menu bar.
 */
public class UndoAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private final UndoRedoManager undoRedoManager;

    private transient Logger logger = Logger.getLogger(UndoAction.class.getName());


    public UndoAction(UndoRedoManager undoRedoManager) {
        this.undoRedoManager = undoRedoManager;
        getLabels().configureAction(this, "edit.undo");
            setEnabled(false);
        }

        /**
         * Invoked when an action occurs.
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                undoRedoManager.undo();
            } catch (CannotUndoException e) {
                logger.warning("Can't undo: " + e);
            }
        }
    }

