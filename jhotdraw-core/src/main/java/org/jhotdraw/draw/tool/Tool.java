/*
 * @(#)Tool.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.event.ToolListener;

/**
 * @deprecated
 * A <em>tool</em> defines an editing mode of a {@link DrawingEditor}.
 * <p>
 * Tools are used for user interaction. Unlike figures, a tool works with
 * the user interface coordinates of a {@link DrawingView}. The user interface
 * coordinates are expressed in integer pixels.
 * <p>
 * When a tool is set on a drawing editor using {@code setTool}, the drawing
 * editor registers the tool as a listener for mouse and keyboard events on all
 * drawing views. The tool processes these events and perform edit operations
 * on the figures of the drawing, on the drawing itself or even on the drawing
 * view.
 * <p>
 * Tools inform listeners when they are done with an interaction by calling
 * the ToolListener's {@code toolDone()} method.
 * <p>
 * In a typical application, tools are created once and reused.
 * <p>
 * A tool forwards UndoableEdit events to the Drawing object onto which it
 * is performing changes.
 * <p>
 * If a tool does not handle a specific keyboard event, it looks up the
 * {@code InputMap} of the drawing editor and then invokes the
 * corresponding action in the {@code ActionMap} of the drawing editor.
 * A tool can have an InputMap and ActionMap of its own which take precedence
 * over the ones provided by the drawing editor.
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Framework</em><br>
 * The following interfaces define the contracts of a framework for structured
 * drawing editors:<br>
 * Contract: {@link Drawing}, {@link Figure}, {@link DrawingView},
 * {@link DrawingEditor}, {@link org.jhotdraw.draw.handle.Handle} and {@link Tool}.
 *
 * <p>
 * <em>Mediator</em><br>
 * {@code DrawingEditor} acts as a mediator for coordinating drawing tools
 * and drawing views:<br>
 * Mediator: {@link DrawingEditor}; Colleagues: {@link DrawingView}, {@link Tool}.
 *
 * <p>
 * <em>Mediator</em><br>
 * {@code DrawingEditor} acts as a mediator for coordinating keyboard input from
 * {@code Tool}s and Swing action objects:<br>
 * Mediator: {@link DrawingEditor}; Colleagues: {@link Tool}, javax.swing.Action.
 *
 * <p>
 * <em>Observer</em><br>
 * State changes of tools can be observed by other objects. Specifically
 * {@code DrawingEditor} observes area invalidations of tools and repaints
 * its active drawing view accordingly.<br>
 * Subject: {@link Tool}; Observer: {@link ToolListener}; Event:
 * {@link org.jhotdraw.draw.event.ToolEvent}; Concrete Observer: {@link DrawingEditor}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
@Deprecated
public interface Tool extends BaseTool, ClickListeningTool, DragableTool, KeyListeningTool {
    // Legacy God Tool to maintain compatibility
}
