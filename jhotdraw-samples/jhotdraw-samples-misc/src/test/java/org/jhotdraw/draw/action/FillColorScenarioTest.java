/*
 * Copyright (c) 2026 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import java.awt.Color;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

/**
 * Lab 8 BDD scenarios for the fill-colour / opacity user story.
 *
 * <p>User story (from Lab 2):
 * <blockquote>
 * As a JHotDraw user creating SVG drawings, I want to fill a selected
 * figure with a chosen colour and adjust its opacity, so that I can
 * visually emphasise or de-emphasise elements in my drawing with
 * fine-grained control.
 * </blockquote>
 *
 * <p>Each scenario covers one acceptance criterion from the change
 * request. The scenarios are written in the JGiven Given/When/Then
 * style and report under the test class name to JGiven's HTML report
 * after a successful build.
 */
public class FillColorScenarioTest
        extends ScenarioTest<GivenSelection, WhenChoosing, ThenFigures> {

    @Test
    public void a_single_selected_rectangle_receives_the_chosen_fill_color() {
        given().a_single_rectangle_is_selected();
        when().the_user_chooses_fill_color(Color.RED);
        then().each_figure_has_fill_color(Color.RED);
    }

    @Test
    public void all_rectangles_in_a_multi_selection_receive_the_chosen_fill_color() {
        given().$_rectangles_are_selected(3);
        when().the_user_chooses_fill_color(Color.BLUE);
        then().each_figure_has_fill_color(Color.BLUE);
    }

    @Test
    public void a_fully_transparent_pick_clears_the_fill_color() {
        given().a_single_rectangle_is_selected()
               .and().the_selected_rectangle_has_fill_color(Color.RED);
        when().the_user_picks_a_fully_transparent_color();
        then().each_figure_has_no_fill_color();
    }

    @Test
    public void adjusting_fill_opacity_updates_the_selected_figure() {
        given().a_single_rectangle_is_selected();
        when().the_user_adjusts_fill_opacity_to(0.5);
        then().each_figure_has_fill_opacity(0.5);
    }

    @Test
    public void adjusting_fill_opacity_to_the_zero_boundary_is_accepted() {
        given().a_single_rectangle_is_selected()
               .and().the_selected_rectangle_has_fill_opacity(1.0);
        when().the_user_adjusts_fill_opacity_to(0.0);
        then().each_figure_has_fill_opacity(0.0);
    }

    @Test
    public void choosing_a_color_with_no_selection_changes_nothing() {
        given().no_figures_are_selected();
        when().the_user_chooses_fill_color(Color.GREEN);
        then().the_selection_remains_empty();
    }
}
