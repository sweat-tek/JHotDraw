package org.jhotdraw.draw.text;

import com.tngtech.jgiven.Stage;

public class WhenFloatingTextField extends Stage<WhenFloatingTextField> {

    public WhenFloatingTextField the_user_writes_a_note_in_text_editor() {
        return self();
    }

    public WhenFloatingTextField the_user_clicks_outside_of_the_text_field() {
        return self();
    }

    public WhenFloatingTextField the_user_selects_the_Text_tool() {
        return self();
    }
}
