package org.jhotdraw.draw;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import static org.junit.Assert.assertEquals;

public final class ArrangeTestSupport {

    private ArrangeTestSupport() {
    }

    public static DefaultDrawingView createDefaultView() {
        return createView(new DefaultDrawing());
    }

    public static DefaultDrawingView createQuadTreeView() {
        return createView(new QuadTreeDrawing());
    }

    public static DefaultDrawingView createView(Drawing drawing) {
        DefaultDrawingView view = new DefaultDrawingView();
        view.setDrawing(drawing);
        return view;
    }

    public static Figure addFigure(DefaultDrawingView view, double x) {
        RectangleFigure figure = new RectangleFigure();
        figure.setBounds(new Point2D.Double(x, 0), new Point2D.Double(x + 5, 5));
        view.getDrawing().add(figure);
        return figure;
    }

    public static void select(DefaultDrawingView view, Figure... figures) {
        view.clearSelection();
        view.addToSelection(Arrays.asList(figures));
    }

    public static void assertOrder(DefaultDrawingView view, Figure... expected) {
        List<Figure> actual = view.getDrawing().sort(Arrays.asList(expected));
        assertEquals(Arrays.asList(expected), actual);
    }

    public static UndoableEdit captureUndoableEdit(Drawing drawing, Runnable trigger) {
        final UndoableEdit[] captured = new UndoableEdit[1];
        UndoableEditListener listener = new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                captured[0] = e.getEdit();
            }
        };
        drawing.addUndoableEditListener(listener);
        try {
            trigger.run();
        } finally {
            drawing.removeUndoableEditListener(listener);
        }
        if (captured[0] == null) {
            throw new AssertionError("Expected an undoable edit to be published.");
        }
        return captured[0];
    }
}
