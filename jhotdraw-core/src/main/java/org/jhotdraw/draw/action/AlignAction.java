package org.jhotdraw.draw.action;

import java.awt.geom.*;
import java.util.*;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.event.TransformEdit;
import org.jhotdraw.undo.CompositeEdit;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Aligns the selected figures based on the specified alignment type.
 */
public class AlignAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;
    protected ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
    private final AlignmentType alignmentType;

    public enum AlignmentType {
        NORTH, EAST, WEST, SOUTH, VERTICAL, HORIZONTAL
    }

    public AlignAction(DrawingEditor editor, AlignmentType alignmentType) {
        super(editor);
        this.alignmentType = alignmentType;
        updateEnabledState();
        labels.configureAction(this, "edit.align" + formatAlignmentName(alignmentType));
    }

    public AlignAction(DrawingEditor editor, AlignmentType alignmentType, ResourceBundleUtil labels) {
        super(editor);
        this.labels = labels;
        this.alignmentType = alignmentType;
        labels.configureAction(this, "edit.align" + formatAlignmentName(alignmentType));
    }

    String formatAlignmentName(AlignmentType alignmentType) {
        String name = alignmentType.name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    @Override
    public void updateEnabledState() {
        setEnabled(getView() != null && getView().isEnabled() && getView().getSelectionCount() > 1);
    }

    @FeatureEntryPoint(value = "align")
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        System.out.println("Something Happened");
        CompositeEdit compositeEdit = new CompositeEdit(labels.getString("edit.align.text"));
        fireUndoableEditHappened(compositeEdit);
        alignFigures(getView().getSelectedFigures(), getSelectionBounds());
        fireUndoableEditHappened(compositeEdit);
    }

    private void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        for (Figure figure : selectedFigures) {
            Point2D.Double delta = calculateAlignmentDelta(figure.getBounds(), selectionBounds);
            AffineTransform translateTransform = new AffineTransform(1, 0, 0, 1, delta.x, delta.y);
            transformFigure(figure, translateTransform);
        }
    }

    Point2D.Double calculateAlignmentDelta(Rectangle2D.Double figureBounds, Rectangle2D.Double selectionBounds) {
        double deltaX = 0;
        double deltaY = 0;

        switch (alignmentType) {
            case NORTH:
                deltaY = selectionBounds.y - figureBounds.y;
                break;
            case EAST:
                deltaX = selectionBounds.getMaxX() - figureBounds.getMaxX();
                break;
            case WEST:
                deltaX = selectionBounds.getMinX() - figureBounds.x;
                break;
            case SOUTH:
                deltaY = selectionBounds.getMaxY() - figureBounds.getMaxY();
                break;
            case VERTICAL:
                deltaY = selectionBounds.getCenterY() - figureBounds.getCenterY();
                break;
            case HORIZONTAL:
                deltaX = selectionBounds.getCenterX() - figureBounds.getCenterX();
                break;
        }
        return new Point2D.Double(deltaX, deltaY);
    }

    protected Rectangle2D.Double getSelectionBounds() {
        Rectangle2D.Double bounds = new Rectangle2D.Double();
        boolean isFirst = true;

        for (Figure figure : getView().getSelectedFigures()) {
            if (isFirst) {
                bounds.setRect(figure.getBounds());
                isFirst = false;
            } else {
                bounds.add(figure.getBounds());
            }
        }
        return bounds;
    }

    protected void transformFigure(Figure figure, AffineTransform transform) {
        if (figure.isTransformable()) {
            figure.willChange();
            figure.transform(transform);
            figure.changed();
            fireUndoableEditHappened(new TransformEdit(figure, transform));
        }
    }
}