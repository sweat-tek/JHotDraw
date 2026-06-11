package org.jhotdraw.draw.action.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import java.awt.event.ActionEvent;
import org.jhotdraw.draw.action.BringToFrontAction;
import org.jhotdraw.draw.action.SendToBackAction;
import static org.jhotdraw.draw.ArrangeTestSupport.captureUndoableEdit;

public class ArrangeWhenStage extends Stage<ArrangeWhenStage> {

    @ExpectedScenarioState
    private ArrangeScenarioState state;

    public ArrangeWhenStage the_user_sends_the_selection_to_the_back() {
        SendToBackAction action = new SendToBackAction(state.editor);
        state.capturedEdit = captureUndoableEdit(
                state.view.getDrawing(),
                () -> action.actionPerformed(actionEvent()));
        return self();
    }

    public ArrangeWhenStage the_user_brings_the_selection_to_the_front() {
        BringToFrontAction action = new BringToFrontAction(state.editor);
        state.capturedEdit = captureUndoableEdit(
                state.view.getDrawing(),
                () -> action.actionPerformed(actionEvent()));
        return self();
    }

    public ArrangeWhenStage the_user_undoes_the_arrange_action() {
        state.capturedEdit.undo();
        return self();
    }

    private ActionEvent actionEvent() {
        return new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bdd");
    }
}
