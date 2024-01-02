package org.jhotdraw.draw.action;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;

import javax.swing.undo.AbstractUndoableEdit;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;

public class SelectionColorChooserHandlerUndoableEdit extends AbstractUndoableEdit {

    private final AttributeKey<Color> key = FILL_COLOR;
    private Color undoValue;
    private List<Figure> selectedFigures;
    private List<Object> restoreData;

    public SelectionColorChooserHandlerUndoableEdit(
            Color undoValue,
            List<Figure> selectedFigures
    ) {
        this.undoValue = undoValue;
        this.selectedFigures = selectedFigures;
        this.restoreData = new ArrayList<>(selectedFigures.size());
        selectedFigures.forEach(figure -> {
            System.out.println(figure.getAttributesRestoreData());
            this.restoreData.add(figure.getAttributesRestoreData());
        });
    }

    @Override
    public String getPresentationName() {
        return AttributeKeys.FONT_FACE.getPresentationName();
    }

    @Override
    public void undo() {
        super.undo();
        Iterator<Object> iRestore = restoreData.iterator();
        for (Figure figure : selectedFigures) {
            figure.willChange();
            figure.restoreAttributesTo(iRestore.next());
            figure.changed();
        }
    }

    @Override
    public void redo() {
        super.redo();
        for (Figure figure : selectedFigures) {
            figure.willChange();
            figure.set(key, undoValue);
            figure.changed();
        }
    }

}
