package org.jhotdraw.samples.svg;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jhotdraw.draw.AttributeKey;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.event.CompositeFigureEvent;
import org.jhotdraw.draw.event.CompositeFigureListener;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.ImageFigure;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.junit.Ignore;
import org.junit.Test;

public class SVGCreateFromFileToolTest {


    /**
     * Method under test: {@link SVGCreateFromFileTool#SVGCreateFromFileTool(ImageHolderFigure, CompositeFigure)}
     */
    @Test
    public void testConstructor() {
        ImageFigure imagePrototype = new ImageFigure();
        SVGCreateFromFileTool actualSvgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing());

        ImageHolderFigure imageHolderFigure = actualSvgCreateFromFileTool.imagePrototype;
        assertTrue(imageHolderFigure instanceof ImageFigure);
        assertTrue(actualSvgCreateFromFileTool.groupPrototype instanceof DefaultDrawing);
        assertSame(imageHolderFigure, actualSvgCreateFromFileTool.getPrototype());
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#SVGCreateFromFileTool(ImageHolderFigure, CompositeFigure, Map)}
     */
    @Test
    public void testConstructor2() {
        ImageFigure imagePrototype = new ImageFigure();
        DefaultDrawing groupPrototype = new DefaultDrawing();
        SVGCreateFromFileTool actualSvgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, groupPrototype,
                new HashMap<>());

        ImageHolderFigure imageHolderFigure = actualSvgCreateFromFileTool.imagePrototype;
        assertTrue(imageHolderFigure instanceof ImageFigure);
        assertTrue(actualSvgCreateFromFileTool.groupPrototype instanceof DefaultDrawing);
        assertSame(imageHolderFigure, actualSvgCreateFromFileTool.getPrototype());
        assertTrue(actualSvgCreateFromFileTool.isToolDoneAfterCreation());
    }

    /**
     * Method under test: {@link SVGCreateFromFileTool#activate(DrawingEditor)}
     */
    @Test
    public void testActivate() {
        ImageFigure imagePrototype = new ImageFigure();
        SVGCreateFromFileTool svgCreateFromFileTool = new SVGCreateFromFileTool(imagePrototype, new DefaultDrawing());
        svgCreateFromFileTool.activate(new DefaultDrawingEditor());
        assertTrue(svgCreateFromFileTool.isActive());
    }
}

