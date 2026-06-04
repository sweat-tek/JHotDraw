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

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

/**
 * Given-stage for the Lab 8 BDD scenarios on the fill-colour / opacity
 * feature.
 *
 * <p>State established here (the current selection and any pre-existing
 * attribute values) flows into the When-stage and is read by the
 * Then-stage via the shared {@link ScenarioState} fields.
 */
public class GivenSelection extends Stage<GivenSelection> {

    @ScenarioState
    List<Figure> selectedFigures = new ArrayList<>();

    public GivenSelection a_single_rectangle_is_selected() {
        selectedFigures.clear();
        selectedFigures.add(new SVGRectFigure(0, 0, 100, 50));
        return self();
    }

    public GivenSelection $_rectangles_are_selected(int count) {
        selectedFigures.clear();
        for (int i = 0; i < count; i++) {
            selectedFigures.add(new SVGRectFigure(i * 10, 0, 50, 30));
        }
        return self();
    }

    public GivenSelection the_selected_rectangle_has_fill_color(Color color) {
        Figure f = selectedFigures.get(0);
        f.willChange();
        f.set(AttributeKeys.FILL_COLOR, color);
        f.changed();
        return self();
    }

    public GivenSelection the_selected_rectangle_has_fill_opacity(double opacity) {
        Figure f = selectedFigures.get(0);
        f.willChange();
        f.set(SVGAttributeKeys.FILL_OPACITY, opacity);
        f.changed();
        return self();
    }

    public GivenSelection no_figures_are_selected() {
        selectedFigures.clear();
        return self();
    }
}
