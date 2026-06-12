package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.io.File;

/**
 * GIVEN steps for ImageTool BDD scenarios
 */
public class GivenImageToolState extends Stage<GivenImageToolState> {
    
    @ProvidedScenarioState
    File imageFile;
    
    @ProvidedScenarioState
    String imageStatus = "not_loaded";
    
    public GivenImageToolState i_have_a_picture_downloaded_on_my_pc() {
        try {
            imageFile = File.createTempFile("test-image", ".png");
            imageFile.deleteOnExit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test image", e);
        }
        return self();
    }
    
    public GivenImageToolState i_have_picture_in_jhotdraw() {
        imageStatus = "loaded_in_jhotdraw";
        return self();
    }
}
