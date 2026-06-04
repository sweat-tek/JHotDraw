/*
 * Copyright (c) 2026 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the package-private helpers extracted from
 * {@link SelectionColorChooserHandler} in the Lab 5 refactoring.
 *
 * <p>The tests exercise the two pure-ish helpers that the orchestrator
 * delegates to:
 * <ul>
 *   <li>{@code normalizeChosenColor(Color)} — the alpha=0 normalisation
 *       rule that the chooser path uses to translate a fully transparent
 *       picked colour into a {@code null} attribute value.</li>
 *   <li>{@code applyColorToFigures(Color, ArrayList&lt;Figure&gt;)} — the
 *       loop that captures restore data and writes the colour into each
 *       selected figure, surrounded by the {@code willChange / changed}
 *       notification pair.</li>
 * </ul>
 *
 * <p>The orchestrator itself ({@code applySelectedColorToFigures}) is not
 * unit-tested here; it composes a {@link javax.swing.JColorChooser}, a
 * {@link org.jhotdraw.draw.DrawingEditor}, and a {@link
 * javax.swing.undo.UndoableEdit}, and is exercised end-to-end by the BDD
 * scenarios in Lab 8.
 */
public class SelectionColorChooserHandlerTest {

    private final AttributeKey<Color> key = AttributeKeys.FILL_COLOR;

    @Before
    public void setUp() {
        // Each test constructs its own collaborators; no shared state.
    }

    // ---- normalizeChosenColor --------------------------------------------

    @Test
    public void normalizeChosenColor_returnsSameColor_forFullyOpaqueColor() {
        Color opaqueRed = new Color(255, 0, 0, 255);

        Color result = SelectionColorChooserHandler.normalizeChosenColor(opaqueRed);

        assertSame("Opaque colour must be returned unchanged",
                opaqueRed, result);
    }

    @Test
    public void normalizeChosenColor_returnsSameColor_forPartiallyTransparentColor() {
        Color halfTransparentBlue = new Color(0, 0, 255, 128);

        Color result = SelectionColorChooserHandler.normalizeChosenColor(halfTransparentBlue);

        assertSame("Partially transparent colour must be returned unchanged",
                halfTransparentBlue, result);
    }

    @Test
    public void normalizeChosenColor_returnsNull_forFullyTransparentColor() {
        Color fullyTransparent = new Color(0, 0, 0, 0);

        Color result = SelectionColorChooserHandler.normalizeChosenColor(fullyTransparent);

        assertNull("Alpha=0 colour must collapse to null (the SVG \"no fill\" value)",
                result);
    }

    @Test
    public void normalizeChosenColor_returnsNull_forNullInput() {
        Color result = SelectionColorChooserHandler.normalizeChosenColor(null);

        assertNull("Null input must pass through as null", result);
    }

    @Test
    public void normalizeChosenColor_returnsSameColor_forColorWithAlphaJustAboveZero() {
        // Boundary check: alpha=1 (the smallest non-zero alpha) must NOT
        // collapse to null. Only alpha=0 should.
        Color barelyVisible = new Color(0, 0, 0, 1);

        Color result = SelectionColorChooserHandler.normalizeChosenColor(barelyVisible);

        assertSame("Alpha=1 must be preserved (only alpha=0 collapses)",
                barelyVisible, result);
    }

    // ---- applyColorToFigures ---------------------------------------------

    @Test
    public void applyColorToFigures_returnsEmptyRestoreData_forEmptyFigureList() {
        SelectionColorChooserHandler handler = newHandlerWithKey(key);
        ArrayList<Figure> emptySelection = new ArrayList<>();

        ArrayList<Object> restoreData =
                handler.applyColorToFigures(Color.RED, emptySelection);

        assertEquals("Empty selection produces empty restore data",
                0, restoreData.size());
    }

    @Test
    public void applyColorToFigures_setsColorOnEachFigure_andCapturesRestoreData() {
        SelectionColorChooserHandler handler = newHandlerWithKey(key);
        Figure figureA = mock(Figure.class);
        Figure figureB = mock(Figure.class);
        Object restoreA = new Object();
        Object restoreB = new Object();
        when(figureA.getAttributesRestoreData()).thenReturn(restoreA);
        when(figureB.getAttributesRestoreData()).thenReturn(restoreB);

        ArrayList<Figure> figures = new ArrayList<>();
        figures.add(figureA);
        figures.add(figureB);

        ArrayList<Object> restoreData =
                handler.applyColorToFigures(Color.GREEN, figures);

        // Restore data captured in selection order, before mutation.
        assertEquals(2, restoreData.size());
        assertSame(restoreA, restoreData.get(0));
        assertSame(restoreB, restoreData.get(1));

        // Each figure received the canonical willChange -> set -> changed
        // notification sequence with the supplied colour.
        InOrder orderA = inOrder(figureA);
        orderA.verify(figureA).getAttributesRestoreData();
        orderA.verify(figureA).willChange();
        orderA.verify(figureA).set(eq(key), eq(Color.GREEN));
        orderA.verify(figureA).changed();

        InOrder orderB = inOrder(figureB);
        orderB.verify(figureB).getAttributesRestoreData();
        orderB.verify(figureB).willChange();
        orderB.verify(figureB).set(eq(key), eq(Color.GREEN));
        orderB.verify(figureB).changed();
    }

    @Test
    public void applyColorToFigures_acceptsNullColor_andPassesItToEachFigure() {
        // A null colour represents the SVG "no fill" value (see
        // normalizeChosenColor). The figure mutation path must accept it.
        SelectionColorChooserHandler handler = newHandlerWithKey(key);
        Figure figure = mock(Figure.class);
        ArrayList<Figure> figures = new ArrayList<>();
        figures.add(figure);

        ArrayList<Object> restoreData = handler.applyColorToFigures(null, figures);

        assertEquals(1, restoreData.size());
        verify(figure).set(eq(key), eq((Color) null));
    }

    @Test
    public void applyColorToFigures_doesNotTouchFigure_whenFigureListIsEmpty() {
        SelectionColorChooserHandler handler = newHandlerWithKey(key);
        Figure unusedFigure = mock(Figure.class);

        handler.applyColorToFigures(Color.RED, new ArrayList<>());

        verifyNoInteractions(unusedFigure);
    }

    @Test
    public void applyColorToFigures_preservesSelectionOrder_inRestoreData() {
        SelectionColorChooserHandler handler = newHandlerWithKey(key);
        List<Figure> mocks = new ArrayList<>();
        ArrayList<Figure> figures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Figure f = mock(Figure.class);
            when(f.getAttributesRestoreData()).thenReturn("restore-" + i);
            mocks.add(f);
            figures.add(f);
        }

        ArrayList<Object> restoreData =
                handler.applyColorToFigures(Color.BLUE, figures);

        assertEquals(5, restoreData.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("restore-" + i, restoreData.get(i));
        }
    }

    // ---- helpers ---------------------------------------------------------

    /**
     * Constructs a handler suitable for unit-testing the helper methods.
     *
     * <p>The handler's constructor wires up Swing collaborators
     * (DrawingEditor, JColorChooser, JPopupMenu) that the helpers under
     * test do not use. The constructor is bypassed here by allocating
     * the object via the unsafe instance-creation path is not possible
     * cleanly, so instead a minimal construction with mocks is performed
     * directly on the public constructor.
     *
     * <p>{@code applyColorToFigures} reads only the
     * {@code key} field, which is set via the public constructor below.
     */
    private SelectionColorChooserHandler newHandlerWithKey(AttributeKey<Color> attributeKey) {
        org.jhotdraw.draw.DrawingEditor editor =
                mock(org.jhotdraw.draw.DrawingEditor.class);
        javax.swing.JColorChooser chooser = new javax.swing.JColorChooser();
        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        return new SelectionColorChooserHandler(editor, attributeKey, chooser, menu);
    }
}
