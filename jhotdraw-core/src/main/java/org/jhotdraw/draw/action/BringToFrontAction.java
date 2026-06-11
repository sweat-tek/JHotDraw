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
    protected void apply(DrawingView view, Collection<Figure> figures) {
        bringToFront(view, figures);
    }

    @Override
    protected void revert(DrawingView view, Collection<Figure> figures) {
        SendToBackAction.sendToBack(view, figures);
    }

    public static void bringToFront(DrawingView view, Collection<Figure> figures) {
        Drawing drawing = view.getDrawing();
        for (Figure figure : drawing.sort(figures)) {
            drawing.bringToFront(figure);
        }
    }
}
