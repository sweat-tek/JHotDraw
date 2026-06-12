package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.junit.ScenarioTest;
import java.io.IOException;
import org.junit.Test;

/**
 * BDD Acceptance Tests for ImageTool using JGiven.
 *
 * Maps user stories to executable Given-When-Then scenarios.
 * Each scenario verifies one acceptance criterion from the feature specification.
 *
 * User Stories (from Lab 2 - Change Request):
 * - Sub-story 1: "As a JHotDraw user I want to insert a picture"
 * - Sub-story 2: "As a JHotDraw user I want to edit existing picture"
 *
 * BDD Benefits (from lecture_9_Software_Verification_BDD):
 * - Stakeholder communication: natural language describes behavior
 * - Living documentation: scenarios are executable specifications
 * - Traceability: direct link from user story to test
 * - Collaboration: domain experts can read and validate scenarios
 *
 * Stage Classes (following lecture slides 14-16):
 * - GivenImageToolState: Sets up initial context
 * - WhenUserInteractsWithImage: Performs user actions
 * - ThenImageBehavesCorrectly: Verifies outcomes (using AssertJ)
 */
public class ImageToolBddTest
        extends ScenarioTest<GivenImageToolState, WhenUserInteractsWithImage, ThenImageBehavesCorrectly> {

    /**
     * Scenario: User can insert an image from PC
     *
     * User Story: "As a JHotDraw user I want to insert a picture
     *            so that I can display it on the canvas"
     *
     * Acceptance Criteria:
     * - Image file exists and is readable
     * - Insertion completes successfully
     * - Image has positive dimensions (width > 0, height > 0)
     * - Image is visible on the canvas
     */
    @Test
    public void user_can_insert_image_from_pc() {
        given()
                .a_picture_file_on_the_pc();

        when()
                .the_user_inserts_the_picture();

        then()
                .the_picture_is_displayed_in_jhotdraw();
    }

    /**
     * Scenario: User can edit image size
     *
     * User Story: "As a JHotDraw user I want to edit existing picture
     *            so that I can adjust its dimensions"
     *
     * Acceptance Criteria:
     * - Picture is loaded in JHotDraw
     * - Size can be changed
     * - New dimensions are applied correctly (e.g., 400x300)
     * - Picture displayed at new size
     */
    @Test
    public void user_can_edit_image_size() throws IOException {
        given()
                .a_picture_loaded_in_jhotdraw();

        when()
                .the_user_changes_the_size_to(400, 300);

        then()
                .the_picture_is_shown_at_size(400, 300);
    }

    /**
     * Scenario: Error displayed when file not found
     *
     * Extension: Error handling for invalid files
     *
     * Acceptance Criteria:
     * - User attempts to insert non-existent file
     * - Error is caught and displayed
     * - No corrupted image appears on canvas
     */
    @Test
    public void error_displayed_when_file_not_found() {
        given()
                .a_picture_file_on_the_pc();

        when()
                .the_user_attempts_to_insert_a_nonexistent_file();

        then()
                .an_error_is_displayed();
    }

    /**
     * Scenario: User can insert and edit in same session
     *
     * Integration: Combined insert + edit workflow
     *
     * Acceptance Criteria:
     * - Insert succeeds
     * - Resize succeeds
     * - Final size is correct
     */
    @Test
    public void user_can_insert_and_then_edit_in_same_session() {
        given()
                .a_picture_file_on_the_pc();

        when()
                .the_user_inserts_the_picture();

        then()
                .the_insertion_was_successful();

        when()
                .the_user_changes_the_size_to(500, 400);

        then()
                .the_picture_is_shown_at_size(500, 400);
    }
}
