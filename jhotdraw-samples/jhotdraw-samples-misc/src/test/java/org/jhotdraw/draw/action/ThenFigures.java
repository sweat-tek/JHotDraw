/*
 * Copyright (c) 2026 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import java.awt.Color;
import java.util.List;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.SVGAttributeKeys;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Then-stage for the Lab 8 BDD scenarios.
 *
 * <p>Reads the post-conditions of the user-visible state. Uses AssertJ's
 * fluent assertions with {@code .as(...)} descriptions so a failed
 * scenario reports the user-meaningful property name, not just the raw
 * Java field.
 */
public class ThenFigures extends Stage<ThenFigures> {

    @ScenarioState
    List<Figure> selectedFigures;

    public ThenFigures each_figure_has_fill_color(Color expected) {
        for (Figure f : selectedFigures) {
            assertThat(f.get(AttributeKeys.FILL_COLOR))
                    .as("FILL_COLOR on a selected figure")
                    .isEqualTo(expected);
        }
        return self();
    }

    public ThenFigures each_figure_has_no_fill_color() {
        for (Figure f : selectedFigures) {
            assertThat(f.get(AttributeKeys.FILL_COLOR))
                    .as("FILL_COLOR on a selected figure (null means \"no fill\")")
                    .isNull();
        }
        return self();
    }

    public ThenFigures each_figure_has_fill_opacity(double expected) {
        for (Figure f : selectedFigures) {
            assertThat(f.get(SVGAttributeKeys.FILL_OPACITY))
                    .as("FILL_OPACITY on a selected figure")
                    .isEqualTo(expected);
        }
        return self();
    }

    public ThenFigures the_selection_remains_empty() {
        assertThat(selectedFigures)
                .as("the selection")
                .isEmpty();
        return self();
    }
}
