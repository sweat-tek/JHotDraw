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
 * Aligns the selected figures.
 *
 * Refactored to reduce duplicate code and improve readability with descriptive variable names.
 *
 * @version Refactored version
 */
public abstract class AlignAction extends AbstractSelectedAction {
    private static final long serialVersionUID = 1L;
    protected ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");

    public AlignAction(DrawingEditor editor) {
        super(editor);
        updateEnabledState();
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

    protected abstract void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds);

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

    public static class North extends AlignAction {
        public North(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignNorth");
        }
        public North(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignNorth");
        }


        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double topY = selectionBounds.y;
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(0, topY - figureBounds.y);
                transformFigure(figure, translateTransform);
            }
        }
    }

    public static class East extends AlignAction {
        public East(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignEast");
        }
        public East(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignEast");
        }

        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double rightX = selectionBounds.x + selectionBounds.width;
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(rightX - figureBounds.x - figureBounds.width, 0);
                transformFigure(figure, translateTransform);
            }
        }
    }

    public static class West extends AlignAction {
        public West(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignWest");
        }

        public West(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignWest");
        }

        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double leftX = selectionBounds.x;
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(leftX - figureBounds.x, 0);
                transformFigure(figure, translateTransform);
            }
        }
    }

    public static class South extends AlignAction {
        public South(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignSouth");
        }
        public South(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignSouth");
        }

        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double bottomY = selectionBounds.y + selectionBounds.height;
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(0, bottomY - figureBounds.y - figureBounds.height);
                transformFigure(figure, translateTransform);
            }
        }
    }

    public static class Vertical extends AlignAction {
        public Vertical(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignVertical");
        }

        public Vertical(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignVertical");
        }

        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double centerY = selectionBounds.y + (selectionBounds.height / 2);
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(0, centerY - figureBounds.y - (figureBounds.height / 2));
                transformFigure(figure, translateTransform);
            }
        }
    }

    public static class Horizontal extends AlignAction {
        public Horizontal(DrawingEditor editor) {
            super(editor);
            labels.configureAction(this, "edit.alignHorizontal");
        }

        public Horizontal(DrawingEditor editor, ResourceBundleUtil labels) {
            super(editor);
            this.labels = labels; // Assign the provided labels
            labels.configureAction(this, "edit.alignHorizontal");
        }

        @Override
        protected void alignFigures(Collection<Figure> selectedFigures, Rectangle2D.Double selectionBounds) {
            double centerX = selectionBounds.x + (selectionBounds.width / 2);
            for (Figure figure : selectedFigures) {
                Rectangle2D.Double figureBounds = figure.getBounds();
                AffineTransform translateTransform = createTranslateTransform(centerX - figureBounds.x - (figureBounds.width / 2), 0);
                transformFigure(figure, translateTransform);
            }
        }
    }
}
