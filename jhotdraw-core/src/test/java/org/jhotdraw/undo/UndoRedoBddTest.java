package org.jhotdraw.undo;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Behavior-Driven tests for Undo/Redo — TestLab2.
 *
 * User story: "As a drawing application user, I want to undo and redo
 * drawing actions so that I can correct mistakes and restore reverted changes."
 *
 * Each test method maps one Acceptance Criterion to a Given–When–Then scenario
 * automated with JGiven and verified with AssertJ.
 */
public class UndoRedoBddTest extends ScenarioTest<
        UndoRedoBddTest.GivenUndoManager,
        UndoRedoBddTest.WhenUndoRedo,
        UndoRedoBddTest.ThenUndoRedo> {

    // =========================================================================
    // AC1 — Undo Action Execution
    // =========================================================================

    @Test
    public void ac1_undo_reverts_the_most_recent_drawing_action() {
        given().a_drawing_action_named_$_has_been_performed("Create Rectangle");
        when().the_user_triggers_undo();
        then().the_edit_is_reverted()
              .and().redo_is_available();
    }

    // =========================================================================
    // AC2 — Redo Action Execution
    // =========================================================================

    @Test
    public void ac2_redo_reapplies_the_most_recently_undone_action() {
        given().a_drawing_action_named_$_has_been_performed("Move Shape")
               .and().the_action_has_been_undone();
        when().the_user_triggers_redo();
        then().the_edit_is_reapplied()
              .and().undo_is_available();
    }

    // =========================================================================
    // AC3 — Undo/Redo History Stack (configurable limit)
    // =========================================================================

    @Test
    public void ac3_oldest_edit_is_discarded_when_history_limit_is_reached() {
        given().a_fresh_manager_with_limit_$(2)
               .and().$_drawing_actions_have_been_performed(3);
        when().the_user_undoes_all_available_edits();
        then().exactly_$_edits_were_undone(2);
    }

    // =========================================================================
    // AC4a — Visual Feedback: Undo button disabled when stack is empty
    // =========================================================================

    @Test
    public void ac4_undo_action_is_disabled_when_there_is_nothing_to_undo() {
        given().a_fresh_undo_manager();
        when().the_user_observes_the_toolbar();
        then().the_undo_action_is_disabled();
    }

    // =========================================================================
    // AC4b — Visual Feedback: Undo button enabled with tooltip after an edit
    // =========================================================================

    @Test
    public void ac4_undo_action_is_enabled_and_shows_action_name_after_edit() {
        given().a_drawing_action_named_$_has_been_performed("Create Rectangle");
        when().the_user_observes_the_toolbar();
        then().the_undo_action_is_enabled()
              .and().the_undo_action_tooltip_contains("Create Rectangle");
    }

    // =========================================================================
    // AC4c — Visual Feedback: Redo button disabled when nothing has been undone
    // =========================================================================

    @Test
    public void ac4_redo_action_is_disabled_when_nothing_has_been_undone() {
        given().a_fresh_undo_manager();
        when().the_user_observes_the_toolbar();
        then().the_redo_action_is_disabled();
    }

    // =========================================================================
    // AC4d — Visual Feedback: Redo button enabled after an undo
    // =========================================================================

    @Test
    public void ac4_redo_action_is_enabled_after_an_undo() {
        given().a_drawing_action_named_$_has_been_performed("Create Rectangle")
               .and().the_action_has_been_undone();
        when().the_user_observes_the_toolbar();
        then().the_redo_action_is_enabled();
    }

    // =========================================================================
    // AC5 — Action Grouping (CompositeEdit)
    // =========================================================================

    @Test
    public void ac5_sequential_similar_actions_grouped_into_one_undo_step() {
        given().a_composite_of_$_sub_edits_named_$(3, "Move Shape");
        when().the_user_triggers_undo();
        then().all_$_grouped_edits_are_reverted(3)
              .and().undo_is_not_available();
    }

    // =========================================================================
    // AC6 — Redo Stack Clearing on New Action
    // =========================================================================

    @Test
    public void ac6_redo_stack_is_cleared_when_new_action_is_performed_after_undo() {
        given().a_drawing_action_named_$_has_been_performed("Create Shape A")
               .and().the_action_has_been_undone();
        when().a_new_action_named_$_is_performed("Create Shape B");
        then().redo_is_not_available();
    }

    // =========================================================================
    // AC7 — Undo/Redo with Multiple Objects
    // =========================================================================

    @Test
    public void ac7_undo_atomically_reverts_changes_to_multiple_selected_objects() {
        given().a_group_edit_modifying_$_objects(3);
        when().the_user_triggers_undo();
        then().all_$_grouped_edits_are_reverted(3);
    }

    // =========================================================================
    // AC8 — Persistent State After Save
    // =========================================================================

    @Test
    public void ac8_undo_redo_history_is_cleared_after_saving() {
        given().a_fresh_undo_manager()
               .and().$_drawing_actions_have_been_performed(3);
        when().the_drawing_is_saved();
        then().undo_is_not_available()
              .and().redo_is_not_available();
    }

    // =========================================================================
    // Stage: Given
    // =========================================================================

    public static class GivenUndoManager extends Stage<GivenUndoManager> {

        @ProvidedScenarioState
        UndoRedoManager manager;

        @ProvidedScenarioState
        TrackableEdit lastEdit;

        @ProvidedScenarioState
        List<TrackableEdit> groupedEdits = new ArrayList<>();

        public GivenUndoManager a_fresh_undo_manager() {
            manager = new UndoRedoManager();
            return self();
        }

        public GivenUndoManager a_fresh_manager_with_limit_$(int limit) {
            manager = new UndoRedoManager();
            manager.setLimit(limit);
            return self();
        }

        public GivenUndoManager a_drawing_action_named_$_has_been_performed(String name) {
            if (manager == null) {
                manager = new UndoRedoManager();
            }
            lastEdit = new TrackableEdit(name);
            manager.addEdit(lastEdit);
            return self();
        }

        public GivenUndoManager the_action_has_been_undone() {
            manager.undo();
            return self();
        }

        public GivenUndoManager $_drawing_actions_have_been_performed(int count) {
            for (int i = 1; i <= count; i++) {
                manager.addEdit(new TrackableEdit("Edit " + i));
            }
            return self();
        }

        /**
         * Creates a CompositeEdit grouping {@code count} sub-edits so that
         * a single undo reverts all of them (AC5).
         *
         * Pattern: fire composite once to open it, fire sub-edits, fire the
         * same composite instance again to close it (see CompositeEdit Javadoc).
         */
        public GivenUndoManager a_composite_of_$_sub_edits_named_$(int count, String name) {
            manager = new UndoRedoManager();
            groupedEdits = new ArrayList<>();
            CompositeEdit composite = new CompositeEdit(name);
            manager.addEdit(composite);
            for (int i = 0; i < count; i++) {
                TrackableEdit sub = new TrackableEdit(name + " step " + (i + 1));
                groupedEdits.add(sub);
                manager.addEdit(sub);
            }
            manager.addEdit(composite);
            return self();
        }

        /**
         * Creates a CompositeEdit wrapping simultaneous edits to N objects (AC7).
         */
        public GivenUndoManager a_group_edit_modifying_$_objects(int objectCount) {
            manager = new UndoRedoManager();
            groupedEdits = new ArrayList<>();
            CompositeEdit group = new CompositeEdit("Move " + objectCount + " Objects");
            manager.addEdit(group);
            for (int i = 0; i < objectCount; i++) {
                TrackableEdit edit = new TrackableEdit("Object " + (i + 1) + " change");
                groupedEdits.add(edit);
                manager.addEdit(edit);
            }
            manager.addEdit(group);
            return self();
        }
    }

    // =========================================================================
    // Stage: When
    // =========================================================================

    public static class WhenUndoRedo extends Stage<WhenUndoRedo> {

        @ExpectedScenarioState
        UndoRedoManager manager;

        @ProvidedScenarioState
        int successfulUndoCount;

        public WhenUndoRedo the_user_triggers_undo() {
            manager.undo();
            return self();
        }

        public WhenUndoRedo the_user_triggers_redo() {
            manager.redo();
            return self();
        }

        public WhenUndoRedo the_user_undoes_all_available_edits() {
            successfulUndoCount = 0;
            while (manager.canUndo()) {
                manager.undo();
                successfulUndoCount++;
            }
            return self();
        }

        public WhenUndoRedo the_user_observes_the_toolbar() {
            return self();
        }

        public WhenUndoRedo a_new_action_named_$_is_performed(String name) {
            manager.addEdit(new TrackableEdit(name));
            return self();
        }

        public WhenUndoRedo the_drawing_is_saved() {
            manager.discardAllEdits();
            return self();
        }
    }

    // =========================================================================
    // Stage: Then
    // =========================================================================

    public static class ThenUndoRedo extends Stage<ThenUndoRedo> {

        @ExpectedScenarioState
        UndoRedoManager manager;

        @ExpectedScenarioState
        TrackableEdit lastEdit;

        @ExpectedScenarioState
        List<TrackableEdit> groupedEdits;

        @ExpectedScenarioState
        int successfulUndoCount;

        public ThenUndoRedo undo_is_available() {
            assertThat(manager.canUndo())
                    .as("undo should be available").isTrue();
            return self();
        }

        public ThenUndoRedo undo_is_not_available() {
            assertThat(manager.canUndo())
                    .as("undo should not be available").isFalse();
            return self();
        }

        public ThenUndoRedo redo_is_available() {
            assertThat(manager.canRedo())
                    .as("redo should be available").isTrue();
            return self();
        }

        public ThenUndoRedo redo_is_not_available() {
            assertThat(manager.canRedo())
                    .as("redo should not be available").isFalse();
            return self();
        }

        public ThenUndoRedo the_edit_is_reverted() {
            assertThat(lastEdit.undoCount)
                    .as("edit undo invocation count").isEqualTo(1);
            return self();
        }

        public ThenUndoRedo the_edit_is_reapplied() {
            assertThat(lastEdit.redoCount)
                    .as("edit redo invocation count").isEqualTo(1);
            return self();
        }

        public ThenUndoRedo exactly_$_edits_were_undone(int expected) {
            assertThat(successfulUndoCount)
                    .as("number of edits successfully undone").isEqualTo(expected);
            return self();
        }

        public ThenUndoRedo the_undo_action_is_disabled() {
            assertThat(manager.getUndoAction().isEnabled())
                    .as("undo Action.isEnabled()").isFalse();
            return self();
        }

        public ThenUndoRedo the_undo_action_is_enabled() {
            assertThat(manager.getUndoAction().isEnabled())
                    .as("undo Action.isEnabled()").isTrue();
            return self();
        }

        public ThenUndoRedo the_undo_action_tooltip_contains(String text) {
            String tooltip = (String) manager.getUndoAction()
                    .getValue(Action.SHORT_DESCRIPTION);
            assertThat(tooltip)
                    .as("undo action tooltip").contains(text);
            return self();
        }

        public ThenUndoRedo the_redo_action_is_disabled() {
            assertThat(manager.getRedoAction().isEnabled())
                    .as("redo Action.isEnabled()").isFalse();
            return self();
        }

        public ThenUndoRedo the_redo_action_is_enabled() {
            assertThat(manager.getRedoAction().isEnabled())
                    .as("redo Action.isEnabled()").isTrue();
            return self();
        }

        public ThenUndoRedo all_$_grouped_edits_are_reverted(int expectedCount) {
            assertThat(groupedEdits)
                    .as("grouped edit list size").hasSize(expectedCount);
            for (TrackableEdit edit : groupedEdits) {
                assertThat(edit.undoCount)
                        .as("undo count for grouped edit '%s'", edit.getName())
                        .isEqualTo(1);
            }
            return self();
        }
    }

    // =========================================================================
    // Helper: TrackableEdit — records how many times undo/redo was called
    // =========================================================================

    static class TrackableEdit extends AbstractUndoableEdit {

        private static final long serialVersionUID = 1L;
        private final String name;
        int undoCount = 0;
        int redoCount = 0;

        TrackableEdit(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            undoCount++;
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            redoCount++;
        }

        @Override
        public String getPresentationName() {
            return name;
        }
    }
}
