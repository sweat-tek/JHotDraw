package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.util.ResourceBundleUtil;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;



public abstract class AbstractCreationTool extends BaseToolImpl implements ClickListeningTool {

    protected boolean isWorking;
    protected Point anchor = new Point();
    protected String presentationName;


    /**
     * Attributes to be applied to the created ConnectionFigure. These attributes override the
     * default attributes of the DrawingEditor.
     */
    protected Map<AttributeKey<?>, Object> prototypeAttributes;
    /**
     * The prototype for new figures.
     */
    protected Figure prototype;
    /**
     * The created figure.
     */
    protected Figure createdFigure;
    /**
     * If this is set to false, the CreationTool does not fire toolDone after a new Figure has been
     * created. This allows to create multiple figures consecutively.
     */
    private boolean isToolDoneAfterCreation = true;

    public AbstractCreationTool(Figure prototype, Map<AttributeKey<?>, Object> prototypeAttributes, String name) {
        super();
        this.prototype = prototype;
        this.prototypeAttributes = prototypeAttributes;

        if (name == null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            name = labels.getString("edit.createFigure.text");
        }
    }

    public AbstractCreationTool(Figure prototype) {
        this(prototype, null, null);
    }


    public AbstractCreationTool(String prototypeClassName, Map<AttributeKey<?>, Object> attributes, String name) {
        try {
            this.prototype = (Figure) Class.forName(prototypeClassName).newInstance();
        } catch (Exception e) {
            InternalError error = new InternalError("Unable to create Figure from " + prototypeClassName);
            error.initCause(e);
            throw error;
        }
        this.prototypeAttributes = attributes;
        if (name == null) {
            ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
            name = labels.getString("edit.createFigure.text");
        }
        this.presentationName = name;
    }

    public AbstractCreationTool(Figure prototype, Map<AttributeKey<?>, Object> attributes) {
        this(prototype, attributes, null);
    }

    public Figure getPrototype() {
        return prototype;
    }

    @Override
    public void activate(DrawingEditor editor) {
        super.activate(editor);
        if (getView() != null) {
            getView().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    @Override
    public void deactivate(DrawingEditor editor) {
        super.deactivate(editor);
        if (getView() != null) {
            getView().setCursor(Cursor.getDefaultCursor());
        }
        if (createdFigure != null) {
            if (createdFigure instanceof CompositeFigure) {
                ((CompositeFigure) createdFigure).layout();
            }
            createdFigure = null;
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        isWorking = false;
    }



    @SuppressWarnings("unchecked")
    protected Figure createFigure() {
        Figure f = prototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        if (prototypeAttributes != null) {
            for (Map.Entry<AttributeKey<?>, Object> entry : prototypeAttributes.entrySet()) {
                f.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
            }
        }
        return f;
    }

    protected Figure getCreatedFigure() {
        return createdFigure;
    }

    protected Figure getAddedFigure() {
        return createdFigure;
    }

    /**
     * This method allows subclasses to do perform additonal user interactions after the new figure
     * has been created. The implementation of this class just invokes fireToolDone.
     */
    protected void creationFinished(Figure createdFigure) {
        if (createdFigure.isSelectable()) {
            getView().addToSelection(createdFigure);
        }
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    /**
     * If this is set to false, the CreationTool does not fire toolDone after a new Figure has been
     * created. This allows to create multiple figures consecutively.
     */
    public void setToolDoneAfterCreation(boolean newValue) {
        boolean oldValue = isToolDoneAfterCreation;
        isToolDoneAfterCreation = newValue;
    }

    /**
     * Returns true, if this tool fires toolDone immediately after a new figure has been created.
     */
    public boolean isToolDoneAfterCreation() {
        return isToolDoneAfterCreation;
    }

    @Override
    public void updateCursor(DrawingView view, Point p) {
        if (view.isEnabled()) {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }
}
