/**
 * @(#)SelectionColorChooserHandler.java
 *
 * Copyright (c) 2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.Figure;
import java.awt.*;
import java.util.*;
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
     *
     */
    // TODO [CODE SMELL] Long Method <<< [LEVEL 1 Refactoring]
    @FeatureEntryPoint("SelectionColorChooserHandler.applySelectedColorToFigures")
    protected void applySelectedColorToFigures() {
        // TODO extract to class level?
        final ArrayList<Figure> selectedFigures = new ArrayList<>(getView().getSelectedFigures());
        // TODO extract to class level?
        final ArrayList<Object> restoreData = new ArrayList<>(selectedFigures.size());
        // TODO Guard - move to beginning if there is no color selected, don't proceed with the method
        Color selectedColor = colorChooser.getColor();
        // TODO Maybe simplify boolean expression
        if (selectedColor != null && selectedColor.getAlpha() == 0) {
            selectedColor = null;
        }
        for (Figure figure : selectedFigures) {
            // TODO different abstraction levels (figures, colors, events (?!)) - separate class? [LEVEL 2 REFACTORING]
            restoreData.add(figure.getAttributesRestoreData());
            // TODO Observer pattern?
            figure.willChange();
            figure.set(key, selectedColor);
            figure.changed();
        }
        getEditor().setDefaultAttribute(key, selectedColor);
        // TODO Shouldn't it be redo value?
        final Color undoValue = selectedColor;

        // TODO refactor to be separate class
        // TODO undo mechanism should be handled somewhere else [LEVEL 3 REFACTORING]
        UndoableEdit edit = new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                return AttributeKeys.FONT_FACE.getPresentationName();
            }

            @Override
            public void undo() {
                super.undo();
                Iterator<Object> iRestore = restoreData.iterator();
                for (Figure figure : selectedFigures) {
                    // TODO Code duplication?
                    figure.willChange();
                    figure.restoreAttributesTo(iRestore.next());
                    figure.changed();
                }
            }

            @Override
            public void redo() {
                super.redo();
                for (Figure figure : selectedFigures) {
                    // TODO code duplication?
                    figure.willChange();
                    figure.set(key, undoValue);
                    figure.changed();
                }
            }
        };
        fireUndoableEditHappened(edit);
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
     * @param event  a ChangeEvent object
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
