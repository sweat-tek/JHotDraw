/*
 * Copyright (C) 2026 JHotDraw.
 */
package org.jhotdraw.samples.draw;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.undo.UndoManager;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DefaultDrawingViewTransferHandler;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.io.DOMStorableInputOutputFormat;
import org.jhotdraw.draw.io.TextInputFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CopyPasteBddTest {

    @Test
    public void givenDrawingWithOneSelectedRectangle_whenUserCopiesAndPastesIt_thenDrawingContainsTwoRectanglesAndPastedRectangleIsSelected() {
        ViewFixture fixture = ViewFixture.withDomFormat();
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        fixture.drawing.add(original);
        fixture.view.addToSelection(original);

        Transferable clipboardContent = fixture.handler.copyFrom(fixture.view);
        boolean pasted = fixture.handler.importData(fixture.view, clipboardContent);

        assertTrue(pasted);
        assertEquals(2, fixture.drawing.getChildCount());
        Figure pastedFigure = fixture.drawing.getChild(1);
        assertTrue(pastedFigure instanceof RectangleFigure);
        assertNotSame(original, pastedFigure);
        assertEquals(1, fixture.view.getSelectionCount());
        assertTrue(fixture.view.isFigureSelected(pastedFigure));
        assertFalse(fixture.view.isFigureSelected(original));
    }

    @Test
    public void givenDrawingViewWithNoSelectedFigures_whenUserInvokesCopy_thenNoDrawingFigureTransferableIsProduced() {
        ViewFixture fixture = ViewFixture.withDomFormat();
        fixture.drawing.add(new RectangleFigure(10, 20, 30, 40));

        Transferable clipboardContent = fixture.handler.copyFrom(fixture.view);

        assertNull(clipboardContent);
        assertEquals(1, fixture.drawing.getChildCount());
        assertEquals(0, fixture.view.getSelectionCount());
    }

    @Test
    public void givenClipboardContainsUnsupportedData_whenUserInvokesPaste_thenDrawingRemainsUnchanged() {
        ViewFixture fixture = ViewFixture.withDomFormat();
        fixture.drawing.add(new RectangleFigure(10, 20, 30, 40));

        boolean pasted = fixture.handler.importData(fixture.view, new StringSelection("plain text"));

        assertFalse(pasted);
        assertEquals(1, fixture.drawing.getChildCount());
        assertEquals(0, fixture.view.getSelectionCount());
    }

    @Test
    public void givenUserPastedFiguresIntoDrawing_whenUserInvokesUndoAndRedo_thenPastedFiguresAreRemovedAndAddedBack() {
        ViewFixture fixture = ViewFixture.withDomFormat();
        UndoManager undoManager = new UndoManager();
        fixture.drawing.addUndoableEditListener(undoManager);
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        fixture.drawing.add(original);
        fixture.view.addToSelection(original);
        Transferable clipboardContent = fixture.handler.copyFrom(fixture.view);

        assertTrue(fixture.handler.importData(fixture.view, clipboardContent));
        assertEquals(2, fixture.drawing.getChildCount());

        undoManager.undo();
        assertEquals(1, fixture.drawing.getChildCount());
        assertTrue(fixture.drawing.getChildren().contains(original));

        undoManager.redo();
        assertEquals(2, fixture.drawing.getChildCount());
    }

    @Test
    public void givenClipboardContainsPlainTextAndDrawingRegistersTextInputFormat_whenUserInvokesPaste_thenTextHolderFigureIsAdded() {
        ViewFixture fixture = ViewFixture.withTextInputFormat();

        boolean pasted = fixture.handler.importData(fixture.view, new StringSelection("hello"));

        assertTrue(pasted);
        assertEquals(1, fixture.drawing.getChildCount());
        Figure pastedFigure = fixture.drawing.getChild(0);
        assertTrue(pastedFigure instanceof TextHolderFigure);
        assertEquals("hello", ((TextHolderFigure) pastedFigure).getText());
        assertEquals(1, fixture.view.getSelectionCount());
        assertTrue(fixture.view.isFigureSelected(pastedFigure));
    }

    private static class ViewFixture {

        private final BddTransferHandler handler = new BddTransferHandler();
        private final DefaultDrawing drawing = new DefaultDrawing();
        private final DefaultDrawingView view = new DefaultDrawingView();

        static ViewFixture withDomFormat() {
            ViewFixture fixture = new ViewFixture();
            DOMStorableInputOutputFormat format =
                    new DOMStorableInputOutputFormat(new DrawFigureFactory());
            fixture.drawing.addInputFormat(format);
            fixture.drawing.addOutputFormat(format);
            return fixture;
        }

        static ViewFixture withTextInputFormat() {
            ViewFixture fixture = new ViewFixture();
            fixture.drawing.addInputFormat(new TextInputFormat(new TextFigure()));
            return fixture;
        }

        private ViewFixture() {
            view.setTransferHandler(handler);
            view.setDrawing(drawing);
        }
    }

    private static class BddTransferHandler extends DefaultDrawingViewTransferHandler {

        private static final long serialVersionUID = 1L;

        Transferable copyFrom(DefaultDrawingView view) {
            return super.createTransferable(view, view.getSelectedFigures());
        }
    }
}
