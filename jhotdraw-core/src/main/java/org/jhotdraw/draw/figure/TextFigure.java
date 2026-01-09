/*
 * @(#)TextFigure.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.figure;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import org.jhotdraw.draw.AttributeKeys;
import static org.jhotdraw.draw.AttributeKeys.*;
import org.jhotdraw.draw.handle.BoundsOutlineHandle;
import org.jhotdraw.draw.handle.FontSizeHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.handle.MoveHandle;
import org.jhotdraw.draw.locator.RelativeLocator;
import org.jhotdraw.draw.tool.BaseTool;
import org.jhotdraw.draw.tool.TextEditingTool;
import org.jhotdraw.geom.Dimension2DDouble;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.util.*;
import org.jhotdraw.xml.DOMInput;
import org.jhotdraw.xml.DOMOutput;

/**
 * A {@code TextHolderFigure} which holds a single line of text.
 * <p>
 * A DrawingEditor should provide the {@link org.jhotdraw.draw.tool.TextCreationTool} to create a
 * {@code TextFigure}.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TextFigure extends AbstractAttributedDecoratedFigure
        implements TextHolderFigure {

private static final int MIN_COLUMN_COUNT = 4;
    private static final long serialVersionUID = 1L;
    protected Point2D.Double origin = new Point2D.Double();
    protected boolean editable = true;
    // cache of the TextFigure's layout
    protected transient TextLayout textLayout;

    /**
     * Creates a new instance.
     */
    public TextFigure() {
        this(ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels").
                getString("TextFigure.defaultText"));
    }

    public TextFigure(String text) {
        setText(text);
    }

    // DRAWING
    @Override
    protected void drawStroke(java.awt.Graphics2D graphics2D) {
        // Text Figures are treated as primitives, they do not support adding strokes.
    }

    @Override
    protected void drawFill(java.awt.Graphics2D graphics2D) {
        // Text Figures do not have a fill area to draw.
    }

    @Override
    protected void drawText(java.awt.Graphics2D canvas) {
        if (getText() != null || isEditable()) {
            TextLayout layout = getTextLayout();
            Graphics2D localGraphics = (Graphics2D) canvas.create();
            try {
                //Test if world to screen transformation mirrors the text. If so it tries to
                //unmirror it.
                if (localGraphics.getTransform().getScaleY() * localGraphics.getTransform().getScaleX() < 0) {
                    AffineTransform at = new AffineTransform();
                    at.translate(0, origin.y + layout.getAscent() / 2);
                    at.scale(1, -1);
                    at.translate(0, -origin.y - layout.getAscent() / 2);
                    localGraphics.transform(at);
                }
                layout.draw(localGraphics, (float) origin.x, (float) (origin.y + layout.getAscent()));
            } finally {
                localGraphics.dispose();
            }
        }
    }

    // SHAPE AND BOUNDS
    @Override
    public void transform(AffineTransform affineTransform) {
        affineTransform.transform(origin, origin);
    }

    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        origin = new Point2D.Double(anchor.x, anchor.y);
    }

    @Override
    public boolean figureContains(Point2D.Double p) {
        return getBounds().contains(p);
    }

    protected TextLayout getTextLayout() {
        if (textLayout == null) {
            String text = getText();
            if (text == null || text.isEmpty()) {
                text = " ";
            }
            FontRenderContext fontRenderContext = getFontRenderContext();
            HashMap<TextAttribute, Object> textAttributes = new HashMap<>();
            textAttributes.put(TextAttribute.FONT, getFont());
            if (get(FONT_UNDERLINE) != null && get(FONT_UNDERLINE)) {
                textAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
            }
            textLayout = new TextLayout(text, textAttributes, fontRenderContext);
        }
        return textLayout;
    }

    @Override
    public Rectangle2D.Double getBounds() {
        TextLayout layout = getTextLayout();
        return new Rectangle2D.Double(
                origin.x, origin.y,
                layout.getAdvance(),
                layout.getAscent() + layout.getDescent()
        );
    }

    @Override
    public Dimension2DDouble getPreferredSize() {
        Rectangle2D.Double bounds = getBounds();
        return new Dimension2DDouble(bounds.width, bounds.height);
    }

    @Override
    public double getBaseline() {
        TextLayout layout = getTextLayout();
        return origin.y + layout.getAscent() - getBounds().y;
    }

    /**
     * Gets the drawing area without taking the decorator into account.
     */
    @Override
    protected Rectangle2D.Double getFigureDrawingArea() {
        if (getText() == null) {
            return getBounds();
        } else {
            TextLayout layout = getTextLayout();
            Rectangle2D.Double rectangle = new Rectangle2D.Double(
                    origin.x, origin.y, layout.getAdvance(), layout.getAscent());
            Rectangle2D lBounds = layout.getBounds();
            if (!lBounds.isEmpty() && !Double.isNaN(lBounds.getX())) {
                rectangle.add(new Rectangle2D.Double(
                        lBounds.getX() + origin.x,
                        (lBounds.getY() + origin.y + layout.getAscent()),
                        lBounds.getWidth(),
                        lBounds.getHeight()));
            }
            // grow by two pixels to take antialiasing into account
            Geom.grow(rectangle, 2d, 2d);
            return rectangle;
        }
    }

    public void restoreTransformTo(Point2D.Double geometry) {
        origin.x = geometry.x;
        origin.y = geometry.y;
    }

    /**
     * @deprecated
     * Old implementation is not typesafe.
     * Use {@link #restoreTransformTo(Point2D.Double)} instead.
     */
    @Override
    @Deprecated
    public void restoreTransformTo(Object geometry) {
        Point2D.Double p = (Point2D.Double) geometry;
        origin.x = p.x;
        origin.y = p.y;
    }

    @Override
    public Object getTransformRestoreData() {
        return origin.clone();
    }

    // ATTRIBUTES
    /**
     * Gets the text shown by the text figure.
     */
    @Override
    public String getText() {
        return get(TEXT);
    }

    /**
     * Sets the text shown by the text figure. This is a convenience method for calling
     * {@code set(TEXT,newText)}.
     */
    @Override
    public void setText(String newText) {
        set(TEXT, newText);
    }

    @Override
    public int getTextColumns() {
        return MIN_COLUMN_COUNT;
    }

    /**
     * Gets the number of characters used to expand tabs.
     */
    @Override
    public int getTabSize() {
        return 8;
    }

    @Override
    public TextHolderFigure getLabelFor() {
        return this;
    }

    @Override
    public Insets2D.Double getInsets() {
        return new Insets2D.Double();
    }

    @Override
    public Font getFont() {
        return AttributeKeys.getFont(this);
    }

    @Override
    public Color getTextColor() {
        return get(TEXT_COLOR);
    }

    @Override
    public Color getFillColor() {
        return get(FILL_COLOR);
    }

    @Override
    public void setFontSize(float size) {
        set(FONT_SIZE, (double) size);
    }

    @Override
    public float getFontSize() {
        return get(FONT_SIZE).floatValue();
    }

    // EDITING
    @Override
    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public Collection<Handle> createHandles(int detailLevel) {
        LinkedList<Handle> handles = new LinkedList<>();

        if (detailLevel == -1) handles.add(new BoundsOutlineHandle(this, false, true));
        else if (detailLevel == 0) {
            handles.add(new BoundsOutlineHandle(this));
            handles.add(new MoveHandle(this, RelativeLocator.northWest()));
            handles.add(new MoveHandle(this, RelativeLocator.northEast()));
            handles.add(new MoveHandle(this, RelativeLocator.southWest()));
            handles.add(new MoveHandle(this, RelativeLocator.southEast()));
            handles.add(new FontSizeHandle(this));
        }
        return handles;
    }

    /**
     * Returns a specialized tool for the given coordinate.
     * <p>
     * Returns null, if no specialized tool is available.
     */
    @Override
    public BaseTool getTool(Point2D.Double p) {
        if (isEditable() && contains(p)) {
            return new TextEditingTool(this);
        }
        return null;
    }

    // CONNECTING
    // COMPOSITE FIGURES
    // CLONING
    // EVENT HANDLING
    @Override
    public void invalidate() {
        super.invalidate();
        textLayout = null;
    }

    @Override
    protected void validate() {
        super.validate();
        textLayout = null;
    }

    @Override
    public void read(DOMInput input) throws IOException {
        setBounds(
                new Point2D.Double(input.getAttribute("x", 0d), input.getAttribute("y", 0d)),
                new Point2D.Double(0, 0));
        readAttributes(input);
        readDecorator(input);
        invalidate();
    }

    @Override
    public void write(DOMOutput out) throws IOException {
        Rectangle2D.Double bounds = getBounds();
        out.addAttribute("x", bounds.x);
        out.addAttribute("y", bounds.y);
        writeAttributes(out);
        writeDecorator(out);
    }


    public static TextFigure createFrom(TextFigure figureToDuplicate) {
        TextFigure duplicate =  new TextFigure();
        duplicate.setText(figureToDuplicate.getText());
        duplicate.origin = (figureToDuplicate.origin == null) ? null : (Point2D.Double)  figureToDuplicate.origin;
        duplicate.setAttributes(figureToDuplicate.getAttributes());
        duplicate.textLayout = figureToDuplicate.textLayout;
        return duplicate;
    }


    /**
    * @deprecated Use {@link #createFrom(TextFigure)} instead.
     * This method is kept around for framework compatibility.
    * */
    // Using .clone is inherently flawed but unfortunately legacy concerns keep it around.
    // I've deprecated it with reference to the copy factory above.
    @Deprecated
    @Override
    @SuppressWarnings("java:S2975")
    public TextFigure clone() {
        TextFigure that = (TextFigure) super.clone();
        that.origin = (Point2D.Double) this.origin.clone();
        that.textLayout = null;
        return that;
    }

    @Override
    public boolean isTextOverflow() {
        return false;
    }
}
