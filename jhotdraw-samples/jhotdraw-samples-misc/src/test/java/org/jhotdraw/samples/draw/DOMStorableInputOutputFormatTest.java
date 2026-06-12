/*
 * Copyright (C) 2026 JHotDraw.
 */
package org.jhotdraw.samples.draw;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.RectangleFigure;
import org.jhotdraw.draw.io.DOMStorableInputOutputFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class DOMStorableInputOutputFormatTest {

    private final DOMStorableInputOutputFormat format =
            new DOMStorableInputOutputFormat(new DrawFigureFactory());

    @Test
    public void createsTransferableThatRoundTripsSelectedFigure() throws Exception {
        RectangleFigure original = new RectangleFigure(10, 20, 30, 40);
        DefaultDrawing source = new DefaultDrawing();
        source.add(original);

        Transferable transferable =
                format.createTransferable(source, Collections.<Figure>singletonList(original), 1d);

        DefaultDrawing target = new DefaultDrawing();
        format.read(transferable, target, false);

        assertEquals(1, target.getChildCount());
        Figure pasted = target.getChild(0);
        assertTrue(pasted instanceof RectangleFigure);
        assertNotSame(original, pasted);
        assertBoundsEqual(original.getBounds(), pasted.getBounds());
    }

    @Test
    public void readWithReplaceClearsExistingFigures() throws Exception {
        RectangleFigure copied = new RectangleFigure(1, 2, 3, 4);
        RectangleFigure existing = new RectangleFigure(10, 20, 30, 40);
        DefaultDrawing source = new DefaultDrawing();
        DefaultDrawing target = new DefaultDrawing();
        source.add(copied);
        target.add(existing);

        Transferable transferable =
                format.createTransferable(source, Collections.<Figure>singletonList(copied), 1d);
        format.read(transferable, target, true);

        assertEquals(1, target.getChildCount());
        assertBoundsEqual(copied.getBounds(), target.getChild(0).getBounds());
    }

    @Test
    public void rejectsUnsupportedDataFlavor() {
        assertFalse(format.isDataFlavorSupported(DataFlavor.stringFlavor));
    }

    private static void assertBoundsEqual(Rectangle2D.Double expected, Rectangle2D.Double actual) {
        assertEquals(expected.x, actual.x, 0.0001d);
        assertEquals(expected.y, actual.y, 0.0001d);
        assertEquals(expected.width, actual.width, 0.0001d);
        assertEquals(expected.height, actual.height, 0.0001d);
    }
}
