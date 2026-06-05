/*
 * @(#)UndoRedoBddTest.java
 *
 * Behaviour-Driven tests for the Undo/Redo feature, written as JGiven
 * Given-When-Then scenarios that mirror the feature's user stories.
 */
package org.jhotdraw.undo.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import org.jhotdraw.undo.UndoRedoManager;
import org.junit.Test;

/**
 * BDD scenarios for the Undo/Redo feature, automated with JGiven and AssertJ.
 *
 * <p>User stories mapped to scenarios:</p>
 * <ul>
 *   <li><b>US-1</b> — As a drawing user, I want to undo my last change so that I
 *       can correct mistakes.</li>
 *   <li><b>US-2</b> — As a drawing user, I want to redo a change I undid so that
 *       I can reapply it.</li>
 *   <li><b>US-3</b> — As a drawing user, I want Undo to be safe when there is
 *       nothing to undo so that the application does not crash.</li>
 * </ul>
 */
public class UndoRedoBddTest extends ScenarioTest<
        UndoRedoBddTest.GivenAnUndoManager,
        UndoRedoBddTest.WhenTheUser,
        UndoRedoBddTest.ThenTheOutcome> {

    // US-1: Given an edit has been recorded, When the user undoes, Then it is reversed.
    @Test
    public void user_can_undo_the_last_change() {
        given().an_undo_manager()
                .and().an_edit_has_been_recorded();
        when().the_user_triggers_undo();
        then().the_last_edit_is_reversed()
                .and().redo_becomes_available();
    }

    // US-2: Given an edit has been undone, When the user redoes, Then it is reapplied.
    @Test
    public void user_can_redo_an_undone_change() {
        given().an_undo_manager()
                .and().an_edit_has_been_recorded()
                .and().the_edit_has_been_undone();
        when().the_user_triggers_redo();
        then().the_last_edit_is_reapplied()
                .and().undo_becomes_available_again();
    }

    // US-3 (boundary): Given nothing recorded, When the user undoes, Then it is rejected.
    @Test
    public void undo_is_rejected_when_there_is_nothing_to_undo() {
        given().an_undo_manager();
        when().the_user_triggers_undo_expecting_failure();
        then().the_undo_is_rejected_safely();
    }

    /** Test double: a significant edit that records how often it is undone/redone. */
    public static final class RecordingEdit extends AbstractUndoableEdit {
        private static final long serialVersionUID = 1L;
        int undoCount = 0;
        int redoCount = 0;

        @Override
        public void undo() {
            super.undo();
            undoCount++;
        }

        @Override
        public void redo() {
            super.redo();
            redoCount++;
        }
    }

    // ---------------------------------------------------------------- GIVEN stage

    public static class GivenAnUndoManager extends Stage<GivenAnUndoManager> {

        @ScenarioState
        UndoRedoManager manager;
        @ScenarioState
        RecordingEdit lastEdit;

        public GivenAnUndoManager an_undo_manager() {
            manager = new UndoRedoManager();
            return self();
        }

        public GivenAnUndoManager an_edit_has_been_recorded() {
            lastEdit = new RecordingEdit();
            manager.addEdit(lastEdit);
            return self();
        }

        public GivenAnUndoManager the_edit_has_been_undone() {
            manager.undo();
            return self();
        }
    }

    // ----------------------------------------------------------------- WHEN stage

    public static class WhenTheUser extends Stage<WhenTheUser> {

        @ScenarioState
        UndoRedoManager manager;
        @ScenarioState
        CannotUndoException caughtUndoError;

        public WhenTheUser the_user_triggers_undo() {
            manager.undo();
            return self();
        }

        public WhenTheUser the_user_triggers_redo() {
            manager.redo();
            return self();
        }

        public WhenTheUser the_user_triggers_undo_expecting_failure() {
            try {
                manager.undo();
            } catch (CannotUndoException e) {
                caughtUndoError = e;
            }
            return self();
        }
    }

    // ----------------------------------------------------------------- THEN stage

    public static class ThenTheOutcome extends Stage<ThenTheOutcome> {

        @ScenarioState
        UndoRedoManager manager;
        @ScenarioState
        RecordingEdit lastEdit;
        @ScenarioState
        CannotUndoException caughtUndoError;

        public ThenTheOutcome the_last_edit_is_reversed() {
            assertThat(lastEdit.undoCount).as("edit was undone once").isEqualTo(1);
            return self();
        }

        public ThenTheOutcome redo_becomes_available() {
            assertThat(manager.canRedo()).as("redo is now possible").isTrue();
            return self();
        }

        public ThenTheOutcome the_last_edit_is_reapplied() {
            assertThat(lastEdit.redoCount).as("edit was redone once").isEqualTo(1);
            return self();
        }

        public ThenTheOutcome undo_becomes_available_again() {
            assertThat(manager.canUndo()).as("undo is possible again").isTrue();
            return self();
        }

        public ThenTheOutcome the_undo_is_rejected_safely() {
            assertThat(caughtUndoError)
                    .as("undo on an empty stack is rejected with CannotUndoException")
                    .isInstanceOf(CannotUndoException.class);
            assertThat(manager.canUndo()).as("still nothing to undo").isFalse();
            return self();
        }
    }
}
