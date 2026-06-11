package org.jhotdraw.draw.action.bdd;

import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.figure.Figure;

final class ArrangeScenarioState {

    DefaultDrawingEditor editor;
    DefaultDrawingView view;
    Figure a;
    Figure b;
    Figure c;
    Figure d;
    Figure e;
    UndoableEdit capturedEdit;
}
