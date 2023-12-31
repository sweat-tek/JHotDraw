/**
 * @(#)SelectionColorChooserHandler.java Copyright (c) 2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.Figure;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.*;

import org.jhotdraw.draw.*;

public class SelectionColorChooserHandler extends AbstractSelectedAction
        implements ChangeListener {

    private static final long serialVersionUID = 1L;
    protected AttributeKey<Color> key;
    protected JColorChooser colorChooser;
    protected JPopupMenu popupMenu; // TODO should not care about popupMenu MVC, etc.
    protected int isUpdating; // TODO Boolean?

    public SelectionColorChooserHandler(DrawingEditor editor, AttributeKey<Color> key, JColorChooser colorChooser, JPopupMenu popupMenu) {
        super(editor);
        this.key = key;
        this.colorChooser = colorChooser;
        this.popupMenu = popupMenu;
        //colorChooser.addActionListener(this);
        colorChooser.getSelectionModel().addChangeListener(this); // TODO is anything achieved with using the change listener?
        updateEnabledState();
    }

    @Override
    // TODO 2 entry points?! actionPerformed() and stateChanged()?
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // TODO class is about handling and applying colors, but should not care about menus?! [LEVEL 2 Refactoring]
        popupMenu.setVisible(false);
    }

    /**
     * Gets the currently selected figures and the currently selected color,
     * and applies the color to all selected figures.
     */
    @FeatureEntryPoint("SelectionColorChooserHandler.applySelectedColorToFigures")
    protected void applySelectedColorToFigures() {
        final Color selectedColor = getSelectedColor();
        setSelectedColorInEditor(selectedColor);
        final List<Figure> selectedFigures = getSelectedFigures();
        enableUndoOfAppliedColor(selectedColor, selectedFigures);
        setColorForFigures(selectedColor, selectedFigures);
    }

    private Color getSelectedColor() {
        Color selectedColor = colorChooser.getColor();
        if (selectedColor != null && selectedColor.getAlpha() == 0) {
            selectedColor = null;
        }
        return selectedColor;
    }

    private void setSelectedColorInEditor(Color selectedColor) {
        getEditor().setDefaultAttribute(key, selectedColor);
    }

    private List<Figure> getSelectedFigures() {
        return new ArrayList<Figure>(getView().getSelectedFigures());
    }

    private void enableUndoOfAppliedColor(Color undoColor, List<Figure> figures) {
        final UndoableEdit edit = new SelectionColorChooserHandlerUndoableEdit(undoColor, figures);
        fireUndoableEditHappened(edit);
    }

    private void setColorForFigures(Color color, List<Figure> figures) {
        figures.forEach(figure -> {
            figure.willChange();
            figure.set(key, color);
            figure.changed();
        });
    }

    @Override
    protected void updateEnabledState() {
        setEnabled(getEditor().isEnabled());
        if (getView() != null && colorChooser != null && popupMenu != null) {
            colorChooser.setEnabled(getView().getSelectionCount() > 0);
            popupMenu.setEnabled(getView().getSelectionCount() > 0);
            isUpdating++;
            if (getView().getSelectionCount() > 0 /*&& colorChooser.isShowing()*/) {
                // TODO Solarlint: Loops with at most one iteration should be refactored [LEVEL 2 REFACTORING]
                for (Figure f : getView().getSelectedFigures()) {
                    Color figureColor = f.get(key);
                    colorChooser.setColor(figureColor == null ? new Color(0, true) : figureColor);
                    break;
                }
            }
            isUpdating--;
        }
    }

    /**
     * If the state of the application changes, it is checked whether the class is currently updating.
     * If it is not updating {@code applySelectedColorToFigures} is called to apply the currently
     * selected color to the currently selected figures. After applying the colors, the class is set to
     * not updating again.
     *
     * @param event a ChangeEvent object
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO simplify conditional expression // make isUpdating a boolean
        if (isUpdating++ == 0) {
            applySelectedColorToFigures();
        }
        isUpdating--;
    }
}
