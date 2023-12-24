/*
 * @(#)SVGRect.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.figures;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.geom.GrowStroke;
import org.jhotdraw.samples.svg.Gradient;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;

/**
 * SVGRect.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SVGRectFigure extends SVGAttributedFigure implements SVGFigure {

    private static final long serialVersionUID = 1L;
    /**
     * The variable acv is used for generating the locations of the control
     * points for the rounded rectangle using path.curveTo.
     */
    private static final double ACV;

    static {
        double angle = Math.PI / 4.0;
        double a = 1.0 - Math.cos(angle);
        double b = Math.tan(angle);
        double c = Math.sqrt(1.0 + b * b) - 1 + a;
        double cv = 4.0 / 3.0 * a * b / c;
        ACV = (1.0 - cv);
    }
    /**
     */
    private RoundRectangle2D.Double roundRect;
    /**
     * This is used to perform faster drawing.
     */
    private transient Shape cachedTransformedShape;
    /**
     * This is used to perform faster hit testing.
     */
    private transient Shape cachedHitShape;

    /**
     * Creates a new instance.
     */
    public SVGRectFigure() {
        this(0, 0, 0, 0);
    }

    public SVGRectFigure(double x, double y, double width, double height) {
        this(x, y, width, height, 0, 0);
    }
    public SVGRectFigure(double x, double y, double width, double height, double rx, double ry) {
        roundRect = new RoundRectangle2D.Double(x, y, width, height, rx, ry);
        SVGAttributeKeys.setDefaults(this);
        setConnectable(false);
    }

    // DRAWING
    @Override
    protected void drawFill(Graphics2D g) {
        if (getArcHeight() == 0d && getArcWidth() == 0d) {
            g.fill(roundRect.getBounds2D());
        } else {
            g.fill(roundRect);
        }
    }

    @Override
    protected void drawStroke(Graphics2D g) {
        if (roundRect.archeight == 0 && roundRect.arcwidth == 0) {
            g.draw(roundRect.getBounds2D());
        } else {
            // We have to generate the path for the round rectangle manually,
            // because the path of a Java RoundRectangle is drawn counterclockwise
            // whereas an SVG rect needs to be drawn clockwise.
            Path2D.Double p = new Path2D.Double();
            double aw = roundRect.arcwidth / 2d;
            double ah = roundRect.archeight / 2d;
            p.moveTo((roundRect.x + aw), (float) roundRect.y);
            p.lineTo((roundRect.x + roundRect.width - aw), (float) roundRect.y);
            p.curveTo((roundRect.x + roundRect.width - aw * ACV), (float) roundRect.y,
                    (roundRect.x + roundRect.width), (float) (roundRect.y + ah * ACV),
                    (roundRect.x + roundRect.width), (roundRect.y + ah));
            p.lineTo((roundRect.x + roundRect.width), (roundRect.y + roundRect.height - ah));
            p.curveTo(
                    (roundRect.x + roundRect.width), (roundRect.y + roundRect.height - ah * ACV),
                    (roundRect.x + roundRect.width - aw * ACV), (roundRect.y + roundRect.height),
                    (roundRect.x + roundRect.width - aw), (roundRect.y + roundRect.height));
            p.lineTo((roundRect.x + aw), (roundRect.y + roundRect.height));
            p.curveTo((roundRect.x + aw * ACV), (roundRect.y + roundRect.height),
                    (roundRect.x), (roundRect.y + roundRect.height - ah * ACV),
                    (float) roundRect.x, (roundRect.y + roundRect.height - ah));
            p.lineTo((float) roundRect.x, (roundRect.y + ah));
            p.curveTo((roundRect.x), (roundRect.y + ah * ACV),
                    (roundRect.x + aw * ACV), (float) (roundRect.y),
                    (float) (roundRect.x + aw), (float) (roundRect.y));
            p.closePath();
            g.draw(p);
        }
    }


    // SHAPE AND BOUNDS
    public double getX() {
        return roundRect.x;
    }

    public double getY() {
        return roundRect.y;
    }

    public double getWidth() {
        return roundRect.width;
    }

    public double getHeight() {
        return roundRect.height;
    }

    /**
     * Gets the arc width.
     */
    public double getArcWidth() {
        return roundRect.arcwidth;
    }

    /**
     * Gets the arc height.
     */
    public double getArcHeight() {
        return roundRect.archeight;
    }

    /**
     * Sets the arc width.
     */
    public void setArcWidth(double newValue) {
        double oldValue = roundRect.arcwidth;
        roundRect.arcwidth = newValue;
        firePropertyChange(PROPERTY.ARC_WIDTH.getName(), oldValue, newValue);
    }

    /**
     * Sets the arc height.
     */
    public void setArcHeight(double newValue) {
        double oldValue = roundRect.archeight;
        roundRect.archeight = newValue;
        firePropertyChange(PROPERTY.ARC_HEIGHT.getName(), oldValue, newValue);
    }

    /**
     * Convenience method for setting both the arc width and the arc height.
     */
    public void setArc(double width, double height) {
        setArcWidth(width);
        setArcHeight(height);
    }

    @Override
    public Rectangle2D.Double getBounds() {
        return (Rectangle2D.Double) roundRect.getBounds2D();
    }

    @Override
    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D rx = getTransformedShape().getBounds2D();
        Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rx : new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
        if (get(TRANSFORM) == null) {
            double g = SVGAttributeKeys.getPerpendicularHitGrowth(this, 1.0) * 2d + 1d;
            Geom.grow(r, g, g);
        } else {
            double strokeTotalWidth = AttributeKeys.getStrokeTotalWidth(this, 1.0);
            double width = strokeTotalWidth / 2d;
            if (get(STROKE_JOIN) == BasicStroke.JOIN_MITER) {
                width *= get(STROKE_MITER_LIMIT);
            }
            if (get(STROKE_CAP) != BasicStroke.CAP_BUTT) {
                width += strokeTotalWidth * 2;
            }
            width++;
            Geom.grow(r, width, width);
        }
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */

    @Override
    public boolean contains(Point2D.Double p) {
        return getHitShape().contains(p);
    }

    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        invalidateTransformedShape();
        roundRect.x = Math.min(anchor.x, lead.x);
        roundRect.y = Math.min(anchor.y, lead.y);
        roundRect.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        roundRect.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
        invalidate();
    }

    private void invalidateTransformedShape() {
        cachedTransformedShape = null;
        cachedHitShape = null;
    }

    private Shape getTransformedShape() {
        if (cachedTransformedShape == null) {
            if (getArcHeight() == 0 || getArcWidth() == 0) {
                cachedTransformedShape = roundRect.getBounds2D();
            } else {
                cachedTransformedShape = (Shape) roundRect.clone();
            }
            if (get(TRANSFORM) != null) {
                cachedTransformedShape = get(TRANSFORM).createTransformedShape(cachedTransformedShape);
            }
        }
        return cachedTransformedShape;
    }

    private Shape getHitShape() {
        if (cachedHitShape == null) {
            if (get(FILL_COLOR) != null || get(FILL_GRADIENT) != null) {
                cachedHitShape = new GrowStroke(
                        (float) AttributeKeys.getStrokeTotalWidth(this, 1.0) / 2f,
                        (float) AttributeKeys.getStrokeTotalMiterLimit(this, 1.0)).createStrokedShape(getTransformedShape());
            } else {
                cachedHitShape = AttributeKeys.getHitStroke(this, 1.0).createStrokedShape(getTransformedShape());
            }
        }
        return cachedHitShape;
    }

    /**
     * Transforms the figure.
     *
     * @param tx The transformation.
     */
    @Override
    public void transform(AffineTransform tx) {
        invalidateTransformedShape();
        if (get(TRANSFORM) != null
                ||
                (tx.getType() & (AffineTransform.TYPE_TRANSLATION)) != tx.getType()) {
            if (get(TRANSFORM) == null) {
                set(TRANSFORM, (AffineTransform) tx.clone());
            } else {
                AffineTransform t = TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                set(TRANSFORM, t);
            }
        } else {
            Point2D.Double anchor = getStartPoint();
            Point2D.Double lead = getEndPoint();
            setBounds(
                    (Point2D.Double) tx.transform(anchor, anchor),
                    (Point2D.Double) tx.transform(lead, lead));
            if (get(FILL_GRADIENT) != null
                    && !get(FILL_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = FILL_GRADIENT.getClone(this);
                g.transform(tx);
                set(FILL_GRADIENT, g);
            }
            if (get(STROKE_GRADIENT) != null
                    && !get(STROKE_GRADIENT).isRelativeToFigureBounds()) {
                Gradient g = STROKE_GRADIENT.getClone(this);
                g.transform(tx);
                set(STROKE_GRADIENT, g);
            }
        }
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        invalidateTransformedShape();
        Object[] restoreData = (Object[]) geometry;
        roundRect = (RoundRectangle2D.Double) ((RoundRectangle2D.Double) restoreData[0]).clone();
        TRANSFORM.setClone(this, (AffineTransform) restoreData[1]);
        FILL_GRADIENT.setClone(this, (Gradient) restoreData[2]);
        STROKE_GRADIENT.setClone(this, (Gradient) restoreData[3]);
    }

    @Override
    public Object getTransformRestoreData() {
        return new Object[]{
            roundRect.clone(),
            TRANSFORM.getClone(this),
            FILL_GRADIENT.getClone(this),
            STROKE_GRADIENT.getClone(this)};
    }

    // EDITING
    @Override
    @FeatureEntryPoint(value = "Edit a rectangle")
    public Collection<Handle> createHandles(int detailLevel) {
        HandleCreator handleCreator;
        switch (detailLevel % 2) {
            case -1: // Mouse hover handles
                handleCreator = new MouseHoverHandleCreator();
                break;
            case 0:
                handleCreator = new ZeroDetailLevelHandleCreator();
                break;
            case 1:
                handleCreator = new OneDetailLevelHandleCreator();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported detail level");
        }

        return handleCreator.createHandles(this);
    }

    // CLONING
    @Override
    public SVGRectFigure clone() {
        SVGRectFigure that = (SVGRectFigure) super.clone();
        that.roundRect = (RoundRectangle2D.Double) this.roundRect.clone();
        that.cachedTransformedShape = null;
        that.cachedHitShape = null;
        return that;
    }

    @Override
    public boolean isEmpty() {
        Rectangle2D.Double b = getBounds();
        return b.width <= 0 || b.height <= 0;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateTransformedShape();
    }
}
