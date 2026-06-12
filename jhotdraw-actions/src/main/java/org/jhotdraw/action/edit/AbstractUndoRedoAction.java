/*
 * @(#)AbstractUndoRedoAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.action.edit;

import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import org.jhotdraw.action.AbstractViewAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Abstract base for {@link UndoAction} and {@link RedoAction}.
 * <p>
 * Both actions share identical listener management, enabled-state tracking,
 * and delegation logic. The only variation is the action ID, supplied by
 * subclasses via {@link #getActionId()}.
 */
abstract class AbstractUndoRedoAction extends AbstractViewAction {

    private static final long serialVersionUID = 1L;
    protected final ResourceBundleUtil labels =
            ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");

    private final PropertyChangeListener actionPropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if ((name == null && AbstractAction.NAME == null)
                    || (name != null && name.equals(AbstractAction.NAME))) {
                putValue(AbstractAction.NAME, evt.getNewValue());
            } else if ("enabled".equals(name)) {
                updateEnabledState();
            }
        }
    };

    public AbstractUndoRedoAction(Application app, View view) {
        super(app, view);
    }

    /**
     * Returns the action map key that identifies this action within a view
     * (e.g. {@code "edit.undo"} or {@code "edit.redo"}).
     */
    protected abstract String getActionId();

    protected void updateEnabledState() {
        Action realAction = getRealAction();
        setEnabled(realAction != null && realAction != this && realAction.isEnabled());
    }

    @Override
    protected void updateView(View oldValue, View newValue) {
        super.updateView(oldValue, newValue);
        if (newValue != null
                && newValue.getActionMap().get(getActionId()) != null
                && newValue.getActionMap().get(getActionId()) != this) {
            putValue(AbstractAction.NAME,
                    newValue.getActionMap().get(getActionId()).getValue(AbstractAction.NAME));
            updateEnabledState();
        }
    }

    @Override
    protected void installViewListeners(View p) {
        super.installViewListeners(p);
        Action actionInView = p.getActionMap().get(getActionId());
        if (actionInView != null && actionInView != this) {
            actionInView.addPropertyChangeListener(actionPropertyListener);
        }
    }

    @Override
    protected void uninstallViewListeners(View p) {
        super.uninstallViewListeners(p);
        Action actionInView = p.getActionMap().get(getActionId());
        if (actionInView != null && actionInView != this) {
            actionInView.removePropertyChangeListener(actionPropertyListener);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            realAction.actionPerformed(e);
        }
    }

    private Action getRealAction() {
        return (getActiveView() == null) ? null : getActiveView().getActionMap().get(getActionId());
    }
}
