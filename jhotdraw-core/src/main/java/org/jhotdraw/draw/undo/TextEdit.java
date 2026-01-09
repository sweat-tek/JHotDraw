package org.jhotdraw.draw.undo;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;

public class TextEdit extends AbstractUndoableEdit {

    private TextHolderFigure editedFigure;
    private String oldText;
    private String newText;

    public TextEdit(TextHolderFigure figure, String oldText, String newText) {
        this.editedFigure = figure;
        this.oldText = oldText;
        this.newText = newText;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String getPresentationName() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        return labels.getString("attribute.text.text");
    }

    public static void createAndFireEditHappened(Drawing drawing, TextHolderFigure figure, String oldText, String newText) {
            TextEdit edit = new TextEdit(figure, oldText, newText);
            drawing.fireUndoableEditHappened(edit);
    }

    @Override
    public void undo() {
        super.undo();
        editedFigure.willChange();
        editedFigure.setText(oldText);
        editedFigure.changed();
    }

    @Override
    public void redo() {
        super.redo();
        editedFigure.willChange();
        editedFigure.setText(newText);
        editedFigure.changed();
    }
}
