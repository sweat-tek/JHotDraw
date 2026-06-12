package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * THEN steps for ImageTool BDD scenarios
 * Verifies outcomes using AssertJ
 */
public class ThenImageBehavesCorrectly extends Stage<ThenImageBehavesCorrectly> {
    
    @ExpectedScenarioState
    boolean insertionAttempted;
    
    @ExpectedScenarioState
    String actionResult;
    
    @ExpectedScenarioState
    int imageWidth;
    
    @ExpectedScenarioState
    int imageHeight;
    
    public ThenImageBehavesCorrectly i_can_display_it_in_jhotdraw() {
        assertThat(insertionAttempted)
            .as("Image insertion should have been attempted")
            .isTrue();
        
        assertThat(actionResult)
            .as("Image should be successfully inserted")
            .isEqualTo("inserted");
        
        assertThat(imageWidth)
            .as("Image should have positive width")
            .isGreaterThan(0);
        
        assertThat(imageHeight)
            .as("Image should have positive height")
            .isGreaterThan(0);
        
        return self();
    }
    
    public ThenImageBehavesCorrectly i_am_able_to_change_size_of_the_picture() {
        assertThat(actionResult)
            .as("Image size should be changed")
            .isEqualTo("resized");
        
        assertThat(imageWidth)
            .as("Image width should be updated to 400")
            .isEqualTo(400);
        
        assertThat(imageHeight)
            .as("Image height should be updated to 300")
            .isEqualTo(300);
        
        return self();
    }
}
