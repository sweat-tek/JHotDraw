/*
 * @(#)SendToBackAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ReversedList;

/**
 * SendToBackAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SendToBackAction extends AbstractZOrderAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.sendToBack";

    /**
     * Creates a new instance.
     */
    public SendToBackAction(DrawingEditor editor) {
        super(editor, ID);
    }

    @Override
    protected void reorder(DrawingView view, Collection<Figure> figures) {
        sendToBack(view, figures);
    }

    @Override
    protected void reverseReorder(DrawingView view, Collection<Figure> figures) {
        BringToFrontAction.bringToFront(view, figures);
    }

    public static void sendToBack(DrawingView view, Collection<Figure> figures) {
        Objects.requireNonNull(view, "DrawingView must not be null.");
        Objects.requireNonNull(figures, "Figures collection must not be null.");
        Drawing drawing = Objects.requireNonNull(
                view.getDrawing(),
                "DrawingView must provide a drawing.");
        List<Figure> sorted = drawing.sort(figures);
        for (Figure figure : new ReversedList<>(sorted)) {
            drawing.sendToBack(figure);
        }
    }
}
