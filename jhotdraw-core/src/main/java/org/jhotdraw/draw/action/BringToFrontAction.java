/*
 * @(#)BringToFrontAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import java.util.*;
import org.jhotdraw.draw.*;

/**
 * ToFrontAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class BringToFrontAction extends AbstractZOrderAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.bringToFront";

    /**
     * Creates a new instance.
     */
    public BringToFrontAction(DrawingEditor editor) {
        super(editor, ID);
    }

    @Override
    protected void reorder(DrawingView view, Collection<Figure> figures) {
        bringToFront(view, figures);
    }

    @Override
    protected void reverseReorder(DrawingView view, Collection<Figure> figures) {
        SendToBackAction.sendToBack(view, figures);
    }

    public static void bringToFront(DrawingView view, Collection<Figure> figures) {
        Objects.requireNonNull(view, "DrawingView must not be null.");
        Objects.requireNonNull(figures, "Figures collection must not be null.");
        Drawing drawing = Objects.requireNonNull(
                view.getDrawing(),
                "DrawingView must provide a drawing.");
        for (Figure figure : drawing.sort(figures)) {
            drawing.bringToFront(figure);
        }
    }
}
