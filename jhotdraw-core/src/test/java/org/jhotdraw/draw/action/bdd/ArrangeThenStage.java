package org.jhotdraw.draw.action.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import java.util.Arrays;
import java.util.List;
import org.jhotdraw.draw.figure.Figure;
import static org.assertj.core.api.Assertions.assertThat;

public class ArrangeThenStage extends Stage<ArrangeThenStage> {

    @ExpectedScenarioState
    private ArrangeScenarioState state;

    public ArrangeThenStage the_order_is_b_d_a_c_e() {
        assertThat(currentOrder()).containsExactly(state.b, state.d, state.a, state.c, state.e);
        return self();
    }

    public ArrangeThenStage the_order_is_a_c_e_b_d() {
        assertThat(currentOrder()).containsExactly(state.a, state.c, state.e, state.b, state.d);
        return self();
    }

    public ArrangeThenStage the_order_is_a_b_c_d_e() {
        assertThat(currentOrder()).containsExactly(state.a, state.b, state.c, state.d, state.e);
        return self();
    }

    private List<Figure> currentOrder() {
        return state.view.getDrawing().sort(
                Arrays.asList(state.a, state.b, state.c, state.d, state.e));
    }
}
