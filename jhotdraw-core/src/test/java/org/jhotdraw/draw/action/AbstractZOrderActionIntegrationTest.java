package org.jhotdraw.draw.action;

import java.awt.event.ActionEvent;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.figure.Figure;
import static org.jhotdraw.draw.ArrangeTestSupport.addFigure;
import static org.jhotdraw.draw.ArrangeTestSupport.assertOrder;
import static org.jhotdraw.draw.ArrangeTestSupport.captureUndoableEdit;
import static org.jhotdraw.draw.ArrangeTestSupport.createDefaultView;
import static org.jhotdraw.draw.ArrangeTestSupport.select;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AbstractZOrderActionIntegrationTest {

    @Test
    public void sendToBackActionPerformedUsesActiveViewSelection() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView activeView = createDefaultView();
        DefaultDrawingView inactiveView = createDefaultView();
        editor.add(activeView);
        editor.add(inactiveView);

        Figure a = addFigure(activeView, 0);
        Figure b = addFigure(activeView, 10);
        Figure c = addFigure(activeView, 20);
        Figure d = addFigure(activeView, 30);

        Figure x = addFigure(inactiveView, 0);
        Figure y = addFigure(inactiveView, 10);
        Figure z = addFigure(inactiveView, 20);

        select(activeView, c);
        select(inactiveView, y);
        editor.setActiveView(activeView);

        SendToBackAction action = new SendToBackAction(editor);
        action.actionPerformed(actionEvent());

        assertOrder(activeView, c, a, b, d);
        assertOrder(inactiveView, x, y, z);
    }

    @Test
    public void bringToFrontActionPerformedUsesActiveViewSelection() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView activeView = createDefaultView();
        DefaultDrawingView inactiveView = createDefaultView();
        editor.add(activeView);
        editor.add(inactiveView);

        Figure a = addFigure(activeView, 0);
        Figure b = addFigure(activeView, 10);
        Figure c = addFigure(activeView, 20);
        Figure d = addFigure(activeView, 30);

        Figure x = addFigure(inactiveView, 0);
        Figure y = addFigure(inactiveView, 10);
        Figure z = addFigure(inactiveView, 20);

        select(activeView, b);
        select(inactiveView, y);
        editor.setActiveView(activeView);

        BringToFrontAction action = new BringToFrontAction(editor);
        action.actionPerformed(actionEvent());

        assertOrder(activeView, a, c, d, b);
        assertOrder(inactiveView, x, y, z);
    }

    @Test
    public void sendToBackActionPublishesUndoableEditThatRestoresOriginalOrderOnUndo() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView view = createDefaultView();
        editor.add(view);
        editor.setActiveView(view);

        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        select(view, b, d);
        SendToBackAction action = new SendToBackAction(editor);

        UndoableEdit edit = captureUndoableEdit(
                view.getDrawing(),
                () -> action.actionPerformed(actionEvent()));

        assertOrder(view, b, d, a, c);

        edit.undo();

        assertOrder(view, a, b, c, d);
    }

    @Test
    public void bringToFrontActionPublishesUndoableEditThatRestoresOriginalOrderOnUndo() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView view = createDefaultView();
        editor.add(view);
        editor.setActiveView(view);

        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        select(view, a, c);
        BringToFrontAction action = new BringToFrontAction(editor);

        UndoableEdit edit = captureUndoableEdit(
                view.getDrawing(),
                () -> action.actionPerformed(actionEvent()));

        assertOrder(view, b, d, a, c);

        edit.undo();

        assertOrder(view, a, b, c, d);
    }

    @Test
    public void sendToBackUndoableEditReappliesOrderOnRedo() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView view = createDefaultView();
        editor.add(view);
        editor.setActiveView(view);

        Figure a = addFigure(view, 0);
        Figure b = addFigure(view, 10);
        Figure c = addFigure(view, 20);
        Figure d = addFigure(view, 30);

        select(view, b, d);
        SendToBackAction action = new SendToBackAction(editor);

        UndoableEdit edit = captureUndoableEdit(
                view.getDrawing(),
                () -> action.actionPerformed(actionEvent()));

        edit.undo();
        edit.redo();

        assertOrder(view, b, d, a, c);
    }

    @Test
    public void actionIsDisabledWhenSelectionIsEmpty() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView view = createDefaultView();
        editor.add(view);
        editor.setActiveView(view);

        addFigure(view, 0);
        BringToFrontAction action = new BringToFrontAction(editor);

        assertFalse(action.isEnabled());
    }

    @Test
    public void actionBecomesEnabledWhenFiguresAreSelected() {
        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        DefaultDrawingView view = createDefaultView();
        editor.add(view);
        editor.setActiveView(view);

        Figure a = addFigure(view, 0);
        BringToFrontAction action = new BringToFrontAction(editor);

        select(view, a);

        assertTrue(action.isEnabled());
    }

    private ActionEvent actionEvent() {
        return new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "test");
    }
}
