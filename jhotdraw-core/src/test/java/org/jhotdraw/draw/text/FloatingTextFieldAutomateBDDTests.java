package org.jhotdraw.draw.text;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

public class FloatingTextFieldAutomateBDDTests extends ScenarioTest<GivenFloatingTextField, WhenFloatingTextField, ThenFloatingTextFieldAssertions> {

    @Test
    public void opening_the_text_creator() {
        given().the_user_opens_the_drawing_tool();
        when().the_user_selects_the_Text_tool();
        then().text_creator_overlay_should_appear();
    }
    @Test
    public void writing_text_in_drawing_view() {
        given().the_user_has_the_text_creator_open();
        when().the_user_writes_a_note_in_text_editor();
        then().the_entered_text_should_be_visible_in_the_drawing_area();
    }

    @Test
    public void closing_text_creator() {
        given().the_user_has_the_text_creator_open();
        when().the_user_clicks_outside_of_the_text_field();
        then().the_text_creator_overlay_should_close();
    }
}
