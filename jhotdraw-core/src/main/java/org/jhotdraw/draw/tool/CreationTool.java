/*
 * @(#)CreationTool.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.jhotdraw.util.*;

/**
 * @deprecated
 * A {@link Tool} to create a new figure by drawing its bounds. The figure to be created is
 * specified by a prototype.
 * <p>
 * To create a figure using the {@code CreationTool}, the user does the following mouse gestures on
 * a DrawingView:
 * <ol>
 * <li>Press the mouse button over the DrawingView. This defines the start point of the Figure
 * bounds.</li>
 * <li>Drag the mouse while keeping the mouse button pressed, and then release the mouse button.
 * This defines the end point of the Figure bounds.</li>
 * </ol>
 * The CreationTool works well with most figures that fit into a rectangular shape or that concist
 * of a single straight line. For figures that need additional editing after these mouse gestures,
 * the use of a specialized creation tool is recommended. For example the TextTool allows to enter
 * the text into a TextFigure after the user has performed the mouse gestures.
 * <p>
 * Alltough the mouse gestures might be fitting for the creation of a connection, the CreationTool
 * is not suited for the creation of a ConnectionFigure. Use the ConnectionTool for this type of
 * figures instead.
 * <p>
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Prototype</em><br>
 * The creation tool creates new figures by cloning a prototype figure object. That's the reason why
 * {@code Figure} extends the {@code Cloneable} interface.
 * <br>
 * Prototype: {@link Figure}; Client: {@link CreationTool}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */

@Deprecated()
public class CreationTool extends ExtendedMouseCreationTool implements DragableTool {

    private static final long serialVersionUID = 1L;
    /**
     * A localized name for this tool. The presentationName is displayed by the UndoableEdit.
     */
    protected String presentationName;
    /**
     * Treshold for which we create a larger shape of a minimal size.
     */
    protected Dimension minimalSizeTreshold = new Dimension(2, 2);
    /**
     * We set the figure to this minimal size, if it is smaller than the minimal size treshold.
     */
    protected Dimension minimalSize = new Dimension(40, 40);

    /**
     * Creates a new instance.
     */
    public CreationTool(String prototypeClassName) {
        this(prototypeClassName, null, null);
    }

    public CreationTool(String prototypeClassName, Map<AttributeKey<?>, Object> attributes) {
        this(prototypeClassName, attributes, null);
    }


    public CreationTool(String prototypeClassName, Map<AttributeKey<?>, Object> attributes, String name) {
        super(createInstance(prototypeClassName),attributes, name);
    }

    public CreationTool(Figure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }

    private static Figure createInstance(String className) {
        try {
            return (Figure) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class " + className + " not found");
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to instantiate figure: " + className, e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Class " + className + " is not a figure");
        } catch (NoSuchMethodException e) {
            throw new  IllegalArgumentException("No such constructor: " + className, e);
        } catch (InvocationTargetException e) {

            Throwable cause = e.getCause();

            if (cause instanceof RuntimeException)
                throw (RuntimeException) cause;
            if (cause instanceof Error)
                throw (Error) cause;

            // TODO: Make Tool Exception class
            throw new RuntimeException("Failed to instantiate figure: " + className, e);
        }
    }

    /**
     * Creates a new instance with the specified prototype but without an attribute set. The
     * CreationTool clones this prototype each time a new Figure needs to be created. When a new
     * Figure is created, the CreationTool applies the default attributes from the DrawingEditor to
     * it.
     *
     * @param prototype The prototype used to create a new Figure.
     */
    public CreationTool(Figure prototype) {
        super(prototype, null, null);
    }

    /**
     * Creates a new instance with the specified prototype but without an attribute set. The
     * CreationTool clones this prototype each time a new Figure needs to be created. When a new
     * Figure is created, the CreationTool applies the default attributes from the DrawingEditor to
     * it, and then it applies the attributes to it, that have been supplied in this constructor.
     *
     * @param prototype The prototype used to create a new Figure.
     * @param attributes The CreationTool applies these attributes to the prototype after having
     * applied the default attributes from the DrawingEditor.
     */




    @Override
    public void mouseDragged(MouseEvent evt) {
        if (createdFigure != null) {
            Point2D.Double p = constrainPoint(new Point(evt.getX(), evt.getY()), createdFigure);
            createdFigure.willChange();
            createdFigure.setBounds(
                    constrainPoint(new Point(anchor.x, anchor.y), createdFigure),
                    p);
            createdFigure.changed();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Intentional No-Op: Legacy Code. Legacy parent implemented this, new parent does not.
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        if (createdFigure != null) {
            Rectangle2D.Double bounds = createdFigure.getBounds();
            if (bounds.width == 0 && bounds.height == 0) {
                getDrawing().remove(createdFigure);
                if (isToolDoneAfterCreation()) {
                    fireToolDone();
                }
            } else {
                if (Math.abs(anchor.x - evt.getX()) < minimalSizeTreshold.width
                        && Math.abs(anchor.y - evt.getY()) < minimalSizeTreshold.height) {
                    createdFigure.willChange();
                    createdFigure.setBounds(
                            constrainPoint(new Point(anchor.x, anchor.y), createdFigure),
                            constrainPoint(new Point(
                                    anchor.x + (int) Math.max(bounds.width, minimalSize.width),
                                    anchor.y + (int) Math.max(bounds.height, minimalSize.height)),
                                    createdFigure));
                    createdFigure.changed();
                }
                if (createdFigure instanceof CompositeFigure) {
                    ((CompositeFigure) createdFigure).layout();
                }
                final Figure addedFigure = createdFigure;
                final Drawing addedDrawing = getDrawing();
                getDrawing().fireUndoableEditHappened(new AbstractUndoableEdit() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public String getPresentationName() {
                        return presentationName;
                    }

                    @Override
                    public void undo() throws CannotUndoException {
                        super.undo();
                        addedDrawing.remove(addedFigure);
                    }

                    @Override
                    public void redo() throws CannotRedoException {
                        super.redo();
                        addedDrawing.add(addedFigure);
                    }
                });
                Rectangle r = new Rectangle(anchor.x, anchor.y, 0, 0);
                r.add(evt.getX(), evt.getY());
                maybeFireBoundsInvalidated(r);
                creationFinished(createdFigure);
                createdFigure = null;
            }
        } else {
            if (isToolDoneAfterCreation()) {
                fireToolDone();
            }
        }
    }

}
