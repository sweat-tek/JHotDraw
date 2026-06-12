package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import java.io.File;

/**
 * WHEN steps for ImageTool BDD scenarios
 */
public class WhenUserInteractsWithImage extends Stage<WhenUserInteractsWithImage> {
    
    @ExpectedScenarioState
    File imageFile;
    
    @ExpectedScenarioState
    String imageStatus;
    
    @ProvidedScenarioState
    boolean insertionAttempted = false;
    
    @ProvidedScenarioState
    String actionResult = "pending";
    
    @ProvidedScenarioState
    int imageWidth = 0;
    
    @ProvidedScenarioState
    int imageHeight = 0;
    
    public WhenUserInteractsWithImage i_want_to_insert_it() {
        insertionAttempted = true;
        if (imageFile != null && imageFile.exists()) {
            actionResult = "inserted";
            imageWidth = 200;
            imageHeight = 150;
        } else {
            actionResult = "failed";
        }
        return self();
    }
    
    public WhenUserInteractsWithImage i_want_to_edit_size_of_the_picture() {
        imageWidth = 400;
        imageHeight = 300;
        actionResult = "resized";
        return self();
    }
}
