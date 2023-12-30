package org.jhotdraw.action.edit;

import org.jhotdraw.action.AbstractViewAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

abstract class UndoRedoAction extends AbstractViewAction {
    private static final long serialVersionUID = 1L;
    private final String id;

    private PropertyChangeListener redoActionPropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if ((name == null && AbstractAction.NAME == null) || (name != null && name.equals(AbstractAction.NAME))) {
                putValue(AbstractAction.NAME, evt.getNewValue());
            } else if ("enabled".equals(name)) {
                updateEnabledState();
            }
        }
    };

    /**
     * Creates a new instance which acts on the specified view of the application.
     *
     * @param app
     * @param view
     */
    protected UndoRedoAction(Application app, View view, String id) {
        super(app, view);
        this.id = id;
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
                && newValue.getActionMap().get(getID(id)) != null
                && newValue.getActionMap().get(getID(id)) != this) {
            putValue(AbstractAction.NAME, newValue.getActionMap().get(getID(id)).
                    getValue(AbstractAction.NAME));
            updateEnabledState();
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @Override
    protected void installViewListeners(View p) {
        super.installViewListeners(p);
        Action actionInView = p.getActionMap().get(getID(id));
        if (actionInView != null && actionInView != this) {
            actionInView.addPropertyChangeListener(redoActionPropertyListener);
        }
    }

    /**
     * Installs listeners on the view object.
     */
    @Override
    protected void uninstallViewListeners(View p) {
        super.uninstallViewListeners(p);
        Action actionInView = p.getActionMap().get(getID(id));
        if (actionInView != null && actionInView != this) {
            actionInView.removePropertyChangeListener(redoActionPropertyListener);
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
        return (getActiveView() == null) ? null : getActiveView().getActionMap().get(getID(id));
    }

    protected static String getID(String id) {
        return id;
    }
}
