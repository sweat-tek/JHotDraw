package org.jhotdraw.draw.action;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.GroupFigure;

import java.util.Collection;
import java.util.List;

public class WhenGroupingStage extends Stage<WhenGroupingStage> {

    @ScenarioState
    DrawingView view;

    @ScenarioState
    GroupAction groupAction;

    @ScenarioState
    UngroupAction ungroupAction;

    @ScenarioState
    List<Figure> selectedFigures;

    @ScenarioState
    GroupFigure selectedGroup;

    @ScenarioState
    CompositeFigure resultGroup;

    @ScenarioState
    Collection<Figure> resultChildren;

    @ScenarioState
    boolean canGroupResult;

    public WhenGroupingStage the_group_action_is_triggered() {
        resultGroup = new GroupFigure();
        // drawing stubs were set up in Given; groupFigures calls view.getDrawing() internally
        groupAction.groupFigures(view, resultGroup, selectedFigures);
        return self();
    }

    public WhenGroupingStage the_ungroup_action_is_triggered() {
        // selectedGroup was pre-populated with children in Given
        resultChildren = ungroupAction.ungroupFigures(view, selectedGroup);
        return self();
    }

    public WhenGroupingStage checking_if_grouping_is_allowed() {
        canGroupResult = groupAction.canGroup(view);
        return self();
    }
}
