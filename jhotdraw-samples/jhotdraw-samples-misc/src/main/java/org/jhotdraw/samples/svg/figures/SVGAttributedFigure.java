/*
 * @(#)SVGAttributedFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.AbstractAttributedFigure;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import org.jhotdraw.draw.*;
import static org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH;
import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;
import org.jhotdraw.util.*;

/**
 * SVGAttributedFigure.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public abstract class SVGAttributedFigure extends AbstractAttributedFigure {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     */

    protected SVGAttributedFigure() {
    }

    @Override
    @FeatureEntryPoint(value = "Draw")
    public void draw(Graphics2D g) {
        double opacity = get(OPACITY);
        opacity = Math.min(Math.max(0d, opacity), 1d);

        if (opacity == 0d) { return; }
        if (opacity == 1d ) { drawFigure(g); return; }

        Rectangle2D.Double drawingArea = getDrawingArea();
        clipDrawingArea(g, drawingArea);

        if (!drawingArea.isEmpty()) {
            BufferedImage buf = createBufferedImage(g, drawingArea);
            drawBufferedImage(g, buf, drawingArea, opacity);
        }
    }

    private void clipDrawingArea(Graphics2D g, Rectangle2D.Double drawingArea) {
        Rectangle2D clipBounds = g.getClipBounds();
        if (clipBounds != null) {
            Rectangle2D.intersect(drawingArea, clipBounds, drawingArea);
        }
    }

    private BufferedImage createBufferedImage(Graphics2D g, Rectangle2D.Double drawingArea) {
        int imageWidth = Math.max(1, (int) ((2 + drawingArea.width) * g.getTransform().getScaleX()));
        int imageHeight = Math.max(1, (int) ((2 + drawingArea.height) * g.getTransform().getScaleY()));

        BufferedImage buf = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = buf.createGraphics();
        prepareGraphicsForBufferedImage(gr, g.getTransform(), drawingArea);
        drawFigure(gr);
        gr.dispose();

        return buf;
    }

    private void prepareGraphicsForBufferedImage(Graphics2D gr, AffineTransform transform, Rectangle2D.Double drawingArea) {
        gr.scale(transform.getScaleX(), transform.getScaleY());
        gr.translate((int) -drawingArea.x, (int) -drawingArea.y);
        gr.setRenderingHints(gr.getRenderingHints()); // Copy rendering hints
    }

    private void drawBufferedImage(Graphics2D g, BufferedImage buf, Rectangle2D.Double drawingArea, double opacity) {
        Composite savedComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
        g.drawImage(buf, (int) drawingArea.x, (int) drawingArea.y,
                2 + (int) drawingArea.width, 2 + (int) drawingArea.height, null);
        g.setComposite(savedComposite);
    }

    /**
     * This method is invoked before the rendered image of the figure is
     * composited.
     */
    @FeatureEntryPoint(value = "Draw rectangle")
    public void drawFigure(Graphics2D g) {
        AffineTransform savedTransform = null;
        if (get(TRANSFORM) != null) {
            savedTransform = g.getTransform();
            g.transform(get(TRANSFORM));
        }

        setFillPaint(g);
        setStrokePaint(g);

        if (get(TRANSFORM) != null) {
            g.setTransform(savedTransform);
        }
    }

    public void setFillPaint(Graphics2D g) {
        Paint paint = SVGAttributeKeys.getFillPaint(this);
        if (paint != null) {
            g.setPaint(paint);
            drawFill(g);
        }
    }

    public void setStrokePaint(Graphics2D g) {
        Paint paint = SVGAttributeKeys.getStrokePaint(this);
        if (paint != null && get(STROKE_WIDTH) > 0) {
            g.setPaint(paint);
            g.setStroke(AttributeKeys.getStroke(this, 1.0));
            drawStroke(g);
        }
    }

    @Override
    public <T> void set(AttributeKey<T> key, T newValue) {
        if (key == TRANSFORM) {
            invalidate();
        }
        super.set(key, newValue);
    }

    @Override
    public Collection<Action> getActions(Point2D.Double p) {
        LinkedList<Action> actions = new LinkedList<>();
        if (get(TRANSFORM) != null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
            actions.add(new AbstractAction(labels.getString("edit.removeTransform.text")) {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent evt) {
                    willChange();
                    fireUndoableEditHappened(
                            TRANSFORM.setUndoable(SVGAttributedFigure.this, null)
                    );
                    changed();
                }
            });
        }
        return actions;
    }
}
