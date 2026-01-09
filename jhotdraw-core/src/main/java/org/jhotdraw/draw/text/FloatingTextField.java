/*
 * @(#)FloatingTextField.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.text;

import org.jhotdraw.draw.figure.TextHolderFigure;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import org.jhotdraw.draw.*;
import static org.jhotdraw.draw.AttributeKeys.*;
import org.jhotdraw.draw.event.FigureAdapter;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;

/**
 * A <em>floating text field</em> that is used to edit a {@link TextHolderFigure}.
 * <p>
 * {@code FloatingTextField} requires a two step initialization:
 * In a first step the overlay is created and in a
 * second step it can be positioned.
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p>
 * <em>Framework</em><br>
 * The text creation and editing tools and the {@code TextHolderFigure}
 * interface define together the contracts of a smaller framework inside of the
 * JHotDraw framework for structured drawing editors.<br>
 * Contract: {@link TextHolderFigure}, {@link org.jhotdraw.draw.tool.TextCreationTool},
 * {@link org.jhotdraw.draw.tool.TextAreaCreationTool},
 * {@link org.jhotdraw.draw.tool.TextEditingTool},
 * {@link org.jhotdraw.draw.tool.TextAreaEditingTool}, {@link FloatingTextField},
 * {@link FloatingTextArea}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id: FloatingTextField.java -1 $
 */
public class FloatingTextField extends AbstractEditableFloatingText {

    private TextHolderFigure editedFigure;
    private JTextField textField;
    private DrawingView view;
    private FigureListener figureHandler = new FigureAdapter() {
        @Override
        public void attributeChanged(FigureEvent e) {
            updateWidget();
        }
    };

    public FloatingTextField() {
        textField = new JTextField(20);
    }

    public void requestFocus() {
        textField.requestFocus();
    }

    @Override
    public JComponent getEditorComponent() {
        return textField;
    }

    @Override
    protected DrawingView getDrawingView() {
        return view;
    }

    /**
     * Creates the overlay for the given Container using a
     * specific font.
     */
    public void createOverlay(DrawingView view, TextHolderFigure figure) {
        view.getComponent().add(textField, 0);
        textField.setText(figure.getText());
        textField.setColumns(figure.getTextColumns());
        textField.selectAll();
        textField.setVisible(true);
        editedFigure = figure;
        editedFigure.addFigureListener(figureHandler);
        this.view = view;
        updateWidget();
    }


    protected void updateWidget() {
        if (editedFigure == null) return;
        Font fontOnFigure = editedFigure.getFont();
        fontOnFigure = fontOnFigure.deriveFont(fontOnFigure.getStyle(), (float) (editedFigure.getFontSize() * view.getScaleFactor()));
        textField.setFont(fontOnFigure);
        textField.setForeground(editedFigure.getTextColor());
        textField.setBackground(editedFigure.getFillColor());
        Rectangle2D.Double fieldDrawBounds = editedFigure.getBounds();
        Point2D.Double fieldDrawLocation = new Point2D.Double(fieldDrawBounds.getX(), fieldDrawBounds.getY());
        if (editedFigure.get(TRANSFORM) != null) {
            editedFigure.get(TRANSFORM).transform(fieldDrawLocation, fieldDrawLocation);
        }
        Point fieldViewLocation = view.drawingToView(fieldDrawLocation);
        Rectangle fieldViewBounds = view.drawingToView(fieldDrawBounds);
        fieldViewBounds.x = fieldViewLocation.x;
        fieldViewBounds.y = fieldViewLocation.y;
        Dimension textFieldDimensions = textField.getPreferredSize();
        Insets textFieldInsets = textField.getInsets();
        if (textField.getGraphics() == null) return;
        float fontBaseline = textField.getGraphics().getFontMetrics(fontOnFigure).getMaxAscent();
        double fieldBaseline = editedFigure.getBaseline() * view.getScaleFactor();
        textField.setBounds(
                fieldViewBounds.x - textFieldInsets.left,
                fieldViewBounds.y - textFieldInsets.top - (int) (fontBaseline - fieldBaseline),
                Math.max(fieldViewBounds.width + textFieldInsets.left + textFieldInsets.right, textFieldDimensions.width),
                Math.max(fieldViewBounds.height + textFieldInsets.top + textFieldInsets.bottom, textFieldDimensions.height)
        );
    }

    public Insets getInsets() {
        return textField.getInsets();
    }

    /**
     * Adds an action listener
     */
    public void addActionListener(ActionListener listener) {
        textField.addActionListener(listener);
    }

    /**
     * Remove an action listener
     */
    public void removeActionListener(ActionListener listener) {
        textField.removeActionListener(listener);
    }

    /**
     * Gets the text contents of the overlay.
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Gets the preferred size of the overlay.
     */
    public Dimension getPreferredSize(int cols) {
        textField.setColumns(cols);
        return textField.getPreferredSize();
    }


}
