/*
 * Copyright (C) 2026 JHotDraw.
 */
package org.jhotdraw.samples.draw;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.undo.UndoManager;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DefaultDrawingViewTransferHandler;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.jhotdraw.draw.io.DOMStorableInputOutputFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DefaultDrawingViewTransferHandlerTest {

    @Test
    public void copySelectedFigureCreatesTransferable() {
        ViewFixture fixture = new ViewFixture();
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        fixture.drawing.add(original);
        fixture.view.addToSelection(original);

        Transferable transferable = fixture.handler.createTransferable(
                fixture.view,
                fixture.view.getSelectedFigures());

        assertNotNull(transferable);
        assertTrue(transferable.isDataFlavorSupported(fixture.format.getDataFlavor()));
    }

    @Test
    public void copyWithoutSelectionCreatesNoTransferable() {
        ViewFixture fixture = new ViewFixture();
        fixture.drawing.add(new RectangleFigure(10, 20, 30, 40));

        Transferable transferable = fixture.handler.createTransferable(
                fixture.view,
                fixture.view.getSelectedFigures());

        assertEquals(1, fixture.drawing.getChildCount());
        assertFalse(fixture.view.getSelectionCount() > 0);
        assertNull(transferable);
    }

    @Test
    public void pasteSupportedTransferableAddsAndSelectsImportedFigure() {
        ViewFixture source = new ViewFixture();
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        source.drawing.add(original);
        source.view.addToSelection(original);
        Transferable transferable = source.handler.createTransferable(
                source.view,
                source.view.getSelectedFigures());

        ViewFixture target = new ViewFixture();
        boolean imported = target.handler.importData(target.view, transferable);

        assertTrue(imported);
        assertEquals(1, target.drawing.getChildCount());
        Figure pasted = target.drawing.getChild(0);
        assertTrue(pasted instanceof RectangleFigure);
        assertNotSame(original, pasted);
        assertBoundsEqual(original.getBounds(), pasted.getBounds());
        assertEquals(1, target.view.getSelectionCount());
        assertTrue(target.view.isFigureSelected(pasted));
    }

    @Test
    public void pasteUnsupportedTransferableLeavesDrawingUnchanged() {
        ViewFixture fixture = new ViewFixture();
        fixture.drawing.add(new RectangleFigure(10, 20, 30, 40));

        boolean imported = fixture.handler.importData(fixture.view, new StringSelection("not a drawing"));

        assertFalse(imported);
        assertEquals(1, fixture.drawing.getChildCount());
        assertEquals(0, fixture.view.getSelectionCount());
    }

    @Test
    public void pasteFiresUndoableEditThatCanUndoAndRedoImportedFigure() {
        ViewFixture source = new ViewFixture();
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        source.drawing.add(original);
        source.view.addToSelection(original);
        Transferable transferable = source.handler.createTransferable(
                source.view,
                source.view.getSelectedFigures());

        ViewFixture target = new ViewFixture();
        UndoManager undoManager = new UndoManager();
        target.drawing.addUndoableEditListener(undoManager);

        assertTrue(target.handler.importData(target.view, transferable));
        assertEquals(1, target.drawing.getChildCount());
        assertTrue(undoManager.canUndo());

        undoManager.undo();
        assertEquals(0, target.drawing.getChildCount());

        assertTrue(undoManager.canRedo());
        undoManager.redo();
        assertEquals(1, target.drawing.getChildCount());
    }

    private static void assertBoundsEqual(Rectangle2D.Double expected, Rectangle2D.Double actual) {
        assertEquals(expected.x, actual.x, 0.0001d);
        assertEquals(expected.y, actual.y, 0.0001d);
        assertEquals(expected.width, actual.width, 0.0001d);
        assertEquals(expected.height, actual.height, 0.0001d);
    }

    private static class ViewFixture {

        private final ExposedTransferHandler handler = new ExposedTransferHandler();
        private final DOMFormatWithFlavor format = new DOMFormatWithFlavor();
        private final DefaultDrawing drawing = new DefaultDrawing();
        private final DefaultDrawingView view = new DefaultDrawingView();

        ViewFixture() {
            drawing.addInputFormat(format);
            drawing.addOutputFormat(format);
            view.setTransferHandler(handler);
            view.setDrawing(drawing);
        }
    }

    private static class ExposedTransferHandler extends DefaultDrawingViewTransferHandler {

        private static final long serialVersionUID = 1L;

        public Transferable createTransferable(DrawingView view, Set<Figure> transferFigures) {
            return super.createTransferable(view, transferFigures);
        }

        @Override
        public boolean importData(JComponent comp, Transferable t) {
            return super.importData(comp, t);
        }
    }

    private static class DOMFormatWithFlavor extends DOMStorableInputOutputFormat {

        DOMFormatWithFlavor() {
            super(new DrawFigureFactory());
        }

        DataFlavor getDataFlavor() {
            Transferable transferable;
            try {
                DefaultDrawing drawing = new DefaultDrawing();
                RectangleFigure figure = new RectangleFigure(0, 0, 1, 1);
                drawing.add(figure);
                transferable = createTransferable(drawing, java.util.Collections.<Figure>singletonList(figure), 1d);
            } catch (java.io.IOException ex) {
                throw new AssertionError(ex);
            }
            return transferable.getTransferDataFlavors()[0];
        }
    }
}
