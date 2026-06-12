package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

/**
 * BDD Acceptance Tests for ImageTool using JGiven
 * 
 * User Story 1: Image Tool - Insert
 * As a user, I want to insert an image from my PC so that I can display it in JHotDraw
 * 
 * User Story 2: Image Tool - Edit
 * As a user, I want to edit the size of a picture so that I can adjust its dimensions
 */
public class ImageToolJGivenTest extends ScenarioTest<GivenImageToolState, WhenUserInteractsWithImage, ThenImageBehavesCorrectly> {
    
    /**
     * Scenario: User inserts image from PC
     * Given I have a picture downloaded on my pc
     * When I want to insert it
     * Then I can display it in JHotDraw
     */
    @Test
    public void user_can_insert_image_from_pc() {
        given()
            .i_have_a_picture_downloaded_on_my_pc();
        
        when()
            .i_want_to_insert_it();
        
        then()
            .i_can_display_it_in_jhotdraw();
    }
    
    /**
     * Scenario: User edits image size
     * Given I have picture in JHotDraw
     * When I want to edit size of the picture
     * Then I am able to change size of the picture
     */
    @Test
    public void user_can_edit_image_size() {
        given()
            .i_have_picture_in_jhotdraw();
        
        when()
            .i_want_to_edit_size_of_the_picture();
        
        then()
            .i_am_able_to_change_size_of_the_picture();
    }
}
