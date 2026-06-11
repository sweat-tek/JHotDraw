package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;
import org.mockito.Mockito;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenGroupingStage extends Stage<ThenGroupingStage> {

    @ScenarioState
    DrawingView view;

    @ScenarioState
    CompositeFigure resultGroup;

    @ScenarioState
    Collection<Figure> resultChildren;

    @ScenarioState
    boolean canGroupResult;

    public ThenGroupingStage the_figures_are_combined_into_a_GroupFigure() {
        assertThat(resultGroup)
                .as("result must be a GroupFigure")
                .isInstanceOf(GroupFigure.class);
        // Verify the group was selected in the view
        Mockito.verify(view).addToSelection(resultGroup);
        return self();
    }

    public ThenGroupingStage the_children_are_restored_to_the_drawing() {
        assertThat(resultChildren)
                .as("ungroupFigures must return the children that were in the group")
                .hasSize(2);
        // Verify children were re-selected in the view
        Mockito.verify(view).addToSelection(resultChildren);
        return self();
    }

    public ThenGroupingStage canGroup_returns_false() {
        assertThat(canGroupResult)
                .as("canGroup should be false when only one figure is selected")
                .isFalse();
        return self();
    }
}
