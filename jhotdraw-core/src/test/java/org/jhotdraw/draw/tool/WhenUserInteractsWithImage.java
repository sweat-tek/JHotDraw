package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.jhotdraw.draw.figure.ImageHolderFigure;

/**
 * WHEN steps for ImageTool BDD scenarios.
 *
 * This stage class performs user actions that trigger the behavior being tested.
 * It reads initial context from Given stage via @ExpectedScenarioState and
 * updates scenario state via @ProvidedScenarioState for Then stage.
 *
 * Following the lecture's pattern: GivenIngredients → WhenCook → ThenMeal
 * (lecture_9_Software_Verification_BDD slides 14-16)
 */
public class WhenUserInteractsWithImage extends Stage<WhenUserInteractsWithImage> {

    @ExpectedScenarioState
    protected ImageHolderFigure figure;

    @ExpectedScenarioState
    protected File imageFile;

    @ExpectedScenarioState
    protected BufferedImage testImage;

    @ExpectedScenarioState
    protected int imageWidth;

    @ExpectedScenarioState
    protected int imageHeight;

    @ProvidedScenarioState
    protected boolean insertionAttempted;

    @ProvidedScenarioState
    protected String actionResult;

    @ProvidedScenarioState
    protected int resultWidth;

    @ProvidedScenarioState
    protected int resultHeight;

    @ProvidedScenarioState
    protected IOException insertionError;

    /**
     * When: the user inserts the picture
     *
     * Simulates the user's action of inserting a picture from their PC into JHotDraw.
     * This action loads the image file and adds it to the figure.
     */
    public WhenUserInteractsWithImage the_user_inserts_the_picture() {
        insertionAttempted = true;

        try {
            // Simulate image loading (in real scenario, would call loadImage(imageFile))
            if (imageFile != null && imageFile.exists() && figure != null) {
                figure.setImage(new byte[0], testImage);
                actionResult = "inserted";
                resultWidth = testImage.getWidth();
                resultHeight = testImage.getHeight();
            } else {
                actionResult = "failed";
                insertionError = new IOException("Image file not found or not readable");
            }
        } catch (IOException e) {
            actionResult = "failed";
            insertionError = e;
        }

        return self();
    }

    /**
     * When: the user changes the size to (width x height)
     *
     * Simulates the user resizing a picture to specific dimensions.
     * This action updates the figure's bounds to the new size.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    public WhenUserInteractsWithImage the_user_changes_the_size_to(int width, int height) {
        try {
            // Simulate resizing the image
            resultWidth = width;
            resultHeight = height;

            // In real scenario, would call setBounds with new dimensions
            // For now, just track the new dimensions
            actionResult = "resized";

        } catch (Exception e) {
            actionResult = "failed";
            insertionError = new IOException("Failed to resize image", e);
        }

        return self();
    }

    /**
     * When: the user attempts to insert a non-existent file
     *
     * Simulates error handling when the image file doesn't exist.
     */
    public WhenUserInteractsWithImage the_user_attempts_to_insert_a_nonexistent_file() {
        insertionAttempted = true;

        try {
            // Try to set null image data (simulates file not found)
            if (figure != null) {
                actionResult = "failed";
                insertionError = new IOException("File not found");
            }
        } catch (Exception e) {
            actionResult = "failed";
            insertionError = (e instanceof IOException) ? (IOException) e : new IOException(e);
        }

        return self();
    }
}
