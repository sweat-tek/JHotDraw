/*
 * @(#)AbstractUndoRedoAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.action.edit;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jhotdraw.action.AbstractViewAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Common behaviour for the Undo and Redo menu actions.
 * <p>
 * These actions do not perform the undo/redo themselves. They look up the
 * "real" action that the active {@link View} registered in its
 * {@code ActionMap} under a given ID, and delegate to it. This class captures
 * the shared delegation, listener wiring and enabled-state synchronisation;
 * concrete subclasses only supply the action ID
 * (see {@link UndoAction} and {@link RedoAction}).
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractUndoRedoAction extends AbstractViewAction {

    private static final long serialVersionUID = 1L;

    /** The ActionMap key of the real action this menu action delegates to. */
    private final String id;
    private final ResourceBundleUtil labels =
            ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");
    /** Keeps this action's name/enabled state in sync with the real action. */
    private final PropertyChangeListener actionPropertyListener =
            this::realActionPropertyChanged;

    /**
     * Creates a new instance that delegates to the action registered under
     * {@code id} in the active view's {@code ActionMap}.
     */
    protected AbstractUndoRedoAction(Application app, View view, String id) {
        super(app, view);
        this.id = id;
        labels.configureAction(this, id);
    }

    private void realActionPropertyChanged(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if ((name == null && AbstractAction.NAME == null)
                || (name != null && name.equals(AbstractAction.NAME))) {
            putValue(AbstractAction.NAME, evt.getNewValue());
        } else if ("enabled".equals(name)) {
            updateEnabledState();
        }
    }

    protected void updateEnabledState() {
        boolean isEnabled = false;
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            isEnabled = realAction.isEnabled();
        }
        setEnabled(isEnabled);
    }

    @Override
    protected void updateView(View oldValue, View newValue) {
        super.updateView(oldValue, newValue);
        if (newValue != null
                && newValue.getActionMap().get(id) != null
                && newValue.getActionMap().get(id) != this) {
            putValue(AbstractAction.NAME,
                    newValue.getActionMap().get(id).getValue(AbstractAction.NAME));
            updateEnabledState();
        }
    }

    @Override
    protected void installViewListeners(View p) {
        super.installViewListeners(p);
        Action actionInView = p.getActionMap().get(id);
        if (actionInView != null && actionInView != this) {
            actionInView.addPropertyChangeListener(actionPropertyListener);
        }
    }

    @Override
    protected void uninstallViewListeners(View p) {
        super.uninstallViewListeners(p);
        Action actionInView = p.getActionMap().get(id);
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

    /** Returns the real action registered in the active view, or null. */
    protected Action getRealAction() {
        return (getActiveView() == null) ? null : getActiveView().getActionMap().get(id);
    }
}
