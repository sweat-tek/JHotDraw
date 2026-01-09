/*
 * @(#)AbstractTool.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.figure.Figure;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import org.jhotdraw.beans.AbstractBean;
import org.jhotdraw.draw.*;
import static org.jhotdraw.draw.AttributeKeys.*;
import org.jhotdraw.draw.event.ToolEvent;
import org.jhotdraw.draw.event.ToolListener;
import org.jhotdraw.draw.handle.Handle;

/**
 * This abstract class can be extended to implement a {@link Tool}.
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Proxy</em><br>
 * To remove the need for null-handling, {@code AbstractTool} makes use of a proxy for
 * {@code DrawingEditor}. Subject: {@link DrawingEditor}; Proxy: {@link DrawingEditorProxy}; Client:
 * {@link AbstractTool}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class AbstractTool extends BaseToolImpl implements Tool {

    private static final long serialVersionUID = 1L;
    /**
     * This is set to true, while the tool is doing some work. This prevents the currentView from
     * being changed when a mouseEnter event is received.
     */
    protected boolean isWorking;
    protected Point anchor = new Point();
    /*
     private PropertyChangeListener editorHandler;
     private PropertyChangeListener viewHandler;
     */

    /**
     * Creates a new instance.
     */
    public AbstractTool() {
        super();
        setInputMap(createInputMap());
        setActionMap(createActionMap());
    }

    @Override
    public void keyReleased(KeyEvent evt) {
        fireToolDone();
    }

    @Override
    public void keyTyped(KeyEvent evt) {
    }

    /**
     * The Tool uses the InputMap to determine what to do, when a key is pressed. If the
     * corresponding value of the InputMap is a String, the ActionMap of the tool is used, to find
     * the action to be performed. If the corresponding value of the InputMap is a ActionListener,
     * the actionPerformed method of the ActionListener is performed.
     */
    @Override
    public void keyPressed(KeyEvent evt) {
        if (!evt.isConsumed()) {
            if (evt.getSource() instanceof Container) {
                editor.setActiveView(editor.findView((Container) evt.getSource()));
            }
            Object obj = null;
            if (inputMap != null) {
                // Lookup the input map of the tool
                obj = inputMap.get(KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers(), false));
            }
            if (obj == null) {
                // Fall back to the input map of the drawing editor
                InputMap im = editor.getInputMap();
                if (im != null) {
                    obj = im.get(KeyStroke.getKeyStroke(evt.getKeyCode(), evt.getModifiers(), false));
                }
            }
            ActionListener al = null;
            if (obj instanceof ActionListener) {
                al = (ActionListener) obj;
            } else if (obj != null) {
                // Lookup the action map of the tool
                if (actionMap != null) {
                    al = actionMap.get(obj);
                }
                if (al == null) {
                    // Fall back to the action map of the drawing editor
                    al = editor.getActionMap().get(obj);
                }
            }
            if (al != null) {
                evt.consume();
                al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "tool", evt.getWhen(), evt.getModifiers()));
                fireToolDone();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        /*if (! isWorking) {
         editor.setActiveView(editor.findView((Container) evt.getSource()));
         }*/
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        DrawingView view = editor.findView((Container) evt.getSource());
        view.requestFocus();
        anchor = new Point(evt.getX(), evt.getY());
        isWorking = true;
        fireToolStarted(view);
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        isWorking = false;
    }

}
