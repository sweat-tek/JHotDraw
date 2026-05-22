package org.jhotdraw.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import javax.swing.undo.AbstractUndoableEdit;

import static org.assertj.core.api.Assertions.assertThat;


@As("Undo/Redo Functionality")
public class UndoRedoManagerBDDTest
        extends ScenarioTest<UndoRedoManagerBDDTest.GivenStage,
                             UndoRedoManagerBDDTest.WhenStage,
                             UndoRedoManagerBDDTest.ThenStage> {

    
    static class DrawingEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
    }

    // Scenario 1 — Acceptance Criterion 1
    // The user can undo the latest action.
    @Test
    @As("User can undo the latest drawing action")
    public void user_can_undo_the_latest_action() {
        given().a_fresh_undo_manager()
               .and().the_user_has_performed_a_drawing_action();

        when().the_user_triggers_undo();

        then().undo_is_no_longer_available()
              .and().redo_becomes_available();
    }

    
    // Scenario 2 — Acceptance Criterion 2
    // The user can redo an undone action.
    
    @Test
    @As("User can redo a previously undone action")
    public void user_can_redo_an_undone_action() {
        given().a_fresh_undo_manager()
               .and().the_user_has_performed_a_drawing_action();

        when().the_user_triggers_undo()
              .and().the_user_triggers_redo();

        then().undo_is_available_again()
              .and().redo_is_no_longer_available();
    }

    
    // Scenario 3 — Acceptance Criterion 3
    // Multiple undo/redo operations are supported.
    
    @Test
    @As("User can undo and redo multiple drawing actions")
    public void user_can_undo_multiple_actions() {
        given().a_fresh_undo_manager()
               .and().the_user_has_performed_$_drawing_actions(3);

        when().the_user_triggers_undo()
              .and().the_user_triggers_undo();

        then().undo_is_still_available_for_remaining_edits()
              .and().redo_becomes_available();
    }

    
    // Scenario 4 — Acceptance Criterion 4
    // State is correctly reset when edits are discarded (e.g. new file).
    
    @Test
    @As("Manager resets correctly when all edits are discarded")
    public void manager_resets_state_after_discard() {
        given().a_fresh_undo_manager()
               .and().the_user_has_performed_a_drawing_action();

        when().all_edits_are_discarded();

        then().neither_undo_nor_redo_is_available();
    }

    
    // STAGES
    

    public static class GivenStage extends Stage<GivenStage> {

        @ProvidedScenarioState
        UndoRedoManager manager;

        public GivenStage a_fresh_undo_manager() {
            manager = new UndoRedoManager();
            return self();
        }

        public GivenStage the_user_has_performed_a_drawing_action() {
            manager.addEdit(new DrawingEdit());
            return self();
        }

        public GivenStage the_user_has_performed_$_drawing_actions(int count) {
            for (int i = 0; i < count; i++) {
                manager.addEdit(new DrawingEdit());
            }
            return self();
        }
    }

    public static class WhenStage extends Stage<WhenStage> {

        @ExpectedScenarioState
        UndoRedoManager manager;

        public WhenStage the_user_triggers_undo() {
            manager.undo();
            return self();
        }

        public WhenStage the_user_triggers_redo() {
            manager.redo();
            return self();
        }

        public WhenStage all_edits_are_discarded() {
            manager.discardAllEdits();
            return self();
        }
    }

    public static class ThenStage extends Stage<ThenStage> {

        @ExpectedScenarioState
        UndoRedoManager manager;

        public ThenStage undo_is_no_longer_available() {
            assertThat(manager.canUndo())
                    .as("Undo should not be available after undoing the only edit")
                    .isFalse();
            return self();
        }

        public ThenStage redo_becomes_available() {
            assertThat(manager.canRedo())
                    .as("Redo should be available after an undo")
                    .isTrue();
            return self();
        }

        public ThenStage undo_is_available_again() {
            assertThat(manager.canUndo())
                    .as("Undo should be available again after redo")
                    .isTrue();
            return self();
        }

        public ThenStage redo_is_no_longer_available() {
            assertThat(manager.canRedo())
                    .as("Redo should not be available after redoing")
                    .isFalse();
            return self();
        }

        public ThenStage undo_is_still_available_for_remaining_edits() {
            assertThat(manager.canUndo())
                    .as("Undo should still be available (1 edit remains)")
                    .isTrue();
            return self();
        }

        public ThenStage neither_undo_nor_redo_is_available() {
            assertThat(manager.canUndo())
                    .as("Undo must be disabled after discard")
                    .isFalse();
            assertThat(manager.canRedo())
                    .as("Redo must be disabled after discard")
                    .isFalse();
            return self();
        }
    }
}