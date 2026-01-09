package org.jhotdraw.undo.BDD;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class UndoRedoScenarioTest extends ScenarioTest<GivenUndoRedo, WhenUndoRedo, ThenUndoRedo> {

    @Test
    public void history_must_be_preserved_in_chronological_order() {
        given().i_have_edited_a_figure();

        when().i_create_a_new_figure();

        then().the_undo_and_redo_history_is_preserved_in_chronological_order();
    }
}