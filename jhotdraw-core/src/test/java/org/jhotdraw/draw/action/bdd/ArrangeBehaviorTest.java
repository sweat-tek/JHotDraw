package org.jhotdraw.draw.action.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class ArrangeBehaviorTest extends ScenarioTest<
        ArrangeGivenStage,
        ArrangeWhenStage,
        ArrangeThenStage> {

    @Test
    public void send_selected_figures_to_back_preserving_relative_order() {
        given().a_default_drawing_with_five_figures()
                .and().the_figures_b_and_d_are_selected();

        when().the_user_sends_the_selection_to_the_back();

        then().the_order_is_b_d_a_c_e();
    }

    @Test
    public void bring_selected_figures_to_front_preserving_relative_order() {
        given().a_default_drawing_with_five_figures()
                .and().the_figures_b_and_d_are_selected();

        when().the_user_brings_the_selection_to_the_front();

        then().the_order_is_a_c_e_b_d();
    }

    @Test
    public void undo_restores_the_original_order_after_send_to_back() {
        given().a_default_drawing_with_five_figures()
                .and().the_figures_b_and_d_are_selected();

        when().the_user_sends_the_selection_to_the_back()
                .and().the_user_undoes_the_arrange_action();

        then().the_order_is_a_b_c_d_e();
    }

    @Test
    public void send_to_back_preserves_relative_order_in_a_quad_tree_drawing() {
        given().a_quad_tree_drawing_with_five_figures()
                .and().the_figures_b_and_d_are_selected();

        when().the_user_sends_the_selection_to_the_back();

        then().the_order_is_b_d_a_c_e();
    }
}
