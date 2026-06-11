package org.jhotdraw.draw.action;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class GroupingBDDTest extends ScenarioTest<GivenGroupingStage, WhenGroupingStage, ThenGroupingStage> {

    @Test
    public void a_designer_can_group_multiple_figures() {
        given().two_figures_are_selected();
        when().the_group_action_is_triggered();
        then().the_figures_are_combined_into_a_GroupFigure();
    }

    @Test
    public void a_designer_can_ungroup_a_grouped_figure() {
        given().one_GroupFigure_is_selected();
        when().the_ungroup_action_is_triggered();
        then().the_children_are_restored_to_the_drawing();
    }

    @Test
    public void group_action_is_disabled_for_a_single_figure() {
        given().one_plain_figure_is_selected();
        when().checking_if_grouping_is_allowed();
        then().canGroup_returns_false();
    }
}
