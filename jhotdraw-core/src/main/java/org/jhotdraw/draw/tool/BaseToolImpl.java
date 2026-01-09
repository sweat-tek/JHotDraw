package org.jhotdraw.draw.tool;

import org.jhotdraw.beans.AbstractBean;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingEditorProxy;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.ToolEvent;
import org.jhotdraw.draw.event.ToolListener;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.handle.Handle;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH;

public class BaseToolImpl extends AbstractBean implements BaseTool {
    protected DrawingEditor editor;
    protected EventListenerList listenerList = new EventListenerList();
    protected DrawingEditorProxy editorProxy;
    /**
     * The input map of the tool.
     */
    protected InputMap inputMap;
    /**
     * The action map of the tool.
     */
    protected ActionMap actionMap;
    /**
     * This is set to true, if this is the active tool of the editor.
     */
    private boolean isActive;

    public BaseToolImpl() {
        editorProxy = new DrawingEditorProxy();
    }

    public void addUndoableEditListener(UndoableEditListener l) {
        listenerList.add(UndoableEditListener.class, l);
    }

    public void removeUndoableEditListener(UndoableEditListener l) {
        listenerList.remove(UndoableEditListener.class, l);
    }

    public void activate(DrawingEditor editor) {
        this.editor = editor;
        editorProxy.setTarget(editor);
        isActive = true;
        // Repaint all handles
        for (DrawingView v : editor.getDrawingViews()) {
            v.repaintHandles();
        }
    }

    public void deactivate(DrawingEditor editor) {
        this.editor = editor;
        editorProxy.setTarget(null);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    protected DrawingView getView() {
        return editor.getActiveView();
    }

    protected DrawingEditor getEditor() {
        return editor;
    }

    protected Drawing getDrawing() {
        return getView().getDrawing();
    }

    protected Point2D.Double viewToDrawing(Point p) {
        return constrainPoint(getView().viewToDrawing(p));
    }

    protected Point2D.Double constrainPoint(Point p, Figure... figure) {
        return constrainPoint(getView().viewToDrawing(p), figure);
    }

    protected Point2D.Double constrainPoint(Point2D.Double p, Figure... figure) {
        if (getView() == null) {
            return p;
        }
        return getView().getConstrainer() == null ? p : getView().getConstrainer().constrainPoint(p, figure);
    }

    /**
     * Sets the InputMap for the Tool.
     *
     * @see #keyPressed
     * @see #setActionMap
     */
    public void setInputMap(InputMap newValue) {
        inputMap = newValue;
    }

    /**
     * Gets the input map of the Tool
     */
    public InputMap getInputMap() {
        return inputMap;
    }

    /**
     * Sets the ActionMap for the Tool.
     *
     * @see #keyPressed
     */
    public void setActionMap(ActionMap newValue) {
        actionMap = newValue;
    }

    /**
     * Gets the action map of the Tool
     */
    public ActionMap getActionMap() {
        return actionMap;
    }

    /**
     * Deletes the selection. Depending on the tool, this could be selected figures, selected points
     * or selected text.
     */
    public void editDelete() {
        getView().getDrawing().removeAll(getView().getSelectedFigures());
    }

    /**
     * Cuts the selection into the clipboard. Depending on the tool, this could be selected figures,
     * selected points or selected text.
     */
    public void editCut() {
    }


    protected DrawingView prepareView(MouseEvent event) {
        DrawingView view = editor.findView((Container) event.getSource());
        if (view != null) {
            view.requestFocus();
            fireToolStarted(view);
        }
        return view;
    }

    /**
     * Copies the selection into the clipboard. Depending on the tool, this could be selected
     * figures, selected points or selected text.
     */
    public void editCopy() {
    }

    /**
     * Duplicates the selection. Depending on the tool, this could be selected figures, selected
     * points or selected text.
     */
    public void editDuplicate() {
    }

    /**
     * Pastes the contents of the clipboard. Depending on the tool, this could be selected figures,
     * selected points or selected text.
     */
    public void editPaste() {
    }

    /**
     * Override this method to create a tool-specific input map, which overrides the input map of
     * the drawing edtior.
     * <p>
     * The implementation of this class returns null.
     */
    protected InputMap createInputMap() {
        return null;
    }

    /**
     * Override this method to create a tool-specific action map, which overrides the action map of
     * the drawing edtior.
     * <p>
     * The implementation of this class returns null.
     */
    protected ActionMap createActionMap() {
        return null;
    }

    public void addToolListener(ToolListener l) {
        listenerList.add(ToolListener.class, l);
    }

    public void removeToolListener(ToolListener l) {
        listenerList.remove(ToolListener.class, l);
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireToolStarted(DrawingView view) {
        ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ToolEvent(this, view, new Rectangle(0, 0, -1, -1));
                }
                ((ToolListener) listeners[i + 1]).toolStarted(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireToolDone() {
        ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ToolEvent(this, getView(), new Rectangle(0, 0, -1, -1));
                }
                ((ToolListener) listeners[i + 1]).toolDone(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(Rectangle2D.Double r) {
        Point p1 = getView().drawingToView(new Point2D.Double(r.x, r.y));
        Point p2 = getView().drawingToView(new Point2D.Double(r.x + r.width, r.y + r.height));
        fireAreaInvalidated(
                new Rectangle(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y));
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireAreaInvalidated(Rectangle invalidatedArea) {
        ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ToolEvent(this, getView(), invalidatedArea);
                }
                ((ToolListener) listeners[i + 1]).areaInvalidated(event);
            }
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     * <p>
     * Note: This method only fires an event, if the invalidated area is outside of the canvas
     * bounds.
     */
    protected void maybeFireBoundsInvalidated(Rectangle invalidatedArea) {
        Drawing d = getDrawing();
        Rectangle2D.Double canvasBounds = new Rectangle2D.Double(0, 0, 0, 0);
        if (d.get(CANVAS_WIDTH) != null) {
            canvasBounds.width += d.get(CANVAS_WIDTH);
        }
        if (d.get(CANVAS_HEIGHT) != null) {
            canvasBounds.height += d.get(CANVAS_HEIGHT);
        }
        if (!canvasBounds.contains(invalidatedArea)) {
            fireBoundsInvalidated(invalidatedArea);
        }
    }

    /**
     * Notify all listenerList that have registered interest for notification on this event type.
     */
    protected void fireBoundsInvalidated(Rectangle invalidatedArea) {
        ToolEvent event = null;
        // Notify all listeners that have registered interest for
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ToolListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ToolEvent(this, getView(), invalidatedArea);
                }
                ((ToolListener) listeners[i + 1]).boundsInvalidated(event);
            }
        }
    }

    public void draw(Graphics2D g) {
    }

    public void updateCursor(DrawingView view, Point p) {
        if (view.isEnabled()) {
            Handle handle = view.findHandle(p);
            if (handle != null) {
                view.setCursor(handle.getCursor());
            } else {
                Figure figure = view.findFigure(p);
                Point2D.Double point = view.viewToDrawing(p);
                Drawing drawing = view.getDrawing();
                while (figure != null && !figure.isSelectable()) {
                    figure = drawing.findFigureBehind(point, figure);
                }
                if (figure != null) {
                    view.setCursor(figure.getCursor(view.viewToDrawing(p)));
                } else {
                    view.setCursor(Cursor.getDefaultCursor());
                }
            }
        } else {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }

    public String getToolTipText(DrawingView view, MouseEvent evt) {
        return null;
    }

    /**
     * Returns true, if this tool lets the user interact with handles.
     * <p>
     * Handles may draw differently, if interaction is not possible.
     *
     * @return True, if this tool supports interaction with the handles.
     */
    public boolean supportsHandleInteraction() {
        return false;
    }
}
