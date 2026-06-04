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

/**
 * When-stage for the Lab 8 BDD scenarios.
 *
 * <p>The colour-choice path uses
 * {@link SelectionColorChooserHandler#normalizeChosenColor(Color)} as
 * the production code does (alpha=0 collapses to null), then writes the
 * normalised colour into every selected figure with the same
 * willChange / set / changed sequence the handler uses internally.
 *
 * <p>The opacity-adjustment path corresponds to the slider widget in the
 * Fill toolbar, which is wired through
 * {@code FigureAttributeEditorHandler} in production. The When-stage
 * applies {@code FILL_OPACITY} directly to each selected figure, since
 * the slider path's behaviour is already covered by the unit-level
 * coverage of the attribute system and the handler's
 * {@code updateFigures()} method.
 */
public class WhenChoosing extends Stage<WhenChoosing> {

    @ScenarioState
    List<Figure> selectedFigures;

    public WhenChoosing the_user_chooses_fill_color(Color color) {
        Color normalized =
                SelectionColorChooserHandler.normalizeChosenColor(color);
        for (Figure f : selectedFigures) {
            f.willChange();
            f.set(AttributeKeys.FILL_COLOR, normalized);
            f.changed();
        }
        return self();
    }

    public WhenChoosing the_user_picks_a_fully_transparent_color() {
        return the_user_chooses_fill_color(new Color(0, 0, 0, 0));
    }

    public WhenChoosing the_user_adjusts_fill_opacity_to(double opacity) {
        for (Figure f : selectedFigures) {
            f.willChange();
            f.set(SVGAttributeKeys.FILL_OPACITY, opacity);
            f.changed();
        }
        return self();
    }
}
