/*
 * @(#)TextCreationTool.java
 *
 * Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.text.*;
import org.jhotdraw.draw.undo.TextEdit;

/**
 * A tool to create figures which implement the {@code TextHolderFigure}
 * interface, such as {@code TextFigure}. The figure to be created is specified
 * by a prototype.
 * <p>
 * To create a figure using this tool, the user does the following mouse
 * gestures on a DrawingView:
 * <ol>
 * <li>Press the mouse button over an area on the DrawingView on which there
 * isn't a text figure present. This defines the location of the figure.</li>
 * </ol>
 * When the user has performed this mouse gesture, the TextCreationTool overlays
 * a text field over the drawing where the user can enter the text for the Figure.
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure}
 * interface define together the contracts of a smaller framework inside of the
 * JHotDraw framework for structured drawing editors.<br>
 * Contract: {@link TextHolderFigure}, {@link TextCreationTool},
 * {@link TextAreaCreationTool}, {@link TextEditingTool},
 * {@link TextAreaEditingTool}, {@link FloatingTextField},
 * {@link FloatingTextArea}.
 *
 * <p>
 * <em>Prototype</em><br>
 * The text creation tools create new figures by cloning a prototype
 * {@code TextHolderFigure} object.<br>
 * Prototype: {@link TextHolderFigure}; Client: {@link TextCreationTool},
 * {@link TextAreaCreationTool}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class TextCreationTool extends SimpleCreationTool implements ActionListener, ClickListeningTool, KeyListeningTool {

    private static final long serialVersionUID = 1L;
    private transient FloatingTextField textField;
    private TextHolderFigure typingTarget;



    /**
     * Creates a new instance.
     */
    public TextCreationTool(TextHolderFigure prototype) {
        super(prototype);
    }

    /**
     * Creates a new instance.
     */
    public TextCreationTool(TextHolderFigure prototype, Map<AttributeKey<?>, Object> attributes) {
        super(prototype, attributes);
    }

    @Override
    public void deactivate(DrawingEditor editor) {
        if (typingTarget != null) {
            endEdit();
        }
        super.deactivate(editor);
    }

    /**
     * If the created figure is a TextHolderFigure it can be edited.
     */

    @Override
    public void mouseClicked(MouseEvent event) {

        if (typingTarget != null) {
            endEdit();
            fireToolDone();
            return;
        }

        super.mouseClicked(event);

        if (createdFigure instanceof TextHolderFigure) {
            beginEdit((TextHolderFigure) createdFigure);
        }
    }


    @Override
    protected void fireToolDone() {
        super.fireToolDone();
    }

    @SuppressWarnings("Duplicates")
    protected void beginEdit(TextHolderFigure textHolder) {
        if (textField == null) {
            textField = new FloatingTextField();
            textField.addActionListener(this);
        }
        if (textHolder != typingTarget && typingTarget != null) {
            endEdit();
        }
        textField.createOverlay(getView(), textHolder);
        textField.requestFocus();
        textField.getEditorComponent().addKeyListener(this);
        typingTarget = textHolder;
    }


    protected void endEdit() {
        if (typingTarget != null) {
            typingTarget.willChange();
            final TextHolderFigure editedFigure = typingTarget;
            final String oldText = typingTarget.getText();
            final String newText = textField.getText();
            if (!newText.isEmpty()) {
                typingTarget.setText(newText);
            } else {
                    getDrawing().remove(getAddedFigure());
                    // XXX - Fire undoable edit here!!
                    typingTarget.willChange();
                    typingTarget.setText("");
                    typingTarget.changed();
            }

            TextEdit.createAndFireEditHappened(getDrawing(), editedFigure, oldText, newText);
            typingTarget.changed();
            typingTarget = null;
            textField.endOverlay();
        }
    }



    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            fireToolDone();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        endEdit();
        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }

    @Override
    protected void creationFinished(Figure createdFigure) {
        beginEdit((TextHolderFigure) createdFigure);
        updateCursor(getView(), new Point(0, 0));
    }

    public boolean isEditing() {
        return typingTarget != null;
    }

    @Override
    public void updateCursor(DrawingView drawingView, Point point) {
        if (drawingView.isEnabled()) {
            drawingView.setCursor(Cursor.getPredefinedCursor(isEditing() ? Cursor.DEFAULT_CURSOR : Cursor.CROSSHAIR_CURSOR));
        } else {
            drawingView.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }
}
