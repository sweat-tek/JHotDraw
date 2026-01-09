/*
 * @(#)LabelFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.geom.*;
import java.util.*;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.tool.BaseTool;
import org.jhotdraw.draw.tool.TextEditingTool;


/**
 * A LabelFigure can be used to provide more double clickable area for a
 * TextHolderFigure.
 *
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class LabelFigure extends TextFigure {

    private static final long serialVersionUID = 1L;
    private TextHolderFigure target;
    private final transient EventHandler eventHandler = new EventHandler();


    private class EventHandler implements FigureListener {
        @Override
        public void figureRemoved(FigureEvent e) {
            if (e.getFigure() == target) {
                target.removeFigureListener(this);
                target = null;
            }
        }
    }


    /**
     * Creates a new instance.
     */
    public LabelFigure() {
        this("Label");
    }

    public LabelFigure(String text) {
        setText(text);
        setEditable(false);
    }


    public void setLabelFor(TextHolderFigure target) {
        if (this.target != null) {
            this.target.removeFigureListener(eventHandler);
        }
        this.target = target;
        if (this.target != null) {
            this.target.addFigureListener(eventHandler);
        }
    }



    @Override
    public TextHolderFigure getLabelFor() {
        return (target == null) ? this : target;
    }

    /**
     * Returns a specialized tool for the given coordinate.
     * <p>
     * Returns null, if no specialized tool is available.
     */
    @Override
    public BaseTool getTool(Point2D.Double coordinate) {
        return (target != null && contains(coordinate)) ? new TextEditingTool(target) : null;
    }







    @Override
    public void remap(Map<Figure, Figure> oldToNew, boolean disconnectIfNotInMap) {
        super.remap(oldToNew, disconnectIfNotInMap);
        if (target != null) {
            Figure newTarget = oldToNew.get(target);
            if (newTarget != null) {
                target.removeFigureListener(eventHandler);
                target = (TextHolderFigure) newTarget;
                newTarget.addFigureListener(eventHandler);
            }
        }
    }


}
