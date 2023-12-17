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
 *
 * Refactored to use an enumeration for alignment types, improving maintainability.
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

    private String formatAlignmentName(AlignmentType alignmentType) {
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
        CompositeEdit compositeEdit = new CompositeEdit(labels.getString("edit.align.text"));
        fireUndoableEditHappened(compositeEdit);
        alignFigures(getView().getSelectedFigures(), getSelectionBounds());
        fireUndoableEditHappened(compositeEdit);
    }

    protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        switch (alignmentType) {
            case NORTH:
                alignNorth(selectedFigures, selectionBounds);
                break;
            case EAST:
                alignEast(selectedFigures, selectionBounds);
                break;
            case WEST:
                alignWest(selectedFigures, selectionBounds);
                break;
            case SOUTH:
                alignSouth(selectedFigures, selectionBounds);
                break;
            case VERTICAL:
                alignVertical(selectedFigures, selectionBounds);
                break;
            case HORIZONTAL:
                alignHorizontal(selectedFigures, selectionBounds);
                break;
        }
    }

    protected Rectangle2D.Double getSelectionBounds() {
        Rectangle2D.Double bounds = null;
        for (Figure figure : getView().getSelectedFigures()) {
            bounds = bounds == null ? figure.getBounds() : unionBounds(bounds, figure.getBounds());
        }
        return bounds;
    }

    private Rectangle2D.Double unionBounds(Rectangle2D.Double existingBounds, Rectangle2D.Double newBounds) {
        existingBounds.add(newBounds);
        return existingBounds;
    }

    protected void transformFigure(Figure figure, AffineTransform transform) {
        if (figure.isTransformable()) {
            figure.willChange();
            figure.transform(transform);
            figure.changed();
            fireUndoableEditHappened(new TransformEdit(figure, transform));
        }
    }

    protected AffineTransform createTranslateTransform(double deltaX, double deltaY) {
        return new AffineTransform(1, 0, 0, 1, deltaX, deltaY);
    }

    private void alignNorth(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double topY = selectionBounds.y;
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(0, topY - figureBounds.y);
            transformFigure(figure, translateTransform);
        }
    }

    private void alignEast(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double rightX = selectionBounds.x + selectionBounds.width;
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(rightX - figureBounds.x - figureBounds.width, 0);
            transformFigure(figure, translateTransform);
        }
    }

    private void alignWest(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double leftX = selectionBounds.x;
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(leftX - figureBounds.x, 0);
            transformFigure(figure, translateTransform);
        }
    }

    private void alignSouth(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double bottomY = selectionBounds.y + selectionBounds.height;
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(0, bottomY - figureBounds.y - figureBounds.height);
            transformFigure(figure, translateTransform);
        }
    }

    private void alignVertical(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double centerY = selectionBounds.y + (selectionBounds.height / 2);
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(0, centerY - figureBounds.y - (figureBounds.height / 2));
            transformFigure(figure, translateTransform);
        }
    }

    private void alignHorizontal(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
        double centerX = selectionBounds.x + (selectionBounds.width / 2);
        for (Figure figure : selectedFigures) {
            Rectangle2D.Double figureBounds = figure.getBounds();
            AffineTransform translateTransform = createTranslateTransform(centerX - figureBounds.x - (figureBounds.width / 2), 0);
            transformFigure(figure, translateTransform);
        }
    }
}
