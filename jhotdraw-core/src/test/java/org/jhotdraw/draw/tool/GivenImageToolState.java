package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * GIVEN steps for ImageTool BDD scenarios.
 *
 * This stage class sets up the initial context for BDD scenarios.
 * It provides scenario state via @ProvidedScenarioState annotations
 * that are shared with When and Then stages.
 *
 * Following the lecture's pattern: GivenIngredients → WhenCook → ThenMeal
 * (lecture_9_Software_Verification_BDD slides 14-16)
 */
public class GivenImageToolState extends Stage<GivenImageToolState> {

    @ProvidedScenarioState
    protected ImageHolderFigure figure;

    @ProvidedScenarioState
    protected File imageFile;

    @ProvidedScenarioState
    protected String imageStatus;

    @ProvidedScenarioState
    protected BufferedImage testImage;

    @ProvidedScenarioState
    protected int imageWidth = 400;

    @ProvidedScenarioState
    protected int imageHeight = 300;

    /**
     * Given: a picture file on the PC
     *
     * Creates a temporary test image file that simulates a picture
     * downloaded on the user's computer.
     *
     * Acceptance criteria: image file exists and is readable
     */
    public GivenImageToolState a_picture_file_on_the_pc() {
        try {
            // Create a temporary test image file (PNG format)
            imageFile = File.createTempFile("test-image", ".png");
            imageFile.deleteOnExit();

            // Create test BufferedImage
            testImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

            // Mark status as file available
            imageStatus = "file_available";

        } catch (IOException e) {
            throw new RuntimeException("Failed to create test image file", e);
        }
        return self();
    }

    /**
     * Given: a picture loaded in JHotDraw
     *
     * Creates and loads an image figure into JHotDraw, simulating
     * a picture that has already been inserted into the canvas.
     *
     * Acceptance criteria: picture is loaded, ready for editing
     */
    public GivenImageToolState a_picture_loaded_in_jhotdraw() throws IOException {
        // First, ensure we have a picture file
        a_picture_file_on_the_pc();

        // Create a mock figure representing loaded image
        figure = mock(ImageHolderFigure.class);
        org.mockito.Mockito.when(figure.getBufferedImage()).thenReturn(testImage);
        org.mockito.Mockito.when(figure.getImageData()).thenReturn(new byte[0]);

        // Mark status as loaded
        imageStatus = "loaded_in_jhotdraw";

        return self();
    }
}
