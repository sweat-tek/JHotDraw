package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import org.jhotdraw.draw.figure.ImageHolderFigure;

/**
 * THEN steps for ImageTool BDD scenarios.
 *
 * This stage class verifies the outcomes of user actions.
 * It uses AssertJ fluent assertions for domain-specific verification.
 *
 * Following the lecture's pattern: GivenIngredients → WhenCook → ThenMeal
 * (lecture_9_Software_Verification_BDD slides 14-16)
 *
 * AssertJ assertions are used because:
 * - More fluent and readable than JUnit assertions
 * - Better error messages
 * - Actively maintained (unlike Hamcrest)
 * - Superset of JUnit capabilities
 * (lecture_9_Software_Verification_BDD slides 22-25)
 */
public class ThenImageBehavesCorrectly extends Stage<ThenImageBehavesCorrectly> {

    @ExpectedScenarioState
    protected ImageHolderFigure figure;

    @ExpectedScenarioState
    protected boolean insertionAttempted;

    @ExpectedScenarioState
    protected String actionResult;

    @ExpectedScenarioState
    protected int resultWidth;

    @ExpectedScenarioState
    protected int resultHeight;

    @ExpectedScenarioState
    protected IOException insertionError;

    /**
     * Then: the picture is displayed in JHotDraw
     *
     * Verifies that:
     * - Insertion was attempted
     * - Insertion succeeded
     * - Image has valid dimensions (width > 0, height > 0)
     *
     * Acceptance criteria: image is visible on canvas with correct dimensions
     */
    public ThenImageBehavesCorrectly the_picture_is_displayed_in_jhotdraw() {
        assertThat(insertionAttempted)
                .as("Image insertion should have been attempted")
                .isTrue();

        assertThat(actionResult)
                .as("Image should be successfully inserted")
                .isEqualTo("inserted");

        assertThat(resultWidth)
                .as("Image should have positive width")
                .isGreaterThan(0);

        assertThat(resultHeight)
                .as("Image should have positive height")
                .isGreaterThan(0);

        // Verify figure has the image loaded
        assertThat(figure)
                .as("Figure should not be null after insertion")
                .isNotNull();

        assertThat(figure.getBufferedImage())
                .as("Figure should contain a BufferedImage")
                .isNotNull();

        return self();
    }

    /**
     * Then: the picture is shown at size (width x height)
     *
     * Verifies that:
     * - Resize action succeeded
     * - New dimensions are applied (exact match to requested size)
     * - Image is still valid after resizing
     *
     * Acceptance criteria: picture displayed at new size (e.g., 400x300)
     */
    public ThenImageBehavesCorrectly the_picture_is_shown_at_size(int expectedWidth, int expectedHeight) {
        assertThat(actionResult)
                .as("Image should be successfully resized")
                .isEqualTo("resized");

        assertThat(resultWidth)
                .as("Image width should match requested size")
                .isEqualTo(expectedWidth);

        assertThat(resultHeight)
                .as("Image height should match requested size")
                .isEqualTo(expectedHeight);

        return self();
    }

    /**
     * Then: an error is displayed
     *
     * Verifies that:
     * - The action failed (not succeeded)
     * - An error was captured
     * - The error provides useful information
     */
    public ThenImageBehavesCorrectly an_error_is_displayed() {
        assertThat(actionResult)
                .as("Action should have failed")
                .isEqualTo("failed");

        assertThat(insertionError)
                .as("An IOException should have been captured")
                .isNotNull();

        assertThat(insertionError.getMessage())
                .as("Error should provide information about the failure")
                .isNotNull()
                .isNotEmpty();

        return self();
    }

    /**
     * Then: no image is displayed
     *
     * Verifies that when insertion fails, no image appears on the canvas.
     */
    public ThenImageBehavesCorrectly no_image_is_displayed() {
        assertThat(actionResult)
                .as("Insertion should have failed")
                .isEqualTo("failed");

        assertThat(insertionAttempted)
                .as("Insertion should have been attempted")
                .isTrue();

        return self();
    }

    /**
     * Then: the insertion was successful
     *
     * General verification that insertion completed without errors.
     */
    public ThenImageBehavesCorrectly the_insertion_was_successful() {
        assertThat(actionResult)
                .as("Insertion should succeed")
                .isEqualTo("inserted");

        assertThat(insertionError)
                .as("No error should have occurred")
                .isNull();

        return self();
    }

    /**
     * Then: the insertion failed
     *
     * General verification that insertion encountered an error.
     */
    public ThenImageBehavesCorrectly the_insertion_failed() {
        assertThat(actionResult)
                .as("Insertion should fail")
                .isEqualTo("failed");

        assertThat(insertionError)
                .as("An error should have been captured")
                .isNotNull();

        return self();
    }
}
